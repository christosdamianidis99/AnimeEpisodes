package com.example.animeepisodes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Anime {
    @SerializedName("_id")
private int _id;
    @SerializedName("title")
private String title;
    @SerializedName("alternativeTitles:")
private ArrayList<String> alternativeTitles;
    @SerializedName("ranking")
private int ranking;
    @SerializedName("genres")
private ArrayList<String> genres;
    @SerializedName("episodes")
private int episodes;
    @SerializedName("hasEpisode")
private boolean hasEpisode;
    @SerializedName("hasRanking")
private boolean hasRanking;
    @SerializedName("image")
private String image;
    @SerializedName("link")
private String link;
    @SerializedName("status")
private String status;
    @SerializedName("synopsis")
private String synopsis;
    @SerializedName("thumb")
private String thumb;
    @SerializedName("type")
private String type;

    private int seasonCount=0;
    private int episodeCount=0;

    public Anime(int _id, String title, ArrayList<String> genres, int episodes, String image, String synopsis, int seasonCount, int episodeCount) {
        this._id = _id;
        this.title = title;
        this.genres = genres;
        this.episodes = episodes;
        this.image = image;
        this.synopsis = synopsis;
        this.seasonCount = seasonCount;
        this.episodeCount = episodeCount;
    }

    public Anime(int _id, String title, ArrayList<String> alternativeTitles, int ranking, ArrayList<String> genres, int episodes, boolean hasEpisode, boolean hasRanking, String image, String link, String status, String synopsis, String thumb, String type, int seasonCount, int episodeCount) {
        this._id = _id;
        this.title = title;
        this.alternativeTitles = alternativeTitles;
        this.ranking = ranking;
        this.genres = genres;
        this.episodes = episodes;
        this.hasEpisode = hasEpisode;
        this.hasRanking = hasRanking;
        this.image = image;
        this.link = link;
        this.status = status;
        this.synopsis = synopsis;
        this.thumb = thumb;
        this.type = type;
        this.seasonCount = seasonCount;
        this.episodeCount = episodeCount;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAlternativeTitles() {
        return alternativeTitles;
    }

    public void setAlternativeTitles(ArrayList<String> alternativeTitles) {
        this.alternativeTitles = alternativeTitles;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public boolean isHasEpisode() {
        return hasEpisode;
    }

    public void setHasEpisode(boolean hasEpisode) {
        this.hasEpisode = hasEpisode;
    }

    public boolean isHasRanking() {
        return hasRanking;
    }

    public void setHasRanking(boolean hasRanking) {
        this.hasRanking = hasRanking;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }
    public void incrementSeason() {
        seasonCount++;
    }

    public void decrementSeason() {
        seasonCount--;
    }

    public void incrementEpisode() {
        episodeCount++;
    }

    public void decrementEpisode() {
        episodeCount--;
    }
    @Override
    public String toString() {
        return "Anime{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", alternativeTitles=" + alternativeTitles +
                ", ranking=" + ranking +
                ", genres=" + genres +
                ", episodes=" + episodes +
                ", hasEpisode=" + hasEpisode +
                ", hasRanking=" + hasRanking +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", status='" + status + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", thumb='" + thumb + '\'' +
                ", type='" + type + '\'' +
                ", seasonCount=" + seasonCount +
                ", episodeCount=" + episodeCount +
                '}';
    }
}
