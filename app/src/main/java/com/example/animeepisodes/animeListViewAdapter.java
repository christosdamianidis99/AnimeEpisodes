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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class animeListViewAdapter extends ArrayAdapter<Anime> {

    Context context = this.getContext();
    DatabaseHelper myDB = new DatabaseHelper(context);
    private Activity activity;
    public static Anime myAnime;


    public animeListViewAdapter(@NonNull Context context, List<Anime> animeList,Activity activity) {
        super(context, 0,animeList);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.anime_cell_view, parent, false);
        }


            if (MainActivity.MODE==2)
            {
                ImageView animeImage = convertView.findViewById(R.id.animeImage);
                ImageButton deleteAnime = convertView.findViewById(R.id.deleteAnime);
                TextView titleTV = convertView.findViewById(R.id.titleTV);
                TextView genreTV = convertView.findViewById(R.id.genreTV);
                TextView contentTV = convertView.findViewById(R.id.contentTV);
                TextView totalEpisodesTV= convertView.findViewById(R.id.animeTotalEpisodes);
                LinearLayout linLayEpSeason = convertView.findViewById(R.id.linLaySeasonEpisode);



                 myAnime = getItem(position);
                String animeUrlImage = myAnime.getImage();

                if (myAnime != null) {

                    // Define the filename for the image associated with this anime
                    final String filename = "anime_" + myAnime.get_id() + ".png";

                    // Check if the image is available in internal storage
                    File imageFile = new File(context.getFilesDir(), filename);

                    if (imageFile.exists()) {
                        animeImage.setVisibility(View.VISIBLE);
                        // Image exists in internal storage; load it
                        Picasso.get().load(imageFile).into(animeImage);
                    } else {
                        if (isInternetConnected()) {
                            // Internet is available; load the image from the internet
                            Picasso.get().load(animeUrlImage).into(animeImage);

                            // Download and save the image to internal storage in the background
                            downloadAndSaveImage(animeUrlImage, filename);
                        } else {
                            // No internet connection; hide the animeImage view
                            animeImage.setVisibility(View.GONE);
                        }
                    }
                }



                assert myAnime != null;
                titleTV.setText(myAnime.getTitle());
                if (myAnime.getGenres()==null)
                {
                genreTV.setVisibility(View.GONE);
                }else {
                    genreTV.setVisibility(View.VISIBLE);
                    genreTV.setText(myAnime.getGenres().toString());
                }
                contentTV.setText(myAnime.getSynopsis());
                totalEpisodesTV.setText(String.valueOf(myAnime.getEpisodes()));
                deleteAnime.setVisibility(View.GONE);
                linLayEpSeason.setVisibility(View.GONE);

            } else if (MainActivity.MODE==1) {
                ImageView animeImage = convertView.findViewById(R.id.animeImage);
                ImageButton deleteAnime = convertView.findViewById(R.id.deleteAnime);
                TextView titleTV = convertView.findViewById(R.id.titleTV);
                TextView genreTV = convertView.findViewById(R.id.genreTV);
                TextView contentTV = convertView.findViewById(R.id.contentTV);
                TextView totalEpisodesTV= convertView.findViewById(R.id.animeTotalEpisodes);
                LinearLayout linLayEpSeason = convertView.findViewById(R.id.linLaySeasonEpisode);


                 myAnime = getItem(position);
                String animeUrlImage = myAnime.getImage();


                if (myAnime != null) {
                    final String filename = "anime_" + myAnime.get_id() + ".png"; // Use myAnime.get_id for unique filenames

                    // Check if the image is available in internal storage
                    File imageFile = new File(context.getFilesDir(), filename);
                    if (imageFile.exists()) {

                        // Load the image from internal storage
                        Picasso.get().load(imageFile).into(animeImage);
                    } else {
                        // Check for internet connectivity
                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        boolean isConnected = (networkInfo != null && networkInfo.isConnected());

                        if (isConnected) {
                            // Load and cache the image from the internet
                            Picasso.get().load(animeUrlImage).into(animeImage);

                            // Download and save the image to internal storage in the background
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        URL url = new URL(animeUrlImage);
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
                }




//
                titleTV.setText(myAnime.getTitle());
                if (myAnime.getGenres()==null)
                {
                    genreTV.setVisibility(View.GONE);
                }else {
                    genreTV.setVisibility(View.VISIBLE);
                    genreTV.setText(myAnime.getGenres().toString());
                }                contentTV.setText(myAnime.getSynopsis());
                totalEpisodesTV.setText(String.valueOf(myAnime.getEpisodes()));
                linLayEpSeason.setVisibility(View.VISIBLE);
                deleteAnimeFromDB(deleteAnime);
                plusMinusClickListeners(convertView,myAnime);
            }

        return convertView;
    }


    void plusMinusClickListeners(View convertView,Anime anime)
    {

        ImageButton plusSeason = convertView.findViewById(R.id.plusSeason);
        ImageButton minusSeason = convertView.findViewById(R.id.minusSeason);
        ImageButton plusEpisode = convertView.findViewById(R.id.plusEpisode);
        ImageButton minusEpisode = convertView.findViewById(R.id.minusEpisode);
        TextView seasonCounter = convertView.findViewById(R.id.seasonCounter);
        TextView episodeCounter = convertView.findViewById(R.id.episodeCounter);

        plusSeason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                iSeason++;
                anime.incrementSeason();
                String counter = String.valueOf(anime.getSeasonCount());
                seasonCounter.setText(counter);
                myDB.updateSeason(String.valueOf(anime.get_id()),anime.getSeasonCount());
            }
        });

        minusSeason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                iSeason--;
                anime.decrementSeason();

                if (anime.getSeasonCount()<0)
                {
                    anime.setSeasonCount(0);
                }
                String counter = String.valueOf(anime.getSeasonCount());
                seasonCounter.setText(counter);
                myDB.updateSeason(String.valueOf(anime.get_id()),anime.getSeasonCount());

            }
        });
        plusEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                iEpisode++;
                anime.incrementEpisode();
                String counter = String.valueOf(anime.getEpisodeCount());
                episodeCounter.setText(counter);
                myDB.updateEpisode(String.valueOf(anime.get_id()),anime.getEpisodeCount());
            }
        });
        minusEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                iEpisode--;
                anime.decrementEpisode();
                if (anime.getEpisodeCount()<0)
                {
                   anime.setEpisodeCount(0);
                }
                String counter = String.valueOf(anime.getEpisodeCount());
                episodeCounter.setText(counter);
                myDB.updateEpisode(String.valueOf(anime.get_id()),anime.getEpisodeCount());
            }
        });

    }

    void deleteAnimeFromDB(ImageButton deleteAnime)
    {

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
                    }
                    else {
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

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
