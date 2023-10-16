package com.example.animeepisodes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBarMain;
    public static OkHttpClient okHttpClient;
    public static Retrofit retrofit;
    ListView animeListView;
    SearchView animeSearchView;
    animeListViewAdapter adapter;
    ArrayList<Anime> myAnime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        progressBarMain.setVisibility(View.VISIBLE);
    setOkHttpClient();
    setRetrofit();
        setAnimeList();


    }

    private void initWidget() {
        animeListView = findViewById(R.id.animListView);
        animeSearchView = findViewById(R.id.animeSearchView);
        progressBarMain = findViewById(R.id.progressBarMain);
    }

    public static void setOkHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(45, TimeUnit.SECONDS)
                .connectTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .addInterceptor(new ApiInterceptor("c5462cadcfmshfba151667d49f0bp11efbdjsn83afd80209c5","anime-db.p.rapidapi.com"))
                .build();
    }

    public static void setRetrofit() {
        String baseUrl = "https://anime-db.p.rapidapi.com/";

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public void setAnimeList()
    {
        AnimeBody animeBody = new AnimeBody("1","10");
        final animeApi myanimeApi = retrofit.create(animeApi.class);
        Call<AnimeResponse> createResponse = myanimeApi.createAnimeBody(animeBody);
        createResponse.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(Call<AnimeResponse> call, Response<AnimeResponse> response) {
                Object obj = response;
                myAnime = response.body().getData();

                adapter = new animeListViewAdapter(MainActivity.this,myAnime);
                animeListView.setAdapter(adapter);
                progressBarMain.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AnimeResponse> call, Throwable t) {

            }
        });
    }
}