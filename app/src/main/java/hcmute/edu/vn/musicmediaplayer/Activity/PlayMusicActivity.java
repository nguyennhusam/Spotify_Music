package hcmute.edu.vn.musicmediaplayer.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import hcmute.edu.vn.musicmediaplayer.Adapter.ViewPagerDiaNhac;
import hcmute.edu.vn.musicmediaplayer.Fragment.Fragment_dia_nhac;
import hcmute.edu.vn.musicmediaplayer.Model.DBHandler;
import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;
import hcmute.edu.vn.musicmediaplayer.ServiceLocal.ForcegroundServiceControl;

public class PlayMusicActivity extends AppCompatActivity {
    private static ArrayList<Song> mangbaihat = new ArrayList<>();
    private androidx.appcompat.widget.Toolbar toolbarplaynhac;
    private SeekBar seekBarnhac;
    private boolean isplaying,repeat = false;
    private TextView textViewtennhac, textViewcasi, textViewrunrime, textViewtatoltime;
    private ImageButton imageButtontronnhac, imageButtonpreviewnhac, imageButtonplaypausenhac, imageButtonnextnhac, imageButtonDowmload,
            imageButtonlapnhac;
    private int dem = 0, position = 0,position_click = 0, duration = 0, timeValue = 0, durationToService = 0;
    private Fragment_dia_nhac fragment_dia_nhac;
    public static ViewPagerDiaNhac adapternhac;

    StorageReference audioReference, imageReference;

    DBHandler dbHandler;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                isplaying = intent.getBooleanExtra("status_player", false);
                int action = intent.getIntExtra("action_music", 0);
                duration = intent.getIntExtra("duration_music", 0);
                timeValue = intent.getIntExtra("seektomusic", 0);
                position = intent.getIntExtra("position_music", 0);
                seekBarnhac.setProgress(timeValue);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                textViewrunrime.setText(simpleDateFormat.format(timeValue));
                handleMusic(action);
                TimeSong();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("send_data_to_activity"));
        GetDataFromIntent();
        AnhXa();
        enventClick();
        setViewStart();
        StartService();
        //overridePendingTransition(R.anim.anim_intent_in, R.anim.anim_intent_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void StartService() {
        Intent intent = new Intent(this, ForcegroundServiceControl.class);
        if (mangbaihat.size() > 0) {
            intent.putExtra("obj_song_baihat", mangbaihat);
            System.out.println("send position click: "+position);
            intent.putExtra("click_position",position);
        }
        startService(intent);
    }

    private void handleMusic(int action) {
        switch (action) {
            case ForcegroundServiceControl.ACTION_PAUSE:
                imageButtonplaypausenhac.setImageResource(R.drawable.baseline_play_circle_24);
                break;
            case ForcegroundServiceControl.ACTION_RESUME:
                imageButtonplaypausenhac.setImageResource(R.drawable.baseline_pause_circle_24);
                break;
            case ForcegroundServiceControl.ACTION_NEXT:
                completeNextMusic();
                break;
            case ForcegroundServiceControl.ACTION_PREVIOUS:
                completePreviousMusic();
                break;
        }
    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();
        if (intent != null) {
            if (intent.hasExtra("play_music_from_search")) {
                Song baiHat = intent.getParcelableExtra("play_music_from_search");
                mangbaihat.add(baiHat);
            }else if (intent.hasExtra("list_play_music_from_search")){
                mangbaihat = intent.getParcelableArrayListExtra("list_play_music_from_search");
                position = intent.getIntExtra("click_position",0);
            }
        }
    }

    private void AnhXa() {
        toolbarplaynhac = findViewById(R.id.toolbarplaynhac);
        seekBarnhac = findViewById(R.id.seekBartime);
        ViewPager viewPagerplaynhac = findViewById(R.id.viewPagerdianhac);

        imageButtonplaypausenhac = findViewById(R.id.imageButtonplaypause);
        imageButtonlapnhac = findViewById(R.id.imageButtonlap);
        imageButtonpreviewnhac = findViewById(R.id.imageButtonpreview);
        imageButtonnextnhac = findViewById(R.id.imageButtonnext);
        imageButtonDowmload = findViewById(R.id.imageButtonDownload);

        textViewtatoltime = findViewById(R.id.textViewtimetotal);
        textViewcasi = findViewById(R.id.textViewtencasiplaynhac);
        textViewtennhac = findViewById(R.id.textViewtenbaihatplaynhac);
        textViewrunrime = findViewById(R.id.textViewruntime);



        fragment_dia_nhac = new Fragment_dia_nhac();
        adapternhac = new ViewPagerDiaNhac(getSupportFragmentManager());
        adapternhac.AddFragment(fragment_dia_nhac);

        viewPagerplaynhac.setAdapter(adapternhac);
        setSupportActionBar(toolbarplaynhac);
        toolbarplaynhac.setTitleTextColor(Color.BLACK);

        fragment_dia_nhac = (Fragment_dia_nhac) adapternhac.getItem(0);

        dbHandler = new DBHandler(PlayMusicActivity.this);
    }

    private void setViewStart() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mangbaihat.size() > 0) {
                    setView(mangbaihat.get(position));
                }
            }
        }, 500);
    }

    private void setView(Song song) {
        setGradient(song.getsImageUrl());
        fragment_dia_nhac.PlayNhac(song.getsImageUrl());

        textViewcasi.setText(song.getsArtist());
        textViewtennhac.setText(song.getsName());
    }

    private void setGradient(String urlImage) {
        Picasso.get().load(urlImage)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(palette -> {
                            assert palette != null;
                            Palette.Swatch swatch = palette.getDominantSwatch();
                            RelativeLayout mContaier = findViewById(R.id.mContainer);
                            mContaier.setBackgroundResource(R.drawable.bgr_playnhac);
                            assert swatch != null;
                            GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                    new int[]{swatch.getRgb(), swatch.getRgb()});
                            mContaier.setBackground(gradientDrawableBg);
                        });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
    }

    private void enventClick() {

        imageButtonDowmload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song song = mangbaihat.get(position);
                System.out.println(song.getsName());
                if (dbHandler.readSong(song.getSongId()).size()!=0){
                    Toast.makeText(PlayMusicActivity.this, "Bài nhạc đã có sẵn", Toast.LENGTH_SHORT).show();


                }
                else {
                    try {
                        downloadFile(song);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        imageButtonplaypausenhac.setOnClickListener(view -> {
            if (isplaying) {
                sendActionToService(ForcegroundServiceControl.ACTION_PAUSE);
                imageButtonplaypausenhac.setImageResource(R.drawable.baseline_play_circle_24);
            } else {
                sendActionToService(ForcegroundServiceControl.ACTION_RESUME);
                imageButtonplaypausenhac.setImageResource(R.drawable.baseline_pause_circle_24);
            }
        });
        imageButtonlapnhac.setOnClickListener(this::onClickRepeat);
        seekBarnhac.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                durationToService = seekBar.getProgress();
                sendActionToService(ForcegroundServiceControl.ACTION_DURATION);
            }
        });
        imageButtonnextnhac.setOnClickListener(view -> sendActionToService(ForcegroundServiceControl.ACTION_NEXT));
        imageButtonpreviewnhac.setOnClickListener(view -> sendActionToService(ForcegroundServiceControl.ACTION_PREVIOUS));
        toolbarplaynhac.setNavigationOnClickListener(view -> {
            mangbaihat.clear();
            finish();
        });
    }
    private void onClickRepeat(View view) {
        if (!repeat) {

                imageButtonlapnhac.setImageResource(R.drawable.baseline_repeat_on_24);

            repeat = true;
        } else {
            imageButtonlapnhac.setImageResource(R.drawable.baseline_repeat_24);
            repeat = false;
        }
        sendActionToService(ForcegroundServiceControl.ACTION_REPEAT);
    }
    private void NextMusic(){
        imageButtonplaypausenhac.setImageResource(R.drawable.baseline_play_circle_24);
        timeValue = 0;
    }
    private void completeNextMusic() {
        if (mangbaihat.size() > 0){
            NextMusic();
            setView(mangbaihat.get(position));
        }
    }
    private void PreviousMusic(){
        imageButtonplaypausenhac.setImageResource(R.drawable.baseline_play_circle_24);
        timeValue = 0;
    }
    private void completePreviousMusic() {
        if (mangbaihat.size() > 0){
            PreviousMusic();
            setView(mangbaihat.get(position));
        }
    }
    private void TimeSong() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        textViewtatoltime.setText(simpleDateFormat.format(duration));
        seekBarnhac.setMax(duration);
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(this, ForcegroundServiceControl.class);
        intent.putExtra("action_music_service", action);
        intent.putExtra("repeat_music", repeat);
        intent.putExtra("duration", durationToService);
        startService(intent);
    }
    private void downloadFile(Song song) throws IOException {
        // Tạo một đối tượng OkHttpClient để tải xuống tệp
        audioReference = FirebaseStorage.getInstance().getReferenceFromUrl(song.getsSongUrl());
        imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(song.getsImageUrl());


        File musicDirectory = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_MUSIC)), song.getSongId() + ".mp3");

        System.out.println(musicDirectory);
