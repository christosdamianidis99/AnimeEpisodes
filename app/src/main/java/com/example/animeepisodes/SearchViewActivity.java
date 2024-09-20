package com.example.animeepisodes;

import static com.example.animeepisodes.GlobalFormats.setUpListView;
import static com.example.animeepisodes.MainActivity.searchViewAnime;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Comparator;

public class SearchViewActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton menuButton;


    SearchView animeSearchView;
    ImageView backBtn;
    ListView animListViewSearchActivity;
    ProgressBar progressBarSearchView;
    private animeListViewAdapter adapter;
    DatabaseHelper databaseHelper;

    private static final int ITEMS_PER_PAGE = 50;
    private int currentPage = 0; // Start from the first page
    private boolean isLoading = false;
    private boolean hasMoreData = true; // To prevent multiple loads
    private String newTextSearchBar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyAnimeList_activity.MODE = 2;
        setContentView(R.layout.activity_search_view);
        initWidgets();
        setupInitialData();
        setSearchViewClickListener();
        databaseHelper = new DatabaseHelper(getApplicationContext());


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(animeSearchView, InputMethodManager.SHOW_IMPLICIT);
        animeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newTextSearchBar = newText;
                if (newText.isEmpty()) {
                    loadInitialData();
                } else {
                    filterList(newText);
                }
                return false;
            }
        });

        backBtn.setOnClickListener(v -> {
            Intent i = new Intent(SearchViewActivity.this, MyAnimeList_activity.class);
            startActivity(i);
        });

        animListViewSearchActivity.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // No action required
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Check if we need to load more items
                if (totalItemCount == (firstVisibleItem + visibleItemCount) && !isLoading && hasMoreData) {
                    loadMoreItems();
                }
            }
        });

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_top_rated) {
                // Sort by ranking
                sortByRanking();
            } else if (id == R.id.nav_action) {
                // Filter by genre "Action"
                filterByGenre("Action");
            } else if (id == R.id.nav_adventure) {
                // Filter by genre "Adventure"
                filterByGenre("Adventure");
            } else if (id == R.id.nav_drama) {
                // Filter by genre "Drama"
                filterByGenre("Drama");
            } else if (id == R.id.nav_award_winning) {
                // Filter by genre "Drama"
                filterByGenre("Award Winning");
            } else if (id == R.id.nav_suspense) {
                // Filter by genre "Drama"
                filterByGenre("Suspense");
            } else if (id == R.id.nav_horror) {
                // Filter by genre "Drama"
                filterByGenre("Horror");
            } else if (id == R.id.nav_comedy) {
                // Filter by genre "Drama"
                filterByGenre("Comedy");
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    Intent i = new Intent(SearchViewActivity.this, MyAnimeList_activity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void initWidgets() {
        drawerLayout = findViewById(R.id.drawerLayoutSearchView);
        navigationView = findViewById(R.id.nav_view_SearchView);
        menuButton = findViewById(R.id.menuButton);

        progressBarSearchView = findViewById(R.id.progressBarSearchView);
        animeSearchView = findViewById(R.id.animeSearchView);
        animListViewSearchActivity = findViewById(R.id.animListViewSearchActivity);
        backBtn = findViewById(R.id.backButtonbtn);
    }

    private void setupInitialData() {
        progressBarSearchView.setVisibility(View.VISIBLE);
        animeSearchView.setVisibility(View.GONE);
        new Thread(() -> {
            // Sort and load initial data
            searchViewAnime.sort(Comparator.comparingInt(Anime::getRanking));
            runOnUiThread(this::loadInitialData);
        }).start();
    }

    private void loadInitialData() {
        currentPage = 0; // Ensure we start from the first page
        ArrayList<Anime> currentItems = getPaginatedItems();
        currentItems.sort((o1, o2) -> Integer.compare(o1.getRanking(), o2.getRanking()));

        setUpListView(adapter,SearchViewActivity.this,currentItems,this,animListViewSearchActivity);




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
        new Thread(() -> {
            ArrayList<Anime> newItems = getPaginatedItems();
            runOnUiThread(() -> {
                if (newItems != null && !newItems.isEmpty()) {
                    if (adapter == null) {
                        new animeListViewAdapter(SearchViewActivity.this, newItems, this);

                    } else {
                        adapter.addAll(newItems);
                        adapter.notifyDataSetChanged();
                    }
                }
                isLoading = false;
            });
        }).start();
    }

    private void filterList(String text) {
        new Thread(() -> {
            ArrayList<Anime> filteredList = new ArrayList<>();
            for (Anime anime : searchViewAnime) {
                if (anime.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(anime);
                }
            }

            runOnUiThread(() -> {
                if (filteredList.isEmpty()) {
                    Toast.makeText(this, "Δεν υπάρχουν διαθέσιμα γεγονότα.", Toast.LENGTH_SHORT).show();
                } else {
                    filteredList.sort((o1, o2) -> Integer.compare(o1.getRanking(), o2.getRanking()));

                    setUpListView(adapter,SearchViewActivity.this,filteredList,this,animListViewSearchActivity);
                }
            });
        }).start();
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



    private void sortByRanking() {
        new Thread(() -> {
            ArrayList<Anime> sortedList = new ArrayList<>(searchViewAnime);
            sortedList.sort(Comparator.comparingInt(Anime::getRanking)); // Sort
            runOnUiThread(() -> {

                setUpListView(adapter,SearchViewActivity.this,sortedList,this,animListViewSearchActivity);

            });
        }).start();
    }

    private void filterByGenre(String genre) {
        new Thread(() -> {


            ArrayList<Anime> filteredList = new ArrayList<>();
            for (Anime anime : searchViewAnime) {
                if (anime.getGenres() != null && anime.getGenres().contains(genre)) {
                    filteredList.add(anime);
                }
            }
            filteredList.sort(Comparator.comparingInt(Anime::getRanking));
            runOnUiThread(() -> {
                if (filteredList.isEmpty()) {
                    Toast.makeText(this, "No results found for " + genre, Toast.LENGTH_SHORT).show();
                } else {

                    currentPage = 0; // Ensure we start from the first page

                    int start = currentPage * ITEMS_PER_PAGE;
                    int end = Math.min(start + ITEMS_PER_PAGE, filteredList.size());

                    ArrayList<Anime> pageItems = new ArrayList<>(filteredList.subList(start, end));
                    if (end == filteredList.size()) {
                        hasMoreData = false;
                    }

                    setUpListView(adapter,SearchViewActivity.this,pageItems,this,animListViewSearchActivity);
                }
            });
        }).start();
    }




}
