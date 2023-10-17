package com.example.animeepisodes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface animeApi {


    @GET("anime")
    Call<AnimeResponse> createAnimeBody(@Body AnimePost animePost);



}
