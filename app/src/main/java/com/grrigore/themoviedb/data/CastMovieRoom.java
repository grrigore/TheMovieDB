package com.grrigore.themoviedb.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "cast_movie",
        indices = {@Index("movie_id"), @Index("cast_id")},
        foreignKeys = {@ForeignKey(entity = CastRoom.class, parentColumns = "id", childColumns = "cast_id"),
                @ForeignKey(entity = MovieRoom.class, parentColumns = "id", childColumns = "movie_id")})
public class CastMovieRoom {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "cast_id")
    private int castId;

    public CastMovieRoom() {
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

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    @Override
    public String toString() {
        return "CastMovieRoom{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", castId=" + castId +
                '}';
    }
}
