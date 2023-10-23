package com.example.animeepisodes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class animeListViewAdapter extends ArrayAdapter<Anime> {

    Context context = this.getContext();
    DatabaseHelper myDB = new DatabaseHelper(context);
    private static int iSeason = 0;
    private static int iEpisode = 0;

    public animeListViewAdapter(@NonNull Context context, List<Anime> animeList) {
        super(context, 0,animeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.anime_cell_view, parent, false);
        }


//        seasonCounter.setText(anime.getSeasonCount());
//        episodeCounter.setText(anime.getEpisodeCount());
            if (MainActivity.MODE==2)
            {
                ImageView animeImage = convertView.findViewById(R.id.animeImage);

                TextView titleTV = convertView.findViewById(R.id.titleTV);
                TextView genreTV = convertView.findViewById(R.id.genreTV);
                TextView contentTV = convertView.findViewById(R.id.contentTV);
                TextView totalEpisodesTV= convertView.findViewById(R.id.animeTotalEpisodes);
                LinearLayout linLayEpSeason = convertView.findViewById(R.id.linLaySeasonEpisode);



                Anime anime = getItem(position);
                if (anime != null) {
                    String animeUrlImage = anime.getImage();
                    Picasso.get().load(animeUrlImage).into(animeImage);
                }

                titleTV.setText(anime.getTitle());
                genreTV.setText(anime.getGenres().toString());
                contentTV.setText(anime.getSynopsis());
                totalEpisodesTV.setText(anime.getEpisodes());
                linLayEpSeason.setVisibility(View.GONE);

            } else if (MainActivity.MODE==1) {
                ImageView animeImage = convertView.findViewById(R.id.animeImage);

                TextView titleTV = convertView.findViewById(R.id.titleTV);
                TextView genreTV = convertView.findViewById(R.id.genreTV);
                TextView contentTV = convertView.findViewById(R.id.contentTV);
                TextView totalEpisodesTV= convertView.findViewById(R.id.animeTotalEpisodes);
                LinearLayout linLayEpSeason = convertView.findViewById(R.id.linLaySeasonEpisode);


                Anime anime = getItem(position);
                if (anime != null) {
                    String animeUrlImage = anime.getImage();
                    Picasso.get().load(animeUrlImage).into(animeImage);
                }

                titleTV.setText(anime.getTitle());
                genreTV.setText(anime.getGenres().toString());
                contentTV.setText(anime.getSynopsis());
                totalEpisodesTV.setText(anime.getEpisodes());
                linLayEpSeason.setVisibility(View.VISIBLE);
                plusMinusClickListeners(convertView,anime);

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
            }
        });
        plusEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                iEpisode++;
                anime.incrementEpisode();
                String counter = String.valueOf(anime.getEpisodeCount());
                episodeCounter.setText(counter);
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
            }
        });

    }


}
