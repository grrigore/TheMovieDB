package com.grrigore.themoviedb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesList {
    @SerializedName("results")
    private List<Movie> movieList;

    public List<Movie> getResults() {
        return movieList;
    }
}
