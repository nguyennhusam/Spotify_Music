package hcmute.edu.vn.musicmediaplayer.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Model.DBHandler;
import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;
import hcmute.edu.vn.musicmediaplayer.ServiceLocal.ForcegroundServiceControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayMusicActivity extends AppCompatActivity {
    private static ArrayList<Song> mangbaihat = new ArrayList<>();
    private int position = 0;
    private ImageButton btnDownload;
    FirebaseStorage audioStorageRef ;
    StorageReference imageStorageRef;
    private DBHandler dbHandler;


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
        dbHandler = new DBHandler(PlayMusicActivity.this);


        btnDownload = findViewById(R.id.imageButtonDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println(mangbaihat.get(0).getsImageUrl());
                    downloadFile(mangbaihat.get(0).getsSongUrl(),mangbaihat.get(0).getsName(),mangbaihat.get(0).getSongId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
    private void downloadFile(String fileUrl, String fileName,String songId) throws IOException {
        // Tạo một đối tượng OkHttpClient để tải xuống tệp
        StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);
        String fileN = fileName+".mp3";
        String folder = songId+"";
////        File SDCardRoot = Environment.getExternalStorageDirectory();
////
////        File audioFolder = new File(String.valueOf(SDCardRoot), folder);
//
////        StorageReference islandRef = audioStorageRef.child("images/island.jpg");
//        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + songId + "/" + fileName;
//        File audioFolder = new File(filePath);
////        File folderDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath()
////                , "Myfolder");
////        File audioFolder = new File(folder);
//        if (!audioFolder.exists()) {
////            Files.createFile(audioFolder.toPath());
//
//            boolean success = audioFolder.getParentFile().mkdirs();
//            if (success) {
//            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
//
//                // save the file
//            }
//        }

//        File musicFile = new File(audioFolder, fileN);

        File musicDirectory = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_MUSIC)),songId+".mp3");

        System.out.println(musicDirectory);
//        File localFile = File.createTempFile("images", "jpg");

        httpsReference.getFile(musicDirectory).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PlayMusicActivity.this, "Download Success", Toast.LENGTH_SHORT).show();
                Song song = mangbaihat.get(0);
                String name = song.getsName();
                String songUrl = String.valueOf(musicDirectory);
                String imageUrl = song.getsImageUrl();
                String id = song.getSongId();
                String artist = song.getsArtist();

                // validating if the text fields are empty or not.
                if (name.isEmpty() || songUrl.isEmpty() || imageUrl.isEmpty() || id.isEmpty()||artist.isEmpty()||id.isEmpty()) {
                    Toast.makeText(PlayMusicActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                // on below line we are calling a method to add new
                // course to sqlite data and pass all our values to it.
                dbHandler.addNewCourse(id,name , songUrl, imageUrl,artist);

                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
}






