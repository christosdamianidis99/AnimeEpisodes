package com.example.animeepisodes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface animeApi {

    @POST("https://anime-db.p.rapidapi.com/anime?page=1&size=10&sortBy=ranking&sortOrder=asc/")
    Call<AnimeResponse> createAnimeBody(@Body AnimeBody animeBody);
}
