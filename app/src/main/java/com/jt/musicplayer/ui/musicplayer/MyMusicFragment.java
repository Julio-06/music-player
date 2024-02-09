package com.jt.musicplayer.ui.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jt.musicplayer.R;
import com.jt.musicplayer.ui.MyItemRecyclerViewAdapter;
import com.jt.musicplayer.ui.placeholder.PlaceholderContent;
import com.jt.musicplayer.utils.MusicPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 */
public class MyMusicFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MusicPlayer musicPlayer;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyMusicFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyMusicFragment newInstance(int columnCount) {
        MyMusicFragment fragment = new MyMusicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_music_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //recyclerView.setAdapter(new MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS));
            musicPlayer = new MusicPlayer();

            HashMap<String, String> allMusic = getAllMusic();
            MusicListAdapter musicListAdapter = new MusicListAdapter(allMusic);
            String[] paths = allMusic.values().toArray(new String[0]);

            musicListAdapter.setOnClickListener(v -> {
                File music = new File(paths[recyclerView.getChildAdapterPosition(v)]);

                musicPlayer.play(getContext(), Uri.fromFile(music));
            });
            recyclerView.setAdapter(musicListAdapter);
        }

        return view;
    }

    public HashMap<String, String> getAllMusic() {
        Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA};
        HashMap<String, String> songs = new HashMap<>();
        Cursor c = getContext().getContentResolver().query(uri, projection, null, null, null);

        if(c != null){
            while (c.moveToNext()){
                String path = c.getString(0);
                File file = new File(path);

                songs.put(file.getName(), path);
            }
            c.close();
        }

        return songs;
    }
}