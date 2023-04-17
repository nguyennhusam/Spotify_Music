package hcmute.edu.vn.musicmediaplayer.Activity;

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

public class PlayMusicActivity extends AppCompatActivity {
    private static ArrayList<Song> mangbaihat = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);


//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
//                new IntentFilter("send_data_to_activity"));
        GetDataFromIntent();
//        AnhXa();
//        enventClick();
//        setViewStart();
//        StartService();
    }
//    private void setGradient(String urlImage){
//        Picasso.get().load(urlImage)
//                .into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        Palette.from(bitmap).generate(palette -> {
//                            assert palette != null;
//                            Palette.Swatch swatch = palette.getDominantSwatch();
//                            RelativeLayout mContaier = findViewById(R.id.mContainer);
//                            mContaier.setBackgroundResource(R.drawable.bgr_playnhac);
//                            assert swatch != null;
//                            GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
//                                    new int[]{swatch.getRgb(), swatch.getRgb()});
//                            mContaier.setBackground(gradientDrawableBg);
//                        });
//                    }
//                    @Override
//                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//                    }
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//                    }
//                });
//    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();

        if (intent != null){
            if (intent.hasExtra("play_music_from_search")){
                Song baiHat = intent.getParcelableExtra("play_music_from_search");
                mangbaihat.add(baiHat);
            }
        }
    }
}
