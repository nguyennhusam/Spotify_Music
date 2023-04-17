package hcmute.edu.vn.musicmediaplayer.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.AlbumDetailFragment;
import hcmute.edu.vn.musicmediaplayer.Model.Album;
import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{

    private ArrayList<Song> songsRVModalArrayList;
    private Context context;


    public SongAdapter(ArrayList<Song> songsRVModalArrayList, Context context) {
        this.songsRVModalArrayList =songsRVModalArrayList ;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_song_item_search, parent, false);
        System.out.println("From Adapter: "+ getItemCount());
        return new SongAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song songModel = songsRVModalArrayList.get(position);
        Picasso.get().load(songModel.getsImageUrl()).into(holder.songIV);
        holder.songName.setText(songModel.getsName());
        holder.songArtist.setText(songModel.getsArtist());


    }



    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // on below line creating variables
        // for image view and text view.
        private ImageView songIV;
        private TextView songName, songArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // on below line initializing variables.
            songIV = itemView.findViewById(R.id.imgnhac);
            songName = itemView.findViewById(R.id.txttennhac);
            songArtist = itemView.findViewById(R.id.txtcasinhac);
        }
    }
}