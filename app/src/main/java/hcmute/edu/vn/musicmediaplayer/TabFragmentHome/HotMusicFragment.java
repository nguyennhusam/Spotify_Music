package hcmute.edu.vn.musicmediaplayer.TabFragmentHome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Adapter.SearchAdapter;
import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;

public class HotMusicFragment extends Fragment {

    private DatabaseReference myRef;
    RecyclerView recyclerViewHotMusic;
    SearchAdapter hotMusicAdapter;
    ArrayList<Song> listSong;



    public HotMusicFragment() {
        // Required empty public constructor
    }

    public static HotMusicFragment newInstance(String param1, String param2) {
        HotMusicFragment fragment = new HotMusicFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotmusic,container, false);
        recyclerViewHotMusic = view.findViewById(R.id.recyclerviewhitsongs);
        recyclerViewHotMusic.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("hitSong");
        setHasOptionsMenu(true);
        recyclerViewHotMusic.setLayoutManager(new LinearLayoutManager(getContext()));
        listSong = new ArrayList<>();
        hotMusicAdapter = new SearchAdapter(getContext(), listSong);
        recyclerViewHotMusic.setAdapter(hotMusicAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Song song = dataSnapshot.getValue(Song.class);
                    listSong.add(song);
                }
                hotMusicAdapter.setFullList(new ArrayList<>(listSong));
                hotMusicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setHasOptionsMenu(true);
        return view;
    }
}