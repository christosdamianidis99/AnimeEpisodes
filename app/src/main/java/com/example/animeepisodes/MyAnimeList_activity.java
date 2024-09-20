package com.example.animeepisodes;

import static com.example.animeepisodes.GlobalFormats.setUpListView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyAnimeList_activity extends AppCompatActivity {



    FloatingActionButton fabAddAnimeButton;
    DatabaseHelper databaseHelper;
    ProgressBar progressBar;
    ListView animeListView;
    TextView emptyTextView;
    ImageView settingsButton;

    public animeListViewAdapter adapter;
    public ArrayList<Anime> myAnime;
    public static int MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myanime);
        initWidget();
        MediaPlayerManager.initialize(this, R.raw.my_song); // Replace with your actual music file
        MediaPlayerManager.setVolume(0.5f); // Set initial volume
        MediaPlayerManager.play();

        // Load saved volume from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        int savedVolume = sharedPreferences.getInt("music_volume", 50);
        setVolume(savedVolume);



        databaseHelper = new DatabaseHelper(getApplicationContext());
        MODE = 1;

        setListWithMyAnime();
        fabAddAnimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(MyAnimeList_activity.this, SearchViewActivity.class);
                startActivity(i);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAnimeList_activity.this, activity_settings.class);
                startActivity(intent);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Create and show the AlertDialog
                new AlertDialog.Builder(MyAnimeList_activity.this)
                        .setTitle("Exit App")
                        .setMessage("Are you sure you want to exit the app?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "Yes", finish all activities and exit the app
                                finishAffinity(); // Finish all activities in the stack
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "No", dismiss the dialog
                                dialog.dismiss();
                            }
                        })
                        .show(); // Show the dialog
            }
        });
    }

    private void initWidget() {
        animeListView = findViewById(R.id.animListView);
        emptyTextView = findViewById(R.id.emptyTextView);
        progressBar = findViewById(R.id.progressBar);
        settingsButton = findViewById(R.id.settingsButton);
        animeListView.setEmptyView(emptyTextView);
        fabAddAnimeButton = findViewById(R.id.fabAddAnime);
    }

    public void setListWithMyAnime() {
        myAnime = new ArrayList<>();
        // Show ProgressBar while loading
        progressBar.setVisibility(View.VISIBLE);

        if (!databaseHelper.isTableEmpty("my_anime_episodes_db")) {
            Cursor cursor = databaseHelper.readAllAnim();
            cursor.moveToFirst();
            if (cursor.isFirst()) {
                do {
                    String arrayListString = cursor.getString(2);
                    ArrayList<String> arrayList = new ArrayList<>();
                    // Remove the brackets and split the string into an array of values
                    if (arrayListString == null) {
                        arrayList = null;
                    } else {
                        String withoutBrackets = arrayListString.substring(1, arrayListString.length() - 1);
                        String[] values = withoutBrackets.split(",\\s*");

// Create an ArrayList and add the values to it

                        for (String value : values) {
                            arrayList.add(value);
                        }

                    }


                    Anime anime = new Anime(
                            cursor.getInt(0),
                            cursor.getString(1),
                            arrayList,
                            cursor.getInt(3),
                            cursor.getString(5),
                            cursor.getString(4),
                            cursor.getString(6));

                    myAnime.add(anime);
                } while (cursor.moveToNext());


            }

            if (!myAnime.isEmpty()) {
                setUpListView(adapter,MyAnimeList_activity.this,myAnime,this,animeListView);

                progressBar.setVisibility(View.GONE);
            }


        } else {
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Hide the ProgressBar
                    progressBar.setVisibility(View.GONE);
                }
            }, 1500); // 2000 milliseconds = 2 seconds
        }


    }
    private void setVolume(int progress) {
        float volume = progress / 100f;
        MediaPlayerManager.setVolume(volume);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
    }
}