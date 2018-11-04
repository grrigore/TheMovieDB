package com.grrigore.themoviedb.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CastMovieRoomDao {

    @Insert
    void insert(CastMovieRoom castMovieRoom);

    @Query("SELECT `cast`.id, `cast`.profile FROM `cast` " +
            "INNER JOIN cast_movie ON `cast`.id = cast_movie.cast_id " +
            "WHERE cast_movie.movie_id == :movieId")
    List<CastRoom> getCastByMovieId(int movieId);
}
