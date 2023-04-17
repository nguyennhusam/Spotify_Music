package hcmute.edu.vn.musicmediaplayer.Adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.musicmediaplayer.Activity.PlayMusicActivity;
import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.myViewholder> implements Filterable {

    Context context;
    List<Song> listSong;
    List<Song> filteredList;

    public SearchAdapter(Context context, List<Song> list) {
        this.context = context;
        this.listSong = list;
        this.filteredList = new ArrayList<>(listSong); // tạo một bản sao của danh sách ban đầu
    }


    @NonNull
    @Override
    public SearchAdapter.myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_song_item_search, parent, false);
        return new SearchAdapter.myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.myViewholder holder, int position) {
        Song song = listSong.get(position);
        if(song == null){
            return;
        }
        holder.txttentimkiem.setText(song.getsName());
        holder.txtcasitimkiem.setText(song.getsArtist());
        Picasso.get().load(song.getsImageUrl()).into(holder.imganhtimkiem);

        holder.imganhtimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayMusicActivity.class);

                //truyền vào intent một Song vị trí position
                intent.putExtra("play_music_from_search", listSong.get(position));
                context.startActivity(intent);
            }
        });
    }
    public void setFullList(List<Song> list){

        this.filteredList = list;
    }
    @Override
    public int getItemCount() {
        if(listSong!=null){
            return listSong.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchText = constraint.toString().toLowerCase();
                List<Song> templist = new ArrayList<>();
                System.out.println(searchText+" -- ");
                if (searchText.length()==0 ||searchText.isEmpty()) {
                    templist.addAll(filteredList);
                } else {
                    for (Song item : filteredList) {

                        if(item.getsName() != null) {
                            if (item.getsName().toLowerCase().contains(searchText)) {
                                templist.add(item);
                            }
                        }
                    }
                }
                FilterResults filterresults = new FilterResults();
                filterresults.values = templist;
                return filterresults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listSong =(List<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class myViewholder extends RecyclerView.ViewHolder {

        TextView txttentimkiem, txtcasitimkiem;
        ImageView imganhtimkiem;

        public myViewholder(@NonNull View itemView) {
            super(itemView);
            txttentimkiem = itemView.findViewById(R.id.txttennhac);
            txtcasitimkiem = itemView.findViewById(R.id.txtcasinhac);
            imganhtimkiem = itemView.findViewById(R.id.imgnhac);

        }
    }

}
