// MusicService.java
package com.example.animeepisodes;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.my_song); // Replace with your file name
        mediaPlayer.setLooping(true); // Loop the music
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean playMusic = intent.getBooleanExtra("playMusic", false);
        if (playMusic) {
            mediaPlayer.start();
        } else {
            mediaPlayer.pause();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
