package hcmute.edu.vn.musicmediaplayer.ServiceLocal;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Model.Song;

public class ForcegroundServiceControl extends Service {
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_NEXT = 3;
    private boolean isPlaying;
    private MediaPlayer mediaPlayer;

    private ArrayList<Song> mangbaihat = new ArrayList<>();
    private int positionPlayer = 0;


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
            System.out.println("intent #null");
            if (intent.hasExtra("obj_song_baihat")) {
                clearArray();
                mangbaihat = intent.getParcelableArrayListExtra("obj_song_baihat");
            }
            System.out.println("intent #null url: "+mangbaihat.get(positionPlayer).getsSongUrl());
            System.out.println("intent #null image: "+mangbaihat.get(positionPlayer).getsImageUrl());
            startMusic(mangbaihat.get(positionPlayer).getsSongUrl());
        }
        //System.out.println("intent null");
        return START_NOT_STICKY;
    }

    private void clearArray() {
        positionPlayer = 0;
        mangbaihat.clear();
    }

    private void startMusic(String linkBaiHat) {
        if (mediaPlayer != null) {
            System.out.println("media null");
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Uri uri = Uri.parse(linkBaiHat);
        System.out.println("uri: " + uri);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        System.out.println("uri: " + uri);
        System.out.println("link: " + linkBaiHat);
        mediaPlayer.start();
//        new playMP3().onPostExecute(linkBaiHat);
        isPlaying = true;

    }

    private void CompleteAndStart() {
        if (mangbaihat != null) {
            startMusic(mangbaihat.get(positionPlayer).getsSongUrl());
        }
    }

    private void sendActonToPlayNhacActivity(int action) {
        if (mangbaihat != null) {
            Intent intent = new Intent("send_data_to_activity");
            intent.putExtra("status_player", isPlaying);
            intent.putExtra("action_music", action);
            intent.putExtra("position_music", positionPlayer);

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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

