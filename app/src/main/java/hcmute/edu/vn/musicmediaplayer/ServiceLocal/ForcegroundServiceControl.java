package hcmute.edu.vn.musicmediaplayer.ServiceLocal;

import static hcmute.edu.vn.musicmediaplayer.ServiceLocal.ChannelNotification.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;

public class ForcegroundServiceControl extends Service {
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_CLEAR = 3;
    public static final int ACTION_DURATION = 4;
    private boolean isPlaying;
    private MediaPlayer mediaPlayer;

    private ArrayList<Song> mangbaihat = new ArrayList<>();

    private int positionPlayer = 0, duration = 0, seekToTime = 0, curentime = 0;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra("obj_song_baihat")) {
                clearArray();
                mangbaihat = intent.getParcelableArrayListExtra("obj_song_baihat");
            }

        }
        if (!intent.hasExtra("action_music_service")){
            CompleteAndStart();
        }
        int actionMusic = intent.getIntExtra("action_music_service", 0);
        seekToTime = intent.getIntExtra("duration", 0);
        //System.out.println("intent null");
        handleActionMusic(actionMusic);
        return START_NOT_STICKY;
    }

    private void clearArray() {
        positionPlayer = 0;
        mangbaihat.clear();
    }

    private void startMusic(String linkBaiHat) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        new playMP3().onPostExecute(linkBaiHat);
        isPlaying = true;
        duration = mediaPlayer.getDuration();
        sendActonToPlayNhacActivity(ACTION_RESUME);
        sendTimeCurrent();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    private void CompleteAndStart() {
        if (mangbaihat != null) {
            startMusic(mangbaihat.get(positionPlayer).getsSongUrl());
            sendNotification(mangbaihat.get(positionPlayer));
        }
    }
    private void handleActionMusic(int action){
        switch (action){
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();
                sendActonToPlayNhacActivity(ACTION_CLEAR);
                break;
            case ACTION_DURATION:
                mediaPlayer.seekTo(seekToTime);
                break;
        }
    }

    private void resumeMusic() {
        if(mediaPlayer != null && !isPlaying){
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(mangbaihat.get(positionPlayer));
            sendActonToPlayNhacActivity(ACTION_RESUME);
        }
    }

    private void pauseMusic() {
        if(mediaPlayer != null && isPlaying){
            mediaPlayer.pause();
            isPlaying = false;
            sendNotification(mangbaihat.get(positionPlayer));
            sendActonToPlayNhacActivity(ACTION_PAUSE);
        }
    }


    private void sendNotification(Song song) {
        Intent intent = new Intent(this, ForcegroundServiceControl.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_playmusic);
        remoteViews.setTextViewText(R.id.tv_title_song, song.getsName());
        remoteViews.setTextViewText(R.id.tv_single_song, song.getsArtist());
        Picasso.get().load(song.getsImageUrl())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        remoteViews.setImageViewBitmap(R.id.img_song,bitmap);
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });

        remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.baseline_pause_circle_24);

        if(isPlaying){
            Log.e("TranDucLong", "isPlaying");
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.baseline_pause_circle_24);
        }else{
            Log.e("TranDucLong", "notIsPlaying");
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.baseline_play_circle_24);
        }
        remoteViews.setOnClickPendingIntent(R.id.img_clear, getPendingIntent(this,ACTION_CLEAR));

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.spotify_logo)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();

        startForeground(1, notification);
    }
    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(this, BroadcastReceiverAction.class);
        intent.putExtra("action_music", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void sendActonToPlayNhacActivity(int action) {
        if (mangbaihat != null) {
            Intent intent = new Intent("send_data_to_activity");
            intent.putExtra("status_player", isPlaying);
            intent.putExtra("action_music", action);
            intent.putExtra("position_music", positionPlayer);
            intent.putExtra("duration_music", duration);
            intent.putExtra("seektomusic", curentime);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
    private void sendTimeCurrent(){
        if (mediaPlayer != null){
            curentime = mediaPlayer.getCurrentPosition();
            sendActonToPlayNhacActivity(ACTION_DURATION);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        sendTimeCurrent();
                    }
                }
            }, 1000);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class playMP3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        CompleteAndStart();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mediaPlayer.setDataSource(baihat);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();

        }
    }
}

