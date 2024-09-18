package com.example.animeepisodes;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Anime> searchViewAnime = new ArrayList<>();
    public static Retrofit retrofit;
    DatabaseHelper databaseHelper;

    ProgressBar progressBar;
    TextView progressBarPercentageTV;
    interface RequestDataSearch {
        @Headers("X-RapidAPI-Key: " + "c5462cadcfmshfba151667d49f0bp11efbdjsn83afd80209c5")
        @GET("anime")
        Call<AnimeResponse> getData(@Query("page") String myPage, @Query("size") String mySize, @Query("sortBy") String sortBy);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(getApplicationContext());


        progressBar = findViewById(R.id.progressBar);
        progressBarPercentageTV = findViewById(R.id.progressPercentage);
        progressBar.setVisibility(View.VISIBLE);
        progressBarPercentageTV.setVisibility(View.VISIBLE);

        setRetrofit();
        // Register the onBackPressed callback
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Create and show the AlertDialog
                new AlertDialog.Builder(MainActivity.this)
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
    public void setRetrofit() {
        searchViewAnime = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        progressBarPercentageTV.setVisibility(View.VISIBLE);
        updateProgress(0); // Initialize progress bar to 0%

        if (!databaseHelper.isTableEmpty("search_anime_episodes_db")) {
            // Loading from DB
            Cursor cursor = databaseHelper.readAllSearch();
            cursor.moveToFirst();

            int count = cursor.getCount();
            double progressStep = count > 0 ? 100.0 / count : 0;

            new Thread(() -> {
                try {
                    int currentIndex = 0;
                    if (cursor.isFirst()) {
                        do {
                            // Process database entries
                            String arrayListString = cursor.getString(2);
                            String withoutBrackets = arrayListString.substring(1, arrayListString.length() - 1);
                            String[] values = withoutBrackets.split(",\\s*");

                            ArrayList<String> arrayList = new ArrayList<>();
                            for (String value : values) {
                                arrayList.add(value);
                            }

                            Anime anime = new Anime(
                                    cursor.getInt(0),
                                    cursor.getString(1),
                                    arrayList,
                                    cursor.getInt(4),
                                    cursor.getString(5),
                                    cursor.getString(3),
                                    Integer.parseInt(cursor.getString(6)));

                            searchViewAnime.add(anime);

                            // Update progress bar
                            if (progressStep > 0) {
                                int progress = (int) (progressStep * (currentIndex + 1));
                                runOnUiThread(() -> updateProgress(progress));
                            }

                            currentIndex++;
                        } while (cursor.moveToNext());
                    }
                } finally {
                    cursor.close();
                    // After DB loading is done, open MyAnimeList_activity
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        progressBarPercentageTV.setVisibility(View.GONE);
                        openMyAnimeListActivity();
                    });
                }
            }).start();

        } else {
            if (GlobalFormats.isInternetConnected(getApplicationContext()))
            {
                // Fetching from API
                String baseUrl = "https://anime-db.p.rapidapi.com/";
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RequestDataSearch requestDataSearch = retrofit.create(RequestDataSearch.class);
                requestDataSearch.getData("1", "2000", "ranking").enqueue(new Callback<AnimeResponse>() {
                    @Override
                    public void onResponse(Call<AnimeResponse> call, Response<AnimeResponse> response) {
                        if (response.isSuccessful()) {
                            searchViewAnime = response.body().getData();
                            double progressStep = searchViewAnime.size() > 0 ? 100.0 / searchViewAnime.size() : 0;

                            new Thread(() -> {
                                try {
                                    for (int i = 0; i < searchViewAnime.size(); i++) {
                                        databaseHelper.addSearchAnime(
                                                String.valueOf(searchViewAnime.get(i).get_id()),
                                                searchViewAnime.get(i).getTitle(),
                                                searchViewAnime.get(i).getGenres().toString(),
                                                String.valueOf(searchViewAnime.get(i).getEpisodes()),
                                                searchViewAnime.get(i).getSynopsis(),
                                                searchViewAnime.get(i).getImage(),
                                                String.valueOf(searchViewAnime.get(i).getRanking()));

                                        // Update progress bar
                                        if (progressStep > 0) {
                                            int progress = (int) (progressStep * (i + 1));
                                            runOnUiThread(() -> updateProgress(progress));
                                        }
                                    }
                                } finally {
                                    // After API loading is done, open MyAnimeList_activity
                                    runOnUiThread(() -> {
                                        progressBar.setVisibility(View.GONE);
                                        progressBarPercentageTV.setVisibility(View.GONE);
                                        openMyAnimeListActivity();
                                    });
                                }
                            }).start();

                        } else {
                            Toast.makeText(MainActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            progressBarPercentageTV.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AnimeResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        progressBarPercentageTV.setVisibility(View.GONE);
                    }
                });
            }else
            {
                Toast.makeText(this, "No internet connection download data..", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,MyAnimeList_activity.class);
                startActivity(i);
            }

        }
    }

    private void updateProgress(int progress) {
        progressBar.setProgress(progress);
        progressBarPercentageTV.setText("Loading: " + progress + "%");
    }

    private void openMyAnimeListActivity() {
        Intent intent = new Intent(MainActivity.this, MyAnimeList_activity.class);
        startActivity(intent);
    }





    public static void refresh(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);


    }


}
