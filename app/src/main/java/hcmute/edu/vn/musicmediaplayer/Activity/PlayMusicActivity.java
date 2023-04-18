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
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import hcmute.edu.vn.musicmediaplayer.Adapter.ViewPagerDiaNhac;
import hcmute.edu.vn.musicmediaplayer.Fragment.Fragment_dia_nhac;
import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;
import hcmute.edu.vn.musicmediaplayer.ServiceLocal.ForcegroundServiceControl;

public class PlayMusicActivity extends AppCompatActivity {
    private static ArrayList<Song> mangbaihat = new ArrayList<>();
    private androidx.appcompat.widget.Toolbar toolbarplaynhac;
    private SeekBar seekBarnhac;
    private boolean isplaying;
    private TextView textViewtennhac, textViewcasi, textViewrunrime, textViewtatoltime;
    private ImageButton imageButtontronnhac, imageButtonpreviewnhac, imageButtonplaypausenhac, imageButtonnexnhac,
            imageButtonlapnhac;
    private int dem = 0, position = 0, duration = 0, timeValue = 0, durationToService = 0;
    private Fragment_dia_nhac fragment_dia_nhac;
    public static ViewPagerDiaNhac adapternhac;
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
//            case ForcegroundServiceControl.ACTION_NEXT:
//                completeNextMusic();
//                break;
//            case ForcegroundServiceControl.ACTION_PREVIOUS:
//                completePreviousMusic();
//                break;
        }
    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();
        if (intent != null) {
            if (intent.hasExtra("play_music_from_search")) {
                Song baiHat = intent.getParcelableExtra("play_music_from_search");
                mangbaihat.add(baiHat);
            }
        }
    }

    private void AnhXa() {
        toolbarplaynhac = findViewById(R.id.toolbarplaynhac);
        seekBarnhac = findViewById(R.id.seekBartime);
        ViewPager viewPagerplaynhac = findViewById(R.id.viewPagerdianhac);

        imageButtonplaypausenhac = findViewById(R.id.imageButtonplaypause);

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

        fragment_dia_nhac = (Fragment_dia_nhac) adapternhac.getItem(position);
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

        imageButtonplaypausenhac.setOnClickListener(view -> {
            if (isplaying) {
                sendActionToService(ForcegroundServiceControl.ACTION_PAUSE);
                imageButtonplaypausenhac.setImageResource(R.drawable.baseline_play_circle_24);
            } else {
                sendActionToService(ForcegroundServiceControl.ACTION_RESUME);
                imageButtonplaypausenhac.setImageResource(R.drawable.baseline_pause_circle_24);
            }
        });

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

        toolbarplaynhac.setNavigationOnClickListener(view -> {
            mangbaihat.clear();
            finish();
        });
    }

    private void TimeSong() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        textViewtatoltime.setText(simpleDateFormat.format(duration));
        seekBarnhac.setMax(duration);
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(this, ForcegroundServiceControl.class);
        intent.putExtra("action_music_service", action);
        intent.putExtra("duration", durationToService);
        startService(intent);
    }
}
