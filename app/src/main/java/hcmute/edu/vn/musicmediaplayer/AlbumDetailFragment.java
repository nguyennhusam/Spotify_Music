package hcmute.edu.vn.musicmediaplayer;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Adapter.AlbumAdapter;
import hcmute.edu.vn.musicmediaplayer.Adapter.SearchAdapter;
import hcmute.edu.vn.musicmediaplayer.Adapter.SongAdapter;
import hcmute.edu.vn.musicmediaplayer.Model.Album;
import hcmute.edu.vn.musicmediaplayer.Model.Song;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumDetailFragment extends Fragment {

    public static final String ARG_ALBUM = "album_title";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView songRC;
    private TextView albumName;
    private TextView albumDescription;
    private ImageView albumPhoto;
    private Button btnBack;
    DatabaseReference myRef;


    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumDetailFragment newInstance(String param1, String param2) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
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
        View v = inflater.inflate(R.layout.fragment_album_detail, container, false);
        albumPhoto = v.findViewById(R.id.img_album_photo);
        albumName= v.findViewById(R.id.tv_album_name);
        albumDescription= v.findViewById(R.id.tv_album_description);
        songRC = v.findViewById(R.id.rcv_songs);
        btnBack = v.findViewById(R.id.buttonBack1);

        Bundle args = getArguments();
        String albumId = args.getString(ARG_ALBUM);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        myRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("albums").child(albumId);
        setHasOptionsMenu(true);
        songRC.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Song> songRVModelArrayList = new ArrayList<>();
        SongAdapter songAdapter = new SongAdapter( songRVModelArrayList,getContext());
        songRC.setAdapter(songAdapter);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Album object = snapshot.getValue(Album.class);

                if (object!=null && object.getSongs() != null && !object.getSongs().isEmpty())
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Song song = dataSnapshot.getValue(Song.class);
                        songRVModelArrayList.add(song);
                    }
                    songAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        // Inflate the layout for this fragment
        return v;
    }
}