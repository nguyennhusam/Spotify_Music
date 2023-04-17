package hcmute.edu.vn.musicmediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Model.Song;

public class AlbumDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView albumPhoto;
    String albumName;
    TextView albumDescription;
    ArrayList<Song> albumSongs= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView = findViewById(R.id.rcv_songs);
        albumName = getIntent().getStringExtra("albumName") ;
//        int j = 0;
//        for(int i = 0; i <   )
    }
}