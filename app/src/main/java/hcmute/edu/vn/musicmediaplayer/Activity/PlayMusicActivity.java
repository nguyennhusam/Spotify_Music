package hcmute.edu.vn.musicmediaplayer.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;
import hcmute.edu.vn.musicmediaplayer.ServiceLocal.ForcegroundServiceControl;

public class PlayMusicActivity extends AppCompatActivity {
    private static ArrayList<Song> mangbaihat = new ArrayList<>();
    private int position = 0;
    private  boolean isPlaying;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                isPlaying = intent.getBooleanExtra("status_player", false);
                position = intent.getIntExtra("position_music", 0);
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        GetDataFromIntent();
        StartService();
    }
    private void StartService() {
        Intent intent =  new Intent(this, ForcegroundServiceControl.class);
        if (mangbaihat.size() > 0){
            intent.putExtra("obj_song_baihat", mangbaihat);
            System.out.println("size: "+mangbaihat.size());
        }
        startService(intent);
    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();
        if (intent != null){
            if (intent.hasExtra("play_music_from_search")){
                Song baiHat = intent.getParcelableExtra("play_music_from_search");
                System.out.println("testing: "+baiHat.getsName());
                mangbaihat.add(baiHat);
            }
        }
    }
    private void sendActionToService(int action){
        Intent intent = new Intent(this, ForcegroundServiceControl.class);
        intent.putExtra("action_music_service", action);
        startService(intent);
    }
}
