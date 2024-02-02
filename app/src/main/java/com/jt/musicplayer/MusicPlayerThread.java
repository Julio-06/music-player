package com.jt.musicplayer;

import android.content.Intent;

public class MusicPlayerThread implements Runnable {
    private boolean exit = false;
    private MusicPlayerService context;
    @Override
    public void run() {
        while (!exit){
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MusicPlayerService.MEDIA_PLAYER_DATA);
            broadcastIntent.putExtra("data", context.getData());
            context.sendBroadcast(broadcastIntent);
        }
    }

    public void stop() {
        exit = true;
    }

    public void setContext(MusicPlayerService context) {
        this.context = context;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }
}
