package com.example.animeepisodes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    ListView animeListView;

    public animeListViewAdapter adapter;
    public ArrayList<Anime> myAnime;
    public static int MODE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        MODE = 1;
        initWidget();
//    setOkHttpClient();
        setRetrofit();
        searchMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
//
//                if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
//                    // There is an internet connection, so start the activity
//                    Intent i = new Intent(MainActivity.this, SearchViewActivity.class);
//                    startActivity(i);
//                } else {
//                    // No internet connection, show a Toast message
//                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
//                }

                Intent i = new Intent(MainActivity.this, SearchViewActivity.class);
                startActivity(i);
            }
        });


    }

    private void initWidget() {
        animeListView = findViewById(R.id.animListView);
        searchMenuBtn = findViewById(R.id.searchMenuBtn);
    }


    public void setRetrofit() {
        myAnime = new ArrayList<>();
        if (!databaseHelper.isTableEmpty("my_anime_episodes_db")) {
            Cursor cursor = databaseHelper.readAllAnim();
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
                            cursor.getInt(3),
                            cursor.getString(5),
                            cursor.getString(4),
                            cursor.getInt(6),
                            cursor.getInt(7));

                    myAnime.add(anime);
                } while (cursor.moveToNext());



            }

            if (!myAnime.isEmpty()) {
                adapter = new animeListViewAdapter(MainActivity.this, myAnime,this);
                animeListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
                animeListView.setAdapter(adapter);
            }

        }
    }
    public static void refresh(Activity activity)
    {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);


    }
}