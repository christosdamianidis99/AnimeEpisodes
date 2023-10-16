package com.example.animeepisodes;

import com.google.gson.annotations.SerializedName;

public class meta {
    @SerializedName("page")

    private int page;
    @SerializedName("size")

    private int size;
    @SerializedName("totalData")

    private int totalData;
    @SerializedName("totalPage")

    private int totalPage;

    public meta(int page, int size, int totalData, int totalPage) {
        this.page = page;
        this.size = size;
        this.totalData = totalData;
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "meta{" +
                "page=" + page +
                ", size=" + size +
                ", totalData=" + totalData +
                ", totalPage=" + totalPage +
                '}';
    }
}
