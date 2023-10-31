package com.example.animeepisodes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class SearchViewActivity extends AppCompatActivity {
    SearchView animeSearchView;
    DatabaseHelper databaseHelper;
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
        databaseHelper = new DatabaseHelper(getApplicationContext());
        progressBarSearchView.setVisibility(View.VISIBLE);
        animeSearchView.setVisibility(View.GONE);
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
        animListViewSearchActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Anime anim = (Anime) adapterView.getAdapter().getItem(i);
            String genre;
            if (anim.getGenres()==null)
            {
                genre = null;
            }else {
                genre = anim.getGenres().toString();
            }
            databaseHelper.addAnime(String.valueOf(anim.get_id()),anim.getTitle(),genre,String.valueOf(anim.getEpisodes()),anim.getSynopsis(),"0","0",anim.getImage());
                Intent i1 = new Intent(SearchViewActivity.this,MainActivity.class);
                startActivity(i1);
            }
        });
    }

    void initWidgets()
{
    progressBarSearchView = findViewById(R.id.progressBarSearchView);
    animeSearchView = findViewById(R.id.animeSearchView);
    animListViewSearchActivity = findViewById(R.id.animListViewSearchActivity);
}

    public void setAnimeSearchView()
    {
        searchViewAnime=new ArrayList<>();
        if (!databaseHelper.isTableEmpty("search_anime_episodes_db"))
        {

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


            if (!searchViewAnime.isEmpty()) {
                searchViewAnime.sort(new Comparator<Anime>() {
                    @Override
                    public int compare(Anime o1, Anime o2) {
                        return Integer.compare(o1.getRanking(), o2.getRanking());
                    }
                });
                adapter = new animeListViewAdapter(SearchViewActivity.this, searchViewAnime,this);
                animListViewSearchActivity.setDivider(new ColorDrawable(Color.TRANSPARENT));
                animListViewSearchActivity.setAdapter(adapter);

                Animation cardAnimation = AnimationUtils.loadAnimation(SearchViewActivity.this, R.anim.cardanimation);
                LayoutAnimationController controller = new LayoutAnimationController(cardAnimation);
                controller.setDelay(0.2f);
                animListViewSearchActivity.setLayoutAnimation(controller);
                animListViewSearchActivity.scheduleLayoutAnimation();

            }
            animeSearchView.setVisibility(View.VISIBLE);
            progressBarSearchView.setVisibility(View.GONE);

        }else
        {
            String baseUrl = "https://anime-db.p.rapidapi.com/";
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
                        searchViewAnime.sort(new Comparator<Anime>() {
                            @Override
                            public int compare(Anime o1, Anime o2) {
                                return Integer.compare(o1.getRanking(), o2.getRanking());
                            }
                        });

                        for (int i=0; i<searchViewAnime.size(); i++)
                        {
                            databaseHelper.addSearchAnime(String.valueOf(searchViewAnime.get(i).get_id()),
                                    searchViewAnime.get(i).getTitle(),
                                    searchViewAnime.get(i).getGenres().toString(),
                                    String.valueOf(searchViewAnime.get(i).getEpisodes()),
                                    searchViewAnime.get(i).getSynopsis(),
                                    searchViewAnime.get(i).getImage(),
                                    String.valueOf(searchViewAnime.get(i).getRanking()));
                        }



                        adapter = new animeListViewAdapter(SearchViewActivity.this,searchViewAnime,SearchViewActivity.this);
                        animListViewSearchActivity.setDivider(new ColorDrawable(Color.TRANSPARENT));
                        animListViewSearchActivity.setAdapter(adapter);


                        Animation cardAnimation = AnimationUtils.loadAnimation(SearchViewActivity.this, R.anim.cardanimation);
                        LayoutAnimationController controller = new LayoutAnimationController(cardAnimation);
                        controller.setDelay(0.2f);
                        animListViewSearchActivity.setLayoutAnimation(controller);
                        animListViewSearchActivity.scheduleLayoutAnimation();


                        animeSearchView.setVisibility(View.VISIBLE);
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
            adapter = new animeListViewAdapter(SearchViewActivity.this,filteredList,this);
            animListViewSearchActivity.setDivider(new ColorDrawable(Color.TRANSPARENT));
            animListViewSearchActivity.setAdapter(adapter);
        }


    }
}