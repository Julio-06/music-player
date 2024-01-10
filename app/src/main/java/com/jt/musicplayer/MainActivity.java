package com.jt.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jt.musicplayer.utils.MusicPlayer;

public class MainActivity extends AppCompatActivity {
    private MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicPlayer = new MusicPlayer();

//        Button btnPLay = findViewById(R.id.btnPlay);
//        Button btnPause = findViewById(R.id.btnPause);
//
//        btnPLay.setOnClickListener(v -> play());
//        btnPause.setOnClickListener(v -> pause());
    }

    private View.OnClickListener play() {
        musicPlayer.play(this, R.raw.wtf);
        return null;
    }

    private View.OnClickListener pause() {
        musicPlayer.pause(this);
        return null;
    }

}