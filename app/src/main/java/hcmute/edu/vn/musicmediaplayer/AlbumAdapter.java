package hcmute.edu.vn.musicmediaplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Model.Album;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    // creating variables for array list and context
    private ArrayList<Album> albumRVModalArrayList;
    private Fragment context;

    // creating a constructor.
    public AlbumAdapter(ArrayList<Album> albumRVModalArrayList, Fragment context) {
        this.albumRVModalArrayList = albumRVModalArrayList;
        this.context = context;
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
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        // setting data to text view and image view on below line.

            Album albumModal = albumRVModalArrayList.get(position);
            Picasso.get().load(albumModal.getAlbumImageUrl()).into(holder.albumIV);
            holder.albumNameTV.setText(albumModal.getName());
            System.out.println(albumModal.getTotalSong());
            holder.albumTotalSongTV.setText(albumModal.getTotalSong()+" bài");

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