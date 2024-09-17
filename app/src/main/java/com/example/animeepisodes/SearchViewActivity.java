package com.example.animeepisodes;

import static com.example.animeepisodes.MainActivity.searchViewAnime;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
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
    ImageView backBtn;
    DatabaseHelper databaseHelper;
    ListView animListViewSearchActivity;
    private static String newTextSearchBar;
    public  animeListViewAdapter adapter;
    ProgressBar progressBarSearchView;

    private static final int ITEMS_PER_PAGE = 50;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMoreData = true; // To prevent multiple loads

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.MODE=2;
        setContentView(R.layout.activity_search_view);
        initWidgets();

        searchViewAnime.sort(new Comparator<Anime>() {
            @Override
            public int compare(Anime o1, Anime o2) {
                return Integer.compare(o1.getRanking(), o2.getRanking());
            }
        });
        databaseHelper = new DatabaseHelper(getApplicationContext());
        progressBarSearchView.setVisibility(View.VISIBLE);
        animeSearchView.setVisibility(View.GONE);
        setAdapter();
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
                     setAdapter();
                    }

                }else
                {
                    filterList(newText);
                }


                return false;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchViewActivity.this,MainActivity.class);
                startActivity(i);
            }
        });


        animListViewSearchActivity.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // No action required
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount == (firstVisibleItem + visibleItemCount) && !isLoading && hasMoreData) {
                    loadMoreItems();
                }
            }
        });
    }

    private void setSearchViewClickListener() {
        animListViewSearchActivity.setOnItemClickListener((adapterView, view, i, l) -> {
            Anime anim = (Anime) adapterView.getAdapter().getItem(i);
            String genre = (anim.getGenres() == null) ? null : anim.getGenres().toString();
            databaseHelper.addAnime(String.valueOf(anim.get_id()), anim.getTitle(), genre, String.valueOf(anim.getEpisodes()), anim.getSynopsis(), "0", "0", anim.getImage());
            Intent i1 = new Intent(SearchViewActivity.this, MainActivity.class);
            startActivity(i1);
        });
    }

    void initWidgets()
    {
        progressBarSearchView = findViewById(R.id.progressBarSearchView);
        animeSearchView = findViewById(R.id.animeSearchView);
        animListViewSearchActivity = findViewById(R.id.animListViewSearchActivity);
        backBtn = findViewById(R.id.backButtonbtn);
    }


    private void setAdapter() {
        ArrayList<Anime> currentItems = getPaginatedItems();
        currentItems.sort(new Comparator<Anime>() {
            @Override
            public int compare(Anime o1, Anime o2) {
                return Integer.compare(o1.getRanking(), o2.getRanking());
            }
        });
        adapter = new animeListViewAdapter(SearchViewActivity.this, currentItems, this);
        animListViewSearchActivity.setDivider(new ColorDrawable(Color.TRANSPARENT));
        animListViewSearchActivity.setAdapter(adapter);

        Animation cardAnimation = AnimationUtils.loadAnimation(SearchViewActivity.this, R.anim.cardanimation);
        LayoutAnimationController controller = new LayoutAnimationController(cardAnimation);
        controller.setDelay(0.2f);
        animListViewSearchActivity.setLayoutAnimation(controller);
        animListViewSearchActivity.scheduleLayoutAnimation();
        animeSearchView.setVisibility(View.VISIBLE);
        progressBarSearchView.setVisibility(View.GONE);
    }
    private ArrayList<Anime> getPaginatedItems() {
        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, searchViewAnime.size());
        ArrayList<Anime> pageItems = new ArrayList<>(searchViewAnime.subList(start, end));
        if (end == searchViewAnime.size()) {
            hasMoreData = false;
        }
        return pageItems;
    }

    private void loadMoreItems() {
        isLoading = true;
        currentPage++;
        ArrayList<Anime> newItems = getPaginatedItems();
        if (newItems != null && !newItems.isEmpty()) {
            adapter.addAll(newItems);
            adapter.notifyDataSetChanged();
        }
        isLoading = false;
    }
    private void filterList(String text) {
        ArrayList<Anime> filteredList = new ArrayList<>();
        for (Anime anime : searchViewAnime) {
            if (anime.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(anime);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Δεν υπάρχουν διαθέσιμα γεγονότα.", Toast.LENGTH_SHORT).show();
        } else {
            filteredList.sort(new Comparator<Anime>() {
                @Override
                public int compare(Anime o1, Anime o2) {
                    return Integer.compare(o1.getRanking(), o2.getRanking());
                }
            });
            adapter = new animeListViewAdapter(SearchViewActivity.this, filteredList, this);
            animListViewSearchActivity.setDivider(new ColorDrawable(Color.TRANSPARENT));
            animListViewSearchActivity.setAdapter(adapter);
        }
    }
}