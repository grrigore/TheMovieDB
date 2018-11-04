package com.grrigore.themoviedb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credits {
    @SerializedName("cast")
    private List<Cast> castList;
    @SerializedName("crew")
    private List<Crew> crewList;

    public List<Cast> getCastList() {
        return castList;
    }

    public List<Crew> getCrewList() {
        return crewList;
    }
}
