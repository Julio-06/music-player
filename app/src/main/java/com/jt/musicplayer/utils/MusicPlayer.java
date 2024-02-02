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

    public void changePosition(Context context, int newPosition) {
        Intent musicPlayerService = new Intent(context, MusicPlayerService.class);
        musicPlayerService.setAction(MusicPlayerService.ACTION_CHANGE_POSITION);
        musicPlayerService.putExtra("CHANGE_POSITION", newPosition);
        context.startService(musicPlayerService);
    }
}
