package com.jt.musicplayer.ui.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jt.musicplayer.MusicPlayerService;
import com.jt.musicplayer.R;
import com.jt.musicplayer.databinding.FragmentDashboardBinding;
import com.jt.musicplayer.databinding.FragmentMusicPlayerBinding;
import com.jt.musicplayer.ui.dashboard.DashboardViewModel;
import com.jt.musicplayer.utils.MusicPlayer;

import java.util.concurrent.TimeUnit;

public class MusicPlayerFragment extends Fragment {

    private FragmentMusicPlayerBinding binding;
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMusicPlayerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        musicPlayer = new MusicPlayer();

        ImageButton imgBtnPLay = root.findViewById(R.id.imgBtnPlay);
        imgBtnPLay.setOnClickListener(v -> play(v));

        txtTitle = root.findViewById(R.id.txtTitle);
        txtDuration = root.findViewById(R.id.txtDuration);
        txtCurrentPosition = root.findViewById(R.id.txtCurrentPosition);

        seekBar = root.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Log.i("onProgressChanged", "" + progress);
                    musicPlayer.changePosition(root.getContext(), progress);
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.MEDIA_PLAYER_DATA);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(mReceiver, intentFilter);
    }

    private View.OnClickListener play(View v) {
        isPlay = !isPlay;
        ImageButton vImgBtn = (ImageButton) v;

        if(isPlay){
            vImgBtn.setImageResource(R.drawable.ic_music_stop);
            musicPlayer.play(getContext(), R.raw.wtf);

        }else {
            vImgBtn.setImageResource(R.drawable.ic_music_play);
            musicPlayer.pause(getContext());
        }
        return null;
    }
}