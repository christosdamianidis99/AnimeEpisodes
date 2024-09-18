package com.example.animeepisodes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.util.ArrayList;

public class MyAnimeList_activity extends AppCompatActivity {



    ImageView searchMenuBtn;
    DatabaseHelper databaseHelper;
    ProgressBar progressBar;
    ListView animeListView;
    TextView emptyTextView;

    public animeListViewAdapter adapter;
    public ArrayList<Anime> myAnime;
    public static int MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myanime);
        initWidget();


        databaseHelper = new DatabaseHelper(getApplicationContext());
        MODE = 1;

        setListWithMyAnime();
        searchMenuBtn.setOnClickListener(new View.OnClickListener() {
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
        searchMenuBtn = findViewById(R.id.searchMenuBtn);
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
                adapter = new animeListViewAdapter(MyAnimeList_activity.this, myAnime, this);
                animeListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
                animeListView.setAdapter(adapter);
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

}