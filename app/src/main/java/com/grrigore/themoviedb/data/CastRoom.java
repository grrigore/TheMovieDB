package com.grrigore.themoviedb.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "cast")
        //,
        //indices = @Index("movie_id"),
        //foreignKeys = @ForeignKey(entity = MovieRoom.class, parentColumns = "id", childColumns = "movie_id"))
public class CastRoom {
    @PrimaryKey
    private int id;
    //@ColumnInfo(name = "movie_id")
    //private int movieId;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] profile;

    public CastRoom() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "CastRoom{" +
                "id=" + id +
                ", profile=" + Arrays.toString(profile) +
                '}';
    }
}
