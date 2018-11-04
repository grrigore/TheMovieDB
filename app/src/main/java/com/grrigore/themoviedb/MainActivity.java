package com.grrigore.themoviedb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.stetho.Stetho;
import com.grrigore.themoviedb.adapter.MovieAdapter;
import com.grrigore.themoviedb.data.CastDao;
import com.grrigore.themoviedb.data.CastMovieRoom;
import com.grrigore.themoviedb.data.CastMovieRoomDao;
import com.grrigore.themoviedb.data.CastRoom;
import com.grrigore.themoviedb.data.CrewDao;
import com.grrigore.themoviedb.data.CrewMovieRoom;
import com.grrigore.themoviedb.data.CrewMovieRoomDao;
import com.grrigore.themoviedb.data.CrewRoom;
import com.grrigore.themoviedb.data.MovieDao;
import com.grrigore.themoviedb.data.MovieDatabase;
import com.grrigore.themoviedb.data.MovieRoom;
import com.grrigore.themoviedb.model.Cast;
import com.grrigore.themoviedb.model.Credits;
import com.grrigore.themoviedb.model.Crew;
import com.grrigore.themoviedb.model.Movie;
import com.grrigore.themoviedb.model.MoviesList;
import com.grrigore.themoviedb.network.MovieApiInterface;
import com.grrigore.themoviedb.network.RetrofitClientInstance;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grrigore.themoviedb.utils.Constants.API_KEY;
import static com.grrigore.themoviedb.utils.Constants.BASE_URL_POSTER;
import static com.grrigore.themoviedb.utils.Constants.BASE_URL_PROFILE;
import static com.grrigore.themoviedb.utils.Constants.CATEGORY;
import static com.grrigore.themoviedb.utils.Constants.DB_NAME;
import static com.grrigore.themoviedb.utils.Constants.LANGUAGE;
import static com.grrigore.themoviedb.utils.Constants.MOVIE_CLICKED;
import static com.grrigore.themoviedb.utils.Utils.doesDatabaseExist;
import static com.grrigore.themoviedb.utils.Utils.getSubList;


