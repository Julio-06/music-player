package com.jt.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.IBinder;
import android.util.Log;

import com.jt.musicplayer.utils.MusicPlayer;

import java.io.IOException;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {
    public static final String ACTION_PLAY = "com.jt.musicplayer.action.PLAY";
    public static final String ACTION_PAUSE = "com.jt.musicplayer.action.PAUSE";
    private static final String TAG = "MusicPlayerService";
    private MediaPlayer mediaPlayer = null;
    private int resumePosition = 0;
    public MusicPlayerService() {
    }

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
                    int music = intent.getIntExtra("MUSIC", 0);
                    Log.i(TAG, mediaPlayer.toString());
                    mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + music));

                    mediaPlayer.prepareAsync(); // prepare async to not block main thread

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                mediaPlayer.start();
            }

        } else if (intent.getAction().equals(ACTION_PAUSE)) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }

        Log.i(TAG, "onStartCommand...");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
}