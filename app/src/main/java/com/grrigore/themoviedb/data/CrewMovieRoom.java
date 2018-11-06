package com.grrigore.themoviedb.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "crew_movie",
        indices = {@Index("movie_id"), @Index("crew_id")},
        foreignKeys = {@ForeignKey(entity = CrewRoom.class, parentColumns = "id", childColumns = "crew_id"),
                @ForeignKey(entity = MovieRoom.class, parentColumns = "id", childColumns = "movie_id")})
public class CrewMovieRoom {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "crew_id")
    private int crewId;

    public CrewMovieRoom() {
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

    public int getCrewId() {
        return crewId;
    }

    public void setCrewId(int crewId) {
        this.crewId = crewId;
    }

    @Override
    public String toString() {
        return "CrewMovieRoom{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", crewId=" + crewId +
                '}';
    }
}