public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {


    private Context mainActivityContext;

    private RecyclerView movieListRecyclerView;
    private SearchView movieSearchView;
    private TextView resultTextView;

    private byte[] posterByteArray;
    private byte[] profileByteArray;

    private MovieApiInterface movieApiInterface;
    private MovieDao movieDao;
    private CrewDao crewDao;
    private CastDao castDao;
    private CrewMovieRoomDao crewMovieRoomDao;
    private CastMovieRoomDao castMovieRoomDao;
    private MovieAdapter movieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //stetho
        Stetho.initializeWithDefaults(this);

        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.textview_main_searchresult);
        movieSearchView = findViewById(R.id.searchview_main_search);

        movieListRecyclerView = findViewById(R.id.recyclerview_main_movielist);

        setToolbar();
        setFloatingActionButton();

        movieListAdapter = new MovieAdapter(new ArrayList<MovieRoom>(), getApplicationContext());

        movieApiInterface = RetrofitClientInstance.getRetrofitInstance().create(MovieApiInterface.class);
        populateDatabase();

        movieSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                new AsyncTask<Void, Void, List<MovieRoom>>() {
                    @Override
                    protected List doInBackground(Void... params) {
                        return movieDao.getMovieByTitle(s + "%");
                    }

                    @Override
                    protected void onPostExecute(List items) {
                        setUI(items);
                        String searchResult = "<b>" + items.size() + "</b> " + getString(R.string.search_result);
                        resultTextView.setText(Html.fromHtml(searchResult));
                        resultTextView.setVisibility(View.VISIBLE);
                    }
                }.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void populateDatabase() {
        movieDao = MovieDatabase.getMovieDatabaseInsatnce(getApplicationContext()).getMovieDao();
        if (!doesDatabaseExist(getApplicationContext(), DB_NAME)) {
            crewDao = MovieDatabase.getMovieDatabaseInsatnce(getApplicationContext()).getCrewDao();
            castDao = MovieDatabase.getMovieDatabaseInsatnce(getApplicationContext()).getCastDao();
            castMovieRoomDao = MovieDatabase.getMovieDatabaseInsatnce(getApplicationContext()).getCastMovieRoomDao();
            crewMovieRoomDao = MovieDatabase.getMovieDatabaseInsatnce(getApplicationContext()).getCrewMovieRoomDao();
            getMovieList();
        } else {
            new AsyncTask<Void, Void, List<MovieRoom>>() {
                @Override
                protected List doInBackground(Void... params) {
                    return movieDao.getAllMovies();
                }

                @Override
                protected void onPostExecute(List items) {
                    setUI(items);
                }
            }.execute();
        }
    }

    private void setUI(List<MovieRoom> movies) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        movieListRecyclerView.setLayoutManager(layoutManager);
        movieListAdapter.setMovies(movies);
        movieListAdapter.setItemClickListener(this);
        movieListAdapter.notifyDataSetChanged();
        movieListRecyclerView.setAdapter(movieListAdapter);
    }

    private void getMovieList() {
        Call<MoviesList> moviesListCall = movieApiInterface.getMovies(CATEGORY, API_KEY, LANGUAGE);
        moviesListCall.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                MoviesList moviesList = response.body();
                List<Movie> movieList = moviesList.getResults();

                final List<MovieRoom> movieRoomList = new ArrayList<>();
                for (final Movie movie : movieList) {
                    String posterUrl = BASE_URL_POSTER + movie.getPosterPath();
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(posterUrl)
                            .apply(new RequestOptions()
                                    .override(120, 180)
                                    .centerCrop())
                            .into(new Target<Bitmap>() {
                                MovieRoom movieRoom = new MovieRoom();

                                @Override
                                public void onLoadStarted(@Nullable Drawable placeholder) {
                                    movieRoom.setId(movie.getId());
                                    movieRoom.setOverview(movie.getOverview());
                                    movieRoom.setReleaseDate(movie.getReleaseDate());
                                    movieRoom.setTitle(movie.getTitle());
                                    movieRoom.setVoteAverage(movie.getVoteAverage());
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    movieRoom.setPoster(null);
                                }

                                @Override
                                public void onResourceReady(@NonNull Bitmap resourcePoster, @Nullable Transition<? super Bitmap> transition) {
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    resourcePoster.compress(Bitmap.CompressFormat.PNG, 0, stream);
                                    posterByteArray = stream.toByteArray();
                                    movieRoom.setPoster(posterByteArray);

                                    Log.d("TAG", movieRoom.toString());

                                    new AsyncTask<Void, Void, MovieRoom>() {
                                        @Override
                                        protected MovieRoom doInBackground(Void... voids) {
                                            movieDao.insert(movieRoom);
                                            return movieRoom;
                                        }

                                        @Override
                                        protected void onPostExecute(MovieRoom movieRoom) {
                                            movieRoomList.add(movieRoom);
                                            setUI(movieRoomList);
                                            getCredits(movieRoom.getId());
                                        }
                                    }.execute();
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }

                                @Override
                                public void getSize(@NonNull SizeReadyCallback cb) {

                                }

                                @Override
                                public void removeCallback(@NonNull SizeReadyCallback cb) {

                                }

                                @Override
                                public void setRequest(@Nullable Request request) {

                                }

                                @Nullable
                                @Override
                                public Request getRequest() {
                                    return null;
                                }

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onStop() {

                                }

                                @Override
                                public void onDestroy() {
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connect to the internet!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCredits(final int movieId) {
        Call<Credits> moviesListCall = movieApiInterface.getCredits(movieId, API_KEY);
        moviesListCall.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                Credits credits = response.body();

                //Get top 5/top 6 from cast/crew
                List<Crew> crewList = credits.getCrewList();
                crewList = (List<Crew>) getSubList(crewList, 6);
                List<Cast> castList = credits.getCastList();
                castList = (List<Cast>) getSubList(castList, 4);

                final List<CastRoom> castRoomList = new ArrayList<>();
                List<CrewRoom> crewRoomList = new ArrayList<>();
                for (final Cast cast : castList) {
                    String profileUrl = BASE_URL_PROFILE + cast.getProfilePath();
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(profileUrl)
                            .apply(new RequestOptions()
                                    .override(120, 180))
                            .into(new Target<Bitmap>() {
                                //create and insert cast into room
                                CastRoom castRoom = new CastRoom();

                                CastMovieRoom castMovieRoom = new CastMovieRoom();

                                @Override
                                public void onLoadStarted(@Nullable Drawable placeholder) {
                                    castRoom.setId(cast.getId());
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    castRoom.setProfile(null);
                                }

                                @Override
                                public void onResourceReady(@NonNull Bitmap resourceProfile, @Nullable Transition<? super Bitmap> transition) {
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    resourceProfile.compress(Bitmap.CompressFormat.PNG, 0, stream);
                                    profileByteArray = stream.toByteArray();
                                    castRoom.setProfile(profileByteArray);


                                    //create and insert cast-movie into room
                                    castMovieRoom.setCastId(cast.getId());
                                    castMovieRoom.setMovieId(movieId);
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            castDao.insert(castRoom);
                                            Log.d("TAG", castRoom.toString());

                                            castMovieRoomDao.insert(castMovieRoom);
                                            Log.d("TAG", castMovieRoom.toString());
                                            return null;
                                        }
                                    }.execute();
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }

                                @Override
                                public void getSize(@NonNull SizeReadyCallback cb) {

                                }

                                @Override
                                public void removeCallback(@NonNull SizeReadyCallback cb) {

                                }

                                @Override
                                public void setRequest(@Nullable Request request) {

                                }

                                @Nullable
                                @Override
                                public Request getRequest() {
                                    return null;
                                }

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onStop() {

                                }

                                @Override
                                public void onDestroy() {

                                }
                            });
                }

                for (Crew crew : crewList) {
                    if (crew != null) {
                        //create and insert crew into room
                        final CrewRoom crewRoom = new CrewRoom();
                        crewRoom.setId(crew.getId());
                        crewRoom.setName(crew.getName());
                        crewRoom.setDepartment(crew.getDepartment());
                        crewRoomList.add(crewRoom);

                        //create and insert crew-movie into room
                        final CrewMovieRoom crewMovieRoom = new CrewMovieRoom();
                        crewMovieRoom.setCrewId(crew.getId());
                        crewMovieRoom.setMovieId(movieId);

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                crewDao.insert(crewRoom);
                                Log.d("TAG", crewRoom.toString());
                                Log.d("TAG", crewMovieRoom.toString());
                                crewMovieRoomDao.insert(crewMovieRoom);
                                return null;
                            }
                        }.execute();
                    }
                }
            }


            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_main_addmovie);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivityContext, MovieAdderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setToolbar() {
        mainActivityContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Movies");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
            case R.id.option_bar:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onItemClick(View view, MovieRoom movie) {
        Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
        intent.putExtra(MOVIE_CLICKED, movie.getId());
        startActivity(intent);
    }
}
