package com.grrigore.themoviedb.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    List<MovieRoom> getAllMovies();

    @Query("SELECT * FROM movie WHERE title LIKE :title")
    List<MovieRoom> getMovieByTitle(String title);


    @Query("SELECT * FROM movie WHERE id == :id")
    MovieRoom getMovieById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieRoom movie);
}
