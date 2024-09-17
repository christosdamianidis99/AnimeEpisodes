package com.example.animeepisodes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    //    ProgressBar progressBarMain;
    ImageView searchMenuBtn;
    DatabaseHelper databaseHelper;
    ProgressBar progressBar;
    ListView animeListView;
    TextView emptyTextView;
    public static ArrayList<Anime> searchViewAnime = new ArrayList<>();

    public animeListViewAdapter adapter;
    public ArrayList<Anime> myAnime;
    public static int MODE;

    public static Retrofit retrofit;

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
        MODE = 1;
        initWidget();
        setListWithMyAnime();
        setRetrofit();
        searchMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(MainActivity.this, SearchViewActivity.class);
                startActivity(i);
            }
        });


    }

    private void initWidget() {
        animeListView = findViewById(R.id.animListView);
        emptyTextView = findViewById(R.id.emptyTextView);
        progressBar = findViewById(R.id.progressBar);

        animeListView.setEmptyView(emptyTextView);
        searchMenuBtn = findViewById(R.id.searchMenuBtn);
        searchMenuBtn.setVisibility(View.GONE);
    }

    public void setRetrofit() {
        searchViewAnime = new ArrayList<>();
        if (!databaseHelper.isTableEmpty("search_anime_episodes_db")) {

            Cursor cursor = databaseHelper.readAllSearch();
            cursor.moveToFirst();
            if (cursor.isFirst()) {
                do {
                    String arrayListString = cursor.getString(2);
                    // Remove the brackets and split the string into an array of values
                    String withoutBrackets = arrayListString.substring(1, arrayListString.length() - 1);
                    String[] values = withoutBrackets.split(",\\s*");

// Create an ArrayList and add the values to it
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
                } while (cursor.moveToNext());


            }
            searchMenuBtn.setVisibility(View.VISIBLE);


        } else {
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

                        for (int i = 0; i < searchViewAnime.size(); i++) {
                            databaseHelper.addSearchAnime(String.valueOf(searchViewAnime.get(i).get_id()),
                                    searchViewAnime.get(i).getTitle(),
                                    searchViewAnime.get(i).getGenres().toString(),
                                    String.valueOf(searchViewAnime.get(i).getEpisodes()),
                                    searchViewAnime.get(i).getSynopsis(),
                                    searchViewAnime.get(i).getImage(),
                                    String.valueOf(searchViewAnime.get(i).getRanking()));
                        }
                        searchMenuBtn.setVisibility(View.VISIBLE);


                    } else {
                        Toast.makeText(MainActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AnimeResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();

                }
            });
        }
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
                adapter = new animeListViewAdapter(MainActivity.this, myAnime, this);
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

    public static void refresh(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);


    }

    @Override
    public void onBackPressed() {
        // Create an AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Yes", exit the app
                        finish(); // Close the current activity
                        System.exit(0); // Optional: Exit the app
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
}
