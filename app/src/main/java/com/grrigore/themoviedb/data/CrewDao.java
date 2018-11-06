package com.grrigore.themoviedb.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CrewDao {

    @Query("SELECT * FROM crew")
    List<CrewRoom> getCrew();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CrewRoom crew);
}
