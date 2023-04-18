package hcmute.edu.vn.musicmediaplayer.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.List;

import hcmute.edu.vn.musicmediaplayer.Adapter.SearchAdapter;
import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;


public class SingerActivity extends AppCompatActivity {
//    private DatabaseReference myRef;
//
//    RecyclerView recyclerViewTrackMono;
//    SearchAdapter trackMonoAdapter;
//
//    ArrayList<Song> listSong;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_singer);


//        recyclerViewTrackMono = findViewById(R.id.recycleview_trackmono);
//        recyclerViewTrackMono.setHasFixedSize(true);
//        //thanh chắn ngăn cách giữa các item
//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
//        recyclerViewTrackMono.addItemDecoration(itemDecoration);
//
//        myRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/")
//                .getReference("trackMomo");
//        recyclerViewTrackMono.setLayoutManager(new LinearLayoutManager(context));
//        listSong = new ArrayList<>();
//        trackMonoAdapter = new SearchAdapter(context, listSong);
//        recyclerViewTrackMono.setAdapter(trackMonoAdapter);
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Song song = dataSnapshot.getValue(Song.class);
//                    listSong.add(song);
//                }
//                trackMonoAdapter.setFullList(new ArrayList<>(listSong));
//                trackMonoAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

}
