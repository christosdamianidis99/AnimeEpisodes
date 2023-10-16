package com.example.animeepisodes;

import com.google.gson.annotations.SerializedName;

public class AnimeBody {
    @SerializedName("page")
    String page;
    @SerializedName("size")
    String size;

    public AnimeBody(String page, String size) {
        this.page = page;
        this.size = size;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "AnimeBody{" +
                "page='" + page + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
