package com.example.animeepisodes;

import com.google.gson.annotations.SerializedName;

public class AnimePost {
    @SerializedName("page")
    String page;
    @SerializedName("size")
    String size;
    @SerializedName("search")
    String search;
    @SerializedName("genres")
    String genres;
    @SerializedName("sortBy")
    String sortBy;
    @SerializedName("sortOrder")
    String sortOrder;
    @SerializedName("types")
    String types;

    public AnimePost(String page, String size) {
        this.page = page;
        this.size = size;
    }

    public AnimePost(String page, String size, String search, String genres, String sortBy, String sortOrder, String types) {
        this.page = page;
        this.size = size;
        this.search = search;
        this.genres = genres;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
        this.types = types;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "AnimePost{" +
                "page='" + page + '\'' +
                ", size='" + size + '\'' +
                ", search='" + search + '\'' +
                ", genres='" + genres + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", types='" + types + '\'' +
                '}';
    }
}
