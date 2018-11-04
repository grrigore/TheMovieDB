package com.grrigore.themoviedb.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CrewMovieRoomDao {

    @Insert
    void insert(CrewMovieRoom crewMovieRoom);

    @Query("SELECT crew.id, crew.name, crew.department FROM crew " +
            "INNER JOIN crew_movie ON crew.id = crew_movie.crew_id " +
            "WHERE crew_movie.movie_id == :movieId")
    List<CrewRoom> getCrewByMovieId(int movieId);
}
