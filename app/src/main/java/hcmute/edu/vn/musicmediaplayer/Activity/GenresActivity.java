package hcmute.edu.vn.musicmediaplayer.Activity;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class GenresActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    RecyclerView recyclerViewIndieMusic;
    SearchAdapter indieMusicAdapter;
    ArrayList<Song> listSong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_genres);

        recyclerViewIndieMusic = findViewById(R.id.recycleview_indiemusic);
        myRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("indieMusic");
        recyclerViewIndieMusic.setHasFixedSize(true);
        recyclerViewIndieMusic.setLayoutManager(new LinearLayoutManager(this));
        listSong = new ArrayList<>();
        indieMusicAdapter = new SearchAdapter(this,listSong);
        recyclerViewIndieMusic.setAdapter(indieMusicAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Song song = dataSnapshot.getValue(Song.class);
                    listSong.add(song);
                }
                indieMusicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
