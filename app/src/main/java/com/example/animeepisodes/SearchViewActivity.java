package com.example.animeepisodes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class SearchViewActivity extends AppCompatActivity {
    SearchView animeSearchView;
    public static Retrofit retrofit;
    public ArrayList<Anime> searchViewAnime;
    ListView animListViewSearchActivity;
    private static String newTextSearchBar;
    public  animeListViewAdapter adapter;
    ProgressBar progressBarSearchView;
    interface RequestDataSearch
    {
        @Headers("X-RapidAPI-Key: " + "c5462cadcfmshfba151667d49f0bp11efbdjsn83afd80209c5")
        @GET("anime")
        Call<AnimeResponse> getData(@Query("page") String myPage, @Query("size") String mySize, @Query("sortBy") String sortBy);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.MODE=2;
        setContentView(R.layout.activity_search_view);
        initWidgets();
        progressBarSearchView.setVisibility(View.VISIBLE);
        setAnimeSearchView();
        setSearchViewClickListener();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(animeSearchView, InputMethodManager.SHOW_IMPLICIT);
        animeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newTextSearchBar=newText;
                if (newText.isEmpty())
                {

                    if (!(adapter==null))
                    {
                        adapter.clear();
                    }

                }else
                {
                    filterList(newText);
                }


                return false;
            }
        });


    }

    private void setSearchViewClickListener() {
    }

    void initWidgets()
{
    progressBarSearchView = findViewById(R.id.progressBarSearchView);
    animeSearchView = findViewById(R.id.animeSearchView);
    animListViewSearchActivity = findViewById(R.id.animListViewSearchActivity);
}

    public void setAnimeSearchView()
    {   String baseUrl = "https://anime-db.p.rapidapi.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)

                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestDataSearch requestDataSearch = retrofit.create(RequestDataSearch.class);
        requestDataSearch.getData("1","2000","ranking").enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(Call<AnimeResponse> call, Response<AnimeResponse> response) {
                if (response.isSuccessful())
                {
                    searchViewAnime = response.body().getData();
                    adapter = new animeListViewAdapter(SearchViewActivity.this,searchViewAnime);
                    animListViewSearchActivity.setDivider(new ColorDrawable(Color.TRANSPARENT));
                    animListViewSearchActivity.setAdapter(adapter);

                    progressBarSearchView.setVisibility(View.GONE);
                }else
                {
                    Toast.makeText(SearchViewActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AnimeResponse> call, Throwable t) {
                Toast.makeText(SearchViewActivity.this, t.toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void filterList(String text)
    {

        ArrayList<Anime> filteredList = new ArrayList<>();

        ArrayList<Anime> test =searchViewAnime;

        if(!test.isEmpty())
        {
            for (int i=0; i<test.size(); i++)
            {
                if (i==645)
                {
                    System.out.println();
                }
                if (test.get(i).getTitle().toLowerCase().contains(text.toLowerCase()))
                {
                    filteredList.add(test.get(i));
                }
            }
        }else
        {
            Toast.makeText(this, "Δεν υπάρχουν διαθέσιμα γεγονότα.", Toast.LENGTH_SHORT).show();
        }


        if (filteredList.isEmpty())
        {
            Toast.makeText(this, "Δεν υπάρχουν διαθέσιμα γεγονότα.", Toast.LENGTH_SHORT).show();
            GlobalFormats.reloadActivity(SearchViewActivity.this);
        }else
        {
            adapter = new animeListViewAdapter(SearchViewActivity.this,filteredList);
            animListViewSearchActivity.setDivider(new ColorDrawable(Color.TRANSPARENT));
            animListViewSearchActivity.setAdapter(adapter);
        }


    }
}