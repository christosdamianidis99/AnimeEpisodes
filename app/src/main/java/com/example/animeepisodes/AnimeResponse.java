package com.example.animeepisodes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AnimeResponse {
    @SerializedName("data")
    private ArrayList<Anime> data;
    @SerializedName("meta")
    private meta meta;

    public AnimeResponse(ArrayList<Anime> data, com.example.animeepisodes.meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public ArrayList<Anime> getData() {
        return data;
    }

    public void setData(ArrayList<Anime> data) {
        this.data = data;
    }

    public com.example.animeepisodes.meta getMeta() {
        return meta;
    }

    public void setMeta(com.example.animeepisodes.meta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "AnimeResponse{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }
}
