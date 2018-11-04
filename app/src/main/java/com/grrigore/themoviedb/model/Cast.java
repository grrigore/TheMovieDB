package com.grrigore.themoviedb.model;

import com.google.gson.annotations.SerializedName;

public class Cast {
    @SerializedName("id")
    private int id;
    @SerializedName("profile_path")
    private String profilePath;
    private int movieId;

    public Cast() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public String toString() {
        return "Cast{" +
                "id=" + id +
                ", profilePath='" + profilePath + '\'' +
                ", movieId=" + movieId +
                '}';
    }
}
