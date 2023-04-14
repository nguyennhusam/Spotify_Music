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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.myViewholder> implements Filterable {

    Context context;
    ArrayList<Song> listSong;
    ArrayList<Song> filteredList;

    public SearchAdapter(Context context, ArrayList<Song> list) {
        this.context = context;
        this.listSong = list;
        this.filteredList = new ArrayList<>(listSong); // tạo một bản sao của danh sách ban đầu
    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public SearchAdapter.myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_song_item_search, parent, false);
        return new myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.myViewholder holder, int position) {
        Song song = filteredList.get(position);
        holder.txttentimkiem.setText(song.getsName());
        holder.txtcasitimkiem.setText(song.getsArtist());
        Picasso.get().load(song.getsImageUrl()).into(holder.imganhtimkiem);

        holder.imganhtimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void setFullList(ArrayList<Song> list){
        this.filteredList = list;
    }
    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    private Filter filterdata = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchText = constraint.toString().toLowerCase();
            ArrayList<Song> templist = new ArrayList<>();
            if (searchText.length() == 0 || searchText.isEmpty()) {

                templist.addAll(listSong);
            } else {

                for (Song item : listSong) {
                    if (item.getsName().toLowerCase().contains(searchText)) {
                        templist.add(item);
                    }
                }
            }
            FilterResults filterresults = new FilterResults();
            filterresults.values = templist;
            return filterresults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredList.clear();
            filteredList.addAll((Collection<? extends Song>) filterResults.values);
            notifyDataSetChanged();
        }
    };

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
//}public class SearchAdapter extends FirebaseRecyclerAdapter<Song, SearchAdapter.ViewHolder> {
//
//    Context context;
//    ArrayList<Song> ListSong;
//
//    public SearchAdapter(@NonNull FirebaseRecyclerOptions<Song> options) {
//        super(options);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position, @NonNull Song model) {
//        Song song = ListSong.get(position);
//        holder.txttentimkiem.setText(song.getsName());
//        holder.txtcasitimkiem.setText(song.getsArtist());
//        Picasso.get().load(song.getsImageUrl()).into(holder.imganhtimkiem);
//    }
//
//
//    @NonNull
//    @Override
//    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = inflater.inflate(R.layout.layout_song_item_search, parent, false);
//        return new ViewHolder(view);
//    }
//
////    @Override
////    public int getItemCount() {
////        if (ListSong != null) {
////            return ListSong.size();
////        }
////        return 0;
////    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView txttentimkiem, txtcasitimkiem;
//        ImageView imganhtimkiem;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            txttentimkiem = itemView.findViewById(R.id.txttennhac);
//            txtcasitimkiem = itemView.findViewById(R.id.txtcasinhac);
//            imganhtimkiem = itemView.findViewById(R.id.imgnhac);
//
//        }
//    }
//
//}
