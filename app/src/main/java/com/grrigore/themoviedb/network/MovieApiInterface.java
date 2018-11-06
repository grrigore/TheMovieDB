package com.grrigore.themoviedb.network;

import com.grrigore.themoviedb.model.Credits;
import com.grrigore.themoviedb.model.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiInterface {

    @GET("/3/movie/{category}")
    Call<MoviesList> getMovies(@Path("category") String category,
                               @Query("api_key") String apiKey,
                               @Query("language") String language);

    @GET("/3/movie/{id}/credits")
    Call<Credits> getCredits(@Path("id") int id,
                             @Query("api_key") String apiKey);
}
