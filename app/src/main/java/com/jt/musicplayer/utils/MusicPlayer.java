package com.jt.musicplayer.utils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.jt.musicplayer.MusicPlayerService;
import com.jt.musicplayer.R;

import java.security.PublicKey;

public class MusicPlayer {
    private String status;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    public void play(Context context, int uri) {
        Intent musicPlayerService = new Intent(context, MusicPlayerService.class);
        musicPlayerService.setAction(MusicPlayerService.ACTION_PLAY);
        musicPlayerService.putExtra("MUSIC", uri);
        context.startService(musicPlayerService);

    }

    public void pause(Context context) {
        Intent musicPlayerService = new Intent(context, MusicPlayerService.class);
        musicPlayerService.setAction(MusicPlayerService.ACTION_PAUSE);
        context.startService(musicPlayerService);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
}
