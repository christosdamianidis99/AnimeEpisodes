package com.example.animeepisodes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class activity_settings extends AppCompatActivity {

    private SeekBar volumeSeekBar;
    private ImageView backButton,muteButton;
    private AudioManager audioManager;
    private SharedPreferences sharedPreferences;
    private boolean isMuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initWigdets();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);

        // Load saved volume level
        int savedVolume = sharedPreferences.getInt("music_volume", 50);
        volumeSeekBar.setProgress(savedVolume);
        setVolume(savedVolume);

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMuteUnmute();
            }
        });
        // SeekBar listener
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setVolume(progress);
                // Save the volume level
                sharedPreferences.edit().putInt("music_volume", progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_settings.this,MyAnimeList_activity.class);
                startActivity(i);
            }
        });
    }

    private void initWigdets()
    {
        backButton = findViewById(R.id.backButton);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        muteButton=findViewById(R.id.muteButton);
    }

    private void setVolume(int progress) {
        float volume = progress / 100f;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volume * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
    }


    private void toggleMuteUnmute() {
        if (MediaPlayerManager.isInitialized()) {
            if (isMuted) {
                // Unmute
                MediaPlayerManager.setVolume(volumeSeekBar.getProgress() / 100f);
                muteButton.setImageResource(R.drawable.ic_mute);
            } else {
                // Mute
                MediaPlayerManager.setVolume(0); // Mute the volume

                muteButton.setImageResource(R.drawable.ic_unmute);
            }
            isMuted = !isMuted; // Toggle state
        }

    }


}