//        File localFile = File.createTempFile("images", "jpg");
        File imageDirectory = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)),song.getSongId()+".jpg");


        audioReference.getFile(musicDirectory).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PlayMusicActivity.this, "Download Audo Success", Toast.LENGTH_SHORT).show();
                Song song = mangbaihat.get(position);
                String name = song.getsName();
                String songUrl = String.valueOf(musicDirectory);
                String imageUrl = String.valueOf(imageDirectory);
                String id = song.getSongId();
                String artist = song.getsArtist();

                // validating if the text fields are empty or not.
                if (name.isEmpty() || songUrl.isEmpty() || imageUrl.isEmpty() || id.isEmpty() || artist.isEmpty() || id.isEmpty()) {
                    Toast.makeText(PlayMusicActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                // on below line we are calling a method to add new
                // course to sqlite data and pass all our values to it.
                dbHandler.addNewCourse(id, name, songUrl, imageUrl, artist);

                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



        System.out.println(musicDirectory);
//        File localFile = File.createTempFile("images", "jpg");

        imageReference.getFile(imageDirectory).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PlayMusicActivity.this, "Download Image Success", Toast.LENGTH_SHORT).show();
//                Song song = mangbaihat.get(position);
//                String name = song.getsName();
//                System.out.println("Test id "+ song.getSongId());
//                String songUrl = String.valueOf(musicDirectory);
//                String imageUrl = String.valueOf(imageDirectory);
//                String id = song.getSongId();
//                String artist = song.getsArtist();
//
//                // validating if the text fields are empty or not.
////                if (name.isEmpty() || songUrl.isEmpty() || imageUrl.isEmpty() || id.isEmpty()||artist.isEmpty()||id.isEmpty()) {
////                    return;
////                }
//
//                // on below line we are calling a method to add new
//                // course to sqlite data and pass all our values to it.
//                dbHandler.updateSong(id,name , songUrl, imageUrl,artist);

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
