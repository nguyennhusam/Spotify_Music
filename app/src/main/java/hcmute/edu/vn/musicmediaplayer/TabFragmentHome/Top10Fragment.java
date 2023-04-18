package hcmute.edu.vn.musicmediaplayer.TabFragmentHome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Adapter.SearchAdapter;
import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;

public class Top10Fragment extends Fragment {
    private DatabaseReference myRef;
    RecyclerView recyclerViewTop10;
    SearchAdapter top10MusicAdapter;
    ArrayList<Song> listSong;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Top10Fragment() {
        // Required empty public constructor
    }

    public static Top10Fragment newInstance(String param1, String param2) {
        Top10Fragment fragment = new Top10Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top10,container, false);

        recyclerViewTop10 = view.findViewById(R.id.recyclerviewtop10);
        recyclerViewTop10.setHasFixedSize(true);
        //thanh chắn ngăn cách giữa các item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL);
        recyclerViewTop10.addItemDecoration(itemDecoration);

        myRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("top10");
        setHasOptionsMenu(true);
        recyclerViewTop10.setLayoutManager(new LinearLayoutManager(getContext()));
        listSong = new ArrayList<>();
        top10MusicAdapter = new SearchAdapter(getContext(), listSong);
        recyclerViewTop10.setAdapter(top10MusicAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Song song = dataSnapshot.getValue(Song.class);
                    listSong.add(song);
                }
                top10MusicAdapter.setFullList(new ArrayList<>(listSong));
                top10MusicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setHasOptionsMenu(true);
        return view;
    }
}