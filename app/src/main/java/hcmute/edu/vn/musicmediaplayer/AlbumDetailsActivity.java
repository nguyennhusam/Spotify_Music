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

import hcmute.edu.vn.musicmediaplayer.Activity.PlayMusicActivity;
import hcmute.edu.vn.musicmediaplayer.Adapter.SearchAdapter;
import hcmute.edu.vn.musicmediaplayer.Adapter.SongAdapter;
import hcmute.edu.vn.musicmediaplayer.Model.Album;
import hcmute.edu.vn.musicmediaplayer.Model.DBHandler;
import hcmute.edu.vn.musicmediaplayer.Model.Song;

public class AlbumDetailsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ImageView albumPhoto;
    String albumId;
    TextView albumDescription,albumName;

    ArrayList<Song> albumSongs= new ArrayList<>();

    private StorageReference imageStorageRef;
    private StorageReference audioStorageRef;
    private ArrayList<Song> songRVModelArrayList;
    private DBHandler dbHandler;
    private DatabaseReference albumDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        albumDescription = findViewById(R.id.tv_album_description);
        albumPhoto = findViewById(R.id.img_album_photo);
        albumName =findViewById(R.id.tv_album_name);
        recyclerView = findViewById(R.id.rcv_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        dbHandler= new DBHandler(AlbumDetailsActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        songRVModelArrayList = new ArrayList<>();
        SongAdapter songAdapter = new SongAdapter(songRVModelArrayList, getApplicationContext());
        recyclerView.setAdapter(songAdapter);

        String check = getIntent().getStringExtra("localAlbum");
        System.out.println(check);


        if (check!=null)  {
            for (Song song :             dbHandler.readCourses()){
                songRVModelArrayList.add(song);
            }

            System.out.println(songRVModelArrayList.size());
            songAdapter.notifyDataSetChanged();
        }else {
            albumId = getIntent().getStringExtra("albumId");

//        listSong = new ArrayList<>();
            albumDatabaseRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("albums").child(albumId);


            albumDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Album object = snapshot.getValue(Album.class);

                    if (object != null && object.getSongs() != null && !object.getSongs().isEmpty())
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Song song = dataSnapshot.getValue(Song.class);

                            songRVModelArrayList.add(song);
                        }
                    songAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




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