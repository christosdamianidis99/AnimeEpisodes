package com.example.animeepisodes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
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
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBarMain;
    public static OkHttpClient okHttpClient;
    public static Retrofit retrofit;
    ListView animeListView;
    SearchView animeSearchView;
    public  animeListViewAdapter adapter;
    public  ArrayList<Anime> myAnime;

    interface RequestData{
        @Headers("X-RapidAPI-Key: " + "c5462cadcfmshfba151667d49f0bp11efbdjsn83afd80209c5")
        @GET("anime")
        Call<AnimeResponse> getData(@Query("page") String myPage, @Query("size") String mySize);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        progressBarMain.setVisibility(View.VISIBLE);
//    setOkHttpClient();
    setRetrofit();



//        setAnimeList();


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
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .url("https://anime-db.p.rapidapi.com/anime")
                                .header("X-RapidAPI-Key", "c5462cadcfmshfba151667d49f0bp11efbdjsn83afd80209c5")
                                .header("X-RapidAPI-Host", "anime-db.p.rapidapi.com")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();

        //                .addInterceptor(new ApiInterceptor("c5462cadcfmshfba151667d49f0bp11efbdjsn83afd80209c5","anime-db.p.rapidapi.com"))

    }

    public  void setRetrofit() {
        String baseUrl = "https://anime-db.p.rapidapi.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)

                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestData requestData = retrofit.create(RequestData.class);
        requestData.getData("1","10").enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(Call<AnimeResponse> call, Response<AnimeResponse> response) {
                if (response.isSuccessful())
                {
                    System.out.println(response.body().toString());
                    myAnime=response.body().getData();
                    adapter=new animeListViewAdapter(MainActivity.this,myAnime);
                    animeListView.setAdapter(adapter);
                    progressBarMain.setVisibility(View.GONE);
                }else
                {
                    System.out.println(response.errorBody());
                    Toast.makeText(MainActivity.this, "SYSTEM ERROR", Toast.LENGTH_LONG).show();
                    progressBarMain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AnimeResponse> call, Throwable t) {

            }
        });
    }

//    public void setAnimeList()
//    {
//        final animeApi myanimeApi = retrofit.create(animeApi.class);
//
//        // Specify the correct values for "page" and "size"
//        String page = "1";
//        String size = "10";
//
//        Call<AnimeResponse> createResponse = myanimeApi.createAnimeBody(page, size);
//
//
//
//        createResponse.enqueue(new Callback<AnimeResponse>() {
//            @Override
//            public void onResponse(Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
//                if (response.isSuccessful()) {
//                    // Handle the successful response here
//                    AnimeResponse animeResponse = response.body();
//                    System.out.println(animeResponse.toString());
//                    // Process the data as needed
//                } else {
//                    try {
//                        System.out.println(response.errorBody().string());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    System.out.println(response.errorBody().toString());
//                    System.out.println(response.errorBody().byteStream());
//                    System.out.println(response.errorBody().charStream());
//                    System.out.println(response.errorBody().source());
//
//                    System.out.println(response.body().toString());
//                }
////                System.out.println(response.errorBody());
//
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
//                Log.e("Request Failure", t.toString());
//            }
//        });
//    }
}