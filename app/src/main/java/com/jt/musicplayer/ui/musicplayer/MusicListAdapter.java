package com.jt.musicplayer.ui.musicplayer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.jt.musicplayer.databinding.FragmentMyMusicBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> implements View.OnClickListener {
    private HashMap<String, String> localDataSet;
    private String[] keys;
    private View.OnClickListener listener;

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView musicName;
        private View root;
        //private ImageButton imgPlay;

        public ViewHolder(FragmentMyMusicBinding binding) {
            super(binding.getRoot());
            root = binding.getRoot();
            // Define click listener for the ViewHolder's View
            musicName = (TextView) binding.musicName;
            //imgPlay = (ImageButton) binding.imgBtnPlay;
        }

        public TextView getTextView() {
            return musicName;
        }
        public View getRoot() { return root; }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MusicListAdapter(HashMap<String, String> dataSet) {
        localDataSet = dataSet;
        keys = dataSet.keySet().toArray(new String[0]);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        FragmentMyMusicBinding binding = FragmentMyMusicBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        View view = binding.getRoot();
        view.setOnClickListener(this);

        return new MusicListAdapter.ViewHolder(binding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(keys[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
