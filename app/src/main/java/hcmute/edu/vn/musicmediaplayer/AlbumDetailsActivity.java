package hcmute.edu.vn.musicmediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Adapter.SearchAdapter;
import hcmute.edu.vn.musicmediaplayer.Model.Song;

public class AlbumDetailsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ImageView albumPhoto;
    String albumId;
    TextView albumDescription;
    ArrayList<Song> albumSongs= new ArrayList<>();

    private StorageReference imageStorageRef;
    private StorageReference audioStorageRef;


    private DatabaseReference albumDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView = findViewById(R.id.rcv_songs);
        albumId = getIntent().getStringExtra("albumId") ;

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        listSong = new ArrayList<>();
        albumDatabaseRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("albums").child(albumId);
        albumDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




//        recyclerViewtim.setLayoutManager(new LinearLayoutManager(getContext()));
//        listSong = new ArrayList<>();
//        searchAdapter = new SearchAdapter(getContext(), listSong);
//        recyclerViewtim.setAdapter(searchAdapter);
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Song song = dataSnapshot.getValue(Song.class);
//                    listSong.add(song);
//                }
//                searchAdapter.setFullList(new ArrayList<>(listSong));
//                searchAdapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });


    }
}