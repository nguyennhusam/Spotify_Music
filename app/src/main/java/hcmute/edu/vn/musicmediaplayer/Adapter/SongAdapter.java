package hcmute.edu.vn.musicmediaplayer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.musicmediaplayer.AlbumDetailFragment;
import hcmute.edu.vn.musicmediaplayer.Model.Album;
import hcmute.edu.vn.musicmediaplayer.Model.DBHandler;
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
                .inflate(R.layout.layout_song_item_albums, parent, false);
        System.out.println("From Adapter: "+ getItemCount());
        return new SongAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("From Song Adapter "+getItemCount());
        Song songModel = songsRVModalArrayList.get(position);
        holder.songName.setText(songModel.getsName());
        holder.songArtist.setText(songModel.getsArtist());

        File filePath = new File (songModel.getsImageUrl());
        Picasso.get()
                .load(filePath)
                .error(R.drawable.hot_music)
                .into(holder.songIV);


        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    // Cập nhật lại danh sách bài hát trong Adapter và cập nhật hiển thị

                    File fileImg = new File(songModel.getsImageUrl());
                    if (fileImg.exists()) {
                        fileImg.delete();
                    }
                    File fileAudio = new File(songModel.getsImageUrl());
                    if (fileAudio.exists()) {
                        fileAudio.delete();
                    }
                    holder.db.deleteCourse(songModel.getSongId());
//                    Intent intent = new Intent("updateSongList");
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

                    Toast.makeText(context, "Delete song from album", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    public void setSongList(ArrayList<Song> songList) {
        songsRVModalArrayList =  songList;
    }



    @Override
    public int getItemCount() {
        return songsRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // on below line creating variables
        // for image view and text view.
        private ImageView songIV, deleteIV;
        private TextView songName, songArtist;
        private DBHandler db;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // on below line initializing variables.
            songIV = itemView.findViewById(R.id.imgnhac1);
            songName = itemView.findViewById(R.id.txttennhac1);
            songArtist = itemView.findViewById(R.id.txtcasinhac1);
            deleteIV = itemView.findViewById(R.id.img_delete);
            db = new DBHandler(context);

        }
    }
}