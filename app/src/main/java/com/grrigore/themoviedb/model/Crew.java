package com.grrigore.themoviedb.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class Crew {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("department")
    private String department;
    @ColumnInfo(name = "movie_id")
    private int movieId;

    public Crew() {
    }

    @Ignore
    public Crew(int id, String name, String department, int movieId) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.movieId = movieId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Crew{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", movieId=" + movieId +
                '}';
    }
}
