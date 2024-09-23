package com.example.animeepisodes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class animeListViewAdapter extends ArrayAdapter<Anime> {

    Context context = this.getContext();
    DatabaseHelper myDB = new DatabaseHelper(context);
    private Activity activity;
    private ArrayList<Anime> animeArrayList = new ArrayList<>();


    public animeListViewAdapter(@NonNull Context context, ArrayList<Anime> animeList, Activity activity) {
        super(context, 0, animeList);
        this.activity = activity;
        this.animeArrayList = animeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.anime_cell_view, parent, false);
        }

        Anime myAnime = animeArrayList.get(position);


        ImageView animeImage = convertView.findViewById(R.id.animeImage);
        ImageButton deleteAnime = convertView.findViewById(R.id.deleteAnime);
        TextView titleTV = convertView.findViewById(R.id.titleTV);
        TextView genreTV = convertView.findViewById(R.id.genreTV);
        TextView contentTV = convertView.findViewById(R.id.contentTV);
        TextView totalEpisodesTV = convertView.findViewById(R.id.animeTotalEpisodes);
        TextView rankingTv = convertView.findViewById(R.id.rankingTv);
        LinearLayout linLayEpSeason = convertView.findViewById(R.id.linLaySeasonEpisode);

        EditText episodeCounterEditText = convertView.findViewById(R.id.episodeCounterEditText);
        episodeCounterEditText.setText(myAnime.getEpisodeCount());


        TextView saveCounterButton = convertView.findViewById(R.id.saveCounterButton);


        if (MyAnimeList_activity.MODE == 2) {


            String animeUrlImage = myAnime.getImage();

            // Define the filename for the image associated with this anime
            final String filename = "anime_" + myAnime.get_id() + ".png";

            // Check if the image is available in internal storage
            File imageFile = new File(context.getFilesDir(), filename);

            if (imageFile.exists()) {
                animeImage.setVisibility(View.VISIBLE);
                // Image exists in internal storage; load it
                Picasso.get().load(imageFile).into(animeImage);
            } else {
                if (GlobalFormats.isInternetConnected(context)) {
                    // Internet is available; load the image from the internet
                    Picasso.get().load(animeUrlImage).into(animeImage);

                    // Download and save the image to internal storage in the background
                    downloadAndSaveImage(animeUrlImage, filename);
                } else {
                    // No internet connection; hide the animeImage view
                    animeImage.setVisibility(View.GONE);
                }
            }


            titleTV.setText(myAnime.getTitle());
            if (myAnime.getGenres() == null) {
                genreTV.setVisibility(View.GONE);
            } else {
                genreTV.setVisibility(View.VISIBLE);
                genreTV.setText(myAnime.getGenres().toString());
            }
            rankingTv.setText("Ranking: " + String.valueOf(myAnime.getRanking()));
            contentTV.setText(myAnime.getSynopsis());
            totalEpisodesTV.setText(String.valueOf(myAnime.getEpisodes()));
            deleteAnime.setVisibility(View.GONE);
            linLayEpSeason.setVisibility(View.GONE);

        } else if (MyAnimeList_activity.MODE == 1) {

            rankingTv.setVisibility(View.GONE);
            String animeUrlImage = myAnime.getImage();

            setImageFromDB(animeImage, animeUrlImage, myAnime);


            titleTV.setText(myAnime.getTitle());
            if (myAnime.getGenres() == null) {
                genreTV.setVisibility(View.GONE);
            } else {
                genreTV.setVisibility(View.VISIBLE);
                genreTV.setText(myAnime.getGenres().toString());
            }
            contentTV.setText(myAnime.getSynopsis());
            totalEpisodesTV.setText(String.valueOf(myAnime.getEpisodes()));
            linLayEpSeason.setVisibility(View.VISIBLE);
            deleteAnimeFromDB(deleteAnime, myAnime);

            episodeCounterEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String newEpisodeCount = episodeCounterEditText.getText().toString().trim();
                        if (!newEpisodeCount.isEmpty()) {
                            // Save the new episode count to the database when focus is lost
                            myAnime.setEpisodeCount(newEpisodeCount);
                            myDB.updateEpisode(String.valueOf(myAnime.get_id()), Integer.parseInt(newEpisodeCount));

                        }
                        episodeCounterEditText.setText(myAnime.getEpisodeCount());
                        episodeCounterEditText.clearFocus();
                    }else
                    {
                        episodeCounterEditText.setText("");


                    }
                }
            });


            saveCounterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(episodeCounterEditText.getText().toString().trim().isEmpty() || episodeCounterEditText.getText().toString().trim()==null)) {
                        myAnime.setEpisodeCount(episodeCounterEditText.getText().toString().trim());
                        myDB.updateEpisode(String.valueOf(myAnime.get_id()), Integer.parseInt(myAnime.getEpisodeCount()));
                        episodeCounterEditText.setText(myAnime.getEpisodeCount());
                    }


                    episodeCounterEditText.clearFocus();


                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                }
            });

        }

        return convertView;
    }



    public void addAll(ArrayList<Anime> newItems) {
        // Add new items to the existing list and notify adapter
        this.animeArrayList.addAll(newItems);
        notifyDataSetChanged();
    }

    public void clear() {
        // Clear the list and notify adapter
        this.animeArrayList.clear();
        notifyDataSetChanged();
    }

    private void setImageFromDB(ImageView animeImage, String animeUrlImage, Anime myAnime) {
        final String filename = "anime_" + myAnime.get_id() + ".png"; // Use myAnime.get_id for unique filenames
        File imageFile = new File(context.getFilesDir(), filename);

        if (imageFile.exists()) {
            Picasso.get().load(imageFile).into(animeImage);
        } else {
            loadImageFromNetwork(animeImage, animeUrlImage, filename);
        }
    }

    private void loadImageFromNetwork(ImageView animeImage, String animeUrlImage, String filename) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = (networkInfo != null && networkInfo.isConnected());

        if (isConnected) {
            Picasso.get()
                    .load(animeUrlImage)
                    .error(R.drawable.placeholder_background) // Use a placeholder for errors
                    .into(animeImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Optionally, handle successful image load
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });

            new Thread(() -> {
                InputStream input = null;
                FileOutputStream output = null;
                try {
                    URL url = new URL(animeUrlImage);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    input = connection.getInputStream();
                    output = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = input.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    void deleteAnimeFromDB(ImageButton deleteAnime, Anime myAnime) {

        deleteAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the ID of the anime
                int animeId = myAnime.get_id();

                // Define the filename for the image associated with this anime
                String filename = "anime_" + animeId + ".png";

                // Delete the image from internal storage (if it exists)
                File imageFile = new File(context.getFilesDir(), filename);
                Log.d("Image File Path", imageFile.getAbsolutePath());

                if (imageFile.exists()) {
                    boolean isDeleted = imageFile.delete();
                    if (isDeleted) {
                        Toast.makeText(context, "Image delete success", Toast.LENGTH_SHORT).show();
                    } else {
                        // File deletion failed
                        // Handle the error or log it
                        Toast.makeText(context, "Error image", Toast.LENGTH_SHORT).show();
                    }

                }


                // Delete the anime from the database
                myDB.deleteOneRowAnime(String.valueOf(animeId));
                MainActivity.refresh(activity);

            }
        });


    }

    public void clearFocusOnEpisodeEditText(int position) {
        View view = getView(position, null, null);
        EditText episodeCounterEditText = view.findViewById(R.id.episodeCounterEditText);
        episodeCounterEditText.clearFocus();
    }

    private void downloadAndSaveImage(final String imageUrl, final String filename) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream input = connection.getInputStream();
                    FileOutputStream output = context.openFileOutput(filename, Context.MODE_PRIVATE);

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = input.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }

                    output.close();
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
