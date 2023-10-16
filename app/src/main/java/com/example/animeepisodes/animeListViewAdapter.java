package com.example.animeepisodes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class animeListViewAdapter extends ArrayAdapter<Anime> {

    Context context = this.getContext();
    DatabaseHelper myDB = new DatabaseHelper(context);


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

        ImageView animeImage = convertView.findViewById(R.id.animeImage);

        TextView titleTV = convertView.findViewById(R.id.titleTV);
        TextView genreTV = convertView.findViewById(R.id.genreTV);
        TextView contentTV = convertView.findViewById(R.id.contentTV);
        TextView seasonCounter = convertView.findViewById(R.id.seasonCounter);
        TextView episodeCounter = convertView.findViewById(R.id.episodeCounter);


        Anime anime = getItem(position);
        if (anime != null) {
            String animeUrlImage = anime.getImage();
            Picasso.get().load(animeUrlImage).into(animeImage);
        }

        titleTV.setText(anime.getTitle());
        genreTV.setText(anime.getGenres().toString());
        contentTV.setText(anime.getSynopsis());

        seasonCounter.setText(anime.getSeasonCount());
        episodeCounter.setText(anime.getEpisodeCount());


        return convertView;
    }





}
