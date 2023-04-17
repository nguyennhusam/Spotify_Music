package hcmute.edu.vn.musicmediaplayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.AlbumDetailFragment;
import hcmute.edu.vn.musicmediaplayer.AlbumDetailsActivity;
import hcmute.edu.vn.musicmediaplayer.LibraryFragment;
import hcmute.edu.vn.musicmediaplayer.Model.Album;
import hcmute.edu.vn.musicmediaplayer.R;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    // creating variables for array list and context
    private ArrayList<Album> albumRVModalArrayList;
    private Context context;

    private FragmentManager mFragmentManager;


    // creating a constructor.
    public AlbumAdapter(ArrayList<Album> albumRVModalArrayList, FragmentManager context) {
        this.albumRVModalArrayList = albumRVModalArrayList;
        this.mFragmentManager = context;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layout file on below line.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_album_items, parent, false);
        System.out.println("From Adapter: "+ getItemCount());
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // setting data to text view and image view on below line.

            Album albumModal = albumRVModalArrayList.get(position);
            Picasso.get().load(albumModal.getAlbumImageUrl()).into(holder.albumIV);
            holder.albumNameTV.setText(albumModal.getName());
            System.out.println(albumModal.getTotalSong());
            holder.albumTotalSongTV.setText(albumModal.getTotalSong()+" bài");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlbumDetailFragment fragment = new AlbumDetailFragment();
                    Bundle args = new Bundle();
                    args.putString(AlbumDetailFragment.ARG_ALBUM,  albumRVModalArrayList.get(position).getId());
                    fragment.setArguments(args);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_library, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

//                    Intent intent = new Intent(context, AlbumDetailsActivity.class);
//                    intent.putExtra("albumId",albumRVModalArrayList.get(position).getId());
//                    context.startActivity(intent);
                }
            });
        // adding click listener for album item on`  below line.
//        holder.itemView.setOnClickListener(new V` iew.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // on below line opening a new album detail
//                // activity for displaying songs within that album.
//                Intent i = new Intent(context, AlbumDetailActivity.class);
//                // on below line passing album related data.
//                i.putExtra("id", albumRVModal.id);
//                i.putExtra("name", albumRVModal.name);
//                i.putExtra("img", albumRVModal.imageUrl);
//                i.putExtra("artist", albumRVModal.artistName);
//                i.putExtra("albumUrl", albumRVModal.external_urls);
//                context.startActivity(i);
//            }
//        });
    }

    // on below line returning the size of list
    @Override
    public int getItemCount() {
        return albumRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // on below line creating variables
        // for image view and text view.
        private ImageView albumIV;
        private TextView albumNameTV, albumTotalSongTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // on below line initializing variables.
            albumIV = itemView.findViewById(R.id.album_img);
            albumNameTV = itemView.findViewById(R.id.tv_title_album);
            albumTotalSongTV = itemView.findViewById(R.id.tv_album_total);
        }
    }
}