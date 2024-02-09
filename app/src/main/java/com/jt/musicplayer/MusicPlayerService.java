package com.jt.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jt.musicplayer.utils.MusicPlayer;

import java.io.IOException;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {
    public static final String ACTION_PLAY = "com.jt.musicplayer.action.PLAY";
    public static final String ACTION_PAUSE = "com.jt.musicplayer.action.PAUSE";
    public static final String ACTION_CHANGE_POSITION = "com.jt.musicplayer.action.POSITION";
    public static final String MEDIA_PLAYER_DATA = "com.jt.musicplayer.action.MEDIA_PLAYER_DATA";
    private static final String TAG = "MusicPlayerService";
    private MediaPlayer mediaPlayer = null;
    MusicPlayerThread musicPlayerThread;
    private int resumePosition = 0;
    private Uri musicUri;
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        Log.i(TAG, "onCreate...");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals(ACTION_PLAY)){
            if(!mediaPlayer.isPlaying() && resumePosition == 0){
                try {
                    String musicPath = intent.getStringExtra("MUSIC");

                    this.setMusicUri(Uri.parse(musicPath));

                    mediaPlayer.setDataSource(this, this.getMusicUri());
                    mediaPlayer.prepareAsync(); // prepare async to not block main thread

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }else {
                mediaPlayer.start();

            }

            musicPlayerThread = new MusicPlayerThread();
            musicPlayerThread.setContext(this);
            musicPlayerThread.setExit(false);

            Thread thread = new Thread(musicPlayerThread);
            thread.start();

        } else if (intent.getAction().equals(ACTION_PAUSE)) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
            musicPlayerThread.stop();

        } else if (intent.getAction().equals(ACTION_CHANGE_POSITION)) {
            Log.i("POSITION", "" + intent.getIntExtra("CHANGE_POSITION", 0));
            mediaPlayer.seekTo(intent.getIntExtra("CHANGE_POSITION", mediaPlayer.getCurrentPosition()));
        }

        Log.i(TAG, "onStartCommand...");

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i(TAG, "onPrepared...");
        mp.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.releaseMediaPlayer();
        Log.i(TAG, "onDestroy...");
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    public void releaseMediaPlayer() {

        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();
            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

        }
    }

    public Bundle getData() {
        Bundle data = new Bundle();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, this.getMusicUri());
        //mediaMetadataRetriever.extractMetadata(mediaMetadataRetriever.METADATA_KEY_TITLE)
        //data.putString("title", mediaPlayer.de);
        data.putLong("duration", mediaPlayer.getDuration());
        data.putLong("current_position", mediaPlayer.getCurrentPosition());

        return data;
    }

    public Uri getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(Uri musicUri) {
        this.musicUri = musicUri;
    }
}