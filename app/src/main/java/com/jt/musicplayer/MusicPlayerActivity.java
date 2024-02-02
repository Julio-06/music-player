package com.jt.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.jt.musicplayer.utils.MusicPlayer;

import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {
    private MusicPlayer musicPlayer;
    private boolean isPlay = false;
    private TextView txtTitle;
    private TextView txtDuration;
    private TextView txtCurrentPosition;
    private SeekBar seekBar;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MusicPlayerService.MEDIA_PLAYER_DATA)) {
                Bundle data = intent.getBundleExtra("data");

                Long duration = data.getLong("duration");
                Long currentPosition = data.getLong("current_position");

                txtTitle.setText(data.getString("title"));
                txtCurrentPosition.setText("" + TimeUnit.MILLISECONDS.toSeconds(currentPosition));
                txtDuration.setText("-" + TimeUnit.MILLISECONDS.toSeconds(duration - currentPosition));

                seekBar.setMax(duration.intValue());
                seekBar.setProgress(currentPosition.intValue());

                Log.i("PRUEBA", "onReceive");

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_music_player);

        musicPlayer = new MusicPlayer();

        ImageButton imgBtnPLay = findViewById(R.id.imgBtnPlay);
        imgBtnPLay.setOnClickListener(v -> play(v));

        txtTitle = findViewById(R.id.txtTitle);
        txtDuration = findViewById(R.id.txtDuration);
        txtCurrentPosition = findViewById(R.id.txtCurrentPosition);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Log.i("onProgressChanged", "" + progress);
                    musicPlayer.changePosition(getApplicationContext(), progress);
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.MEDIA_PLAYER_DATA);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private View.OnClickListener play(View v) {
        isPlay = !isPlay;
        ImageButton vImgBtn = (ImageButton) v;

        if(isPlay){
            vImgBtn.setImageResource(R.drawable.ic_music_stop);
            musicPlayer.play(this, R.raw.wtf);

        }else {
            vImgBtn.setImageResource(R.drawable.ic_music_play);
            musicPlayer.pause(this);
        }
        return null;
    }
}