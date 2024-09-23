package com.example.animeepisodes;

import static com.example.animeepisodes.GlobalFormats.setUpListView;
import static com.example.animeepisodes.MainActivity.savedAnime;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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


    public animeListViewAdapter adapter;
    public static int MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myanime);
        initWidget();


        databaseHelper = new DatabaseHelper(getApplicationContext());
        savedAnime = databaseHelper.readAllAnimeFromDB();

        MODE = 1;

        setListWithMyAnime();
        fabAddAnimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(MyAnimeList_activity.this, SearchViewActivity.class);
                startActivity(i);
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
        animeListView.setEmptyView(emptyTextView);
        fabAddAnimeButton = findViewById(R.id.fabAddAnime);
    }

    public void setListWithMyAnime() {
        // Show ProgressBar while loading
        progressBar.setVisibility(View.VISIBLE);


            if (!savedAnime.isEmpty()) {

                adapter = new animeListViewAdapter(this, savedAnime, this);
                animeListView.setAdapter(adapter);
                animeListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
                animeListView.setDividerHeight(16);

                if (!(MyAnimeList_activity.MODE == 1))
                {
                    Animation cardAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);
                    LayoutAnimationController controller = new LayoutAnimationController(cardAnimation);
                    controller.setDelay(0.2f);
                    animeListView.setLayoutAnimation(controller);
                    animeListView.scheduleLayoutAnimation();
                }
                //setUpListView(adapter,MyAnimeList_activity.this,savedAnime,this,animeListView);

                progressBar.setVisibility(View.GONE);
            }


         else {
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



}