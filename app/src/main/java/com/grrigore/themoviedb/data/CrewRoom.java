package com.grrigore.themoviedb.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "crew")
//,
//indices = @Index("movie_id"),
//foreignKeys = @ForeignKey(entity = MovieRoom.class, parentColumns = "id", childColumns = "movie_id"))
public class CrewRoom {
    @PrimaryKey
    private int id;
    //@ColumnInfo(name = "movie_id")
    //private int movieId;
    private String name;
    private String department;

    public CrewRoom() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "CrewRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
