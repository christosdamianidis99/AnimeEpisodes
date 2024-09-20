package com.example.animeepisodes;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaPlayerManager {
    private static MediaPlayer mediaPlayer;
    private static boolean isInitialized = false;

    // Initializes the MediaPlayer with the provided audio resource
    public static void initialize(Context context, int resourceId) {
        if (!isInitialized) {
            mediaPlayer = MediaPlayer.create(context, resourceId);
            mediaPlayer.setLooping(true); // Set to loop if needed
            isInitialized = true;
        }
    }

    // Plays the music
    public static void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // Pauses the music
    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    // Releases the MediaPlayer resources
    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isInitialized = false; // Reset initialization state
        }
    }

    // Sets the volume of the MediaPlayer
    public static void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    // Checks if MediaPlayer is initialized
    public static boolean isInitialized() {
        return isInitialized;
    }
}
