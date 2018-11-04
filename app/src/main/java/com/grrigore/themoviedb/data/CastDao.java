package com.grrigore.themoviedb.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CastDao {

    @Query("SELECT * FROM `cast`")
    List<CastRoom> getCast();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CastRoom cast);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CastRoom> cast);

    @Delete
    void delete(CastRoom cast);


}
