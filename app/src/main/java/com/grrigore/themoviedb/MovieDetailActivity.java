package com.grrigore.themoviedb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.grrigore.themoviedb.adapter.CastAdapter;
import com.grrigore.themoviedb.adapter.CrewAdapter;
import com.grrigore.themoviedb.data.CastMovieRoomDao;
import com.grrigore.themoviedb.data.CastRoom;
import com.grrigore.themoviedb.data.CrewMovieRoomDao;
import com.grrigore.themoviedb.data.CrewRoom;
import com.grrigore.themoviedb.data.MovieDao;
import com.grrigore.themoviedb.data.MovieDatabase;
import com.grrigore.themoviedb.data.MovieRoom;

import java.util.ArrayList;
import java.util.List;

import static com.grrigore.themoviedb.utils.Constants.MOVIE_CLICKED;
import static com.grrigore.themoviedb.utils.Utils.formatFloat;
import static com.grrigore.themoviedb.utils.Utils.parseDate;
import static com.grrigore.themoviedb.utils.Utils.setProgressBarColor;

public class MovieDetailActivity extends AppCompatActivity {

    private Integer movieId;

    private ImageView coverImageView;
    private ImageView posterImageView;
    private TextView ratingTextView;
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView overviewTextView;
    private RecyclerView castRecyclerView;
    private RecyclerView crewRecyclerView;
    private CardView crewCardView;
    private CardView castCardView;
    private CircularProgressBar ratingProgressBar;
    private FrameLayout posterFrameLayout;

    private MovieDao movieDao;
    private CastMovieRoomDao castMovieRoomDao;
    private CrewMovieRoomDao crewMovieRoomDao;

    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            movieId = bundle.getInt(MOVIE_CLICKED);
        }

        coverImageView = findViewById(R.id.imageview_moviedetails_cover);
        posterImageView = findViewById(R.id.imageview_moviedetail_poster);
        ratingTextView = findViewById(R.id.textview_moviedetail_rating);
        titleTextView = findViewById(R.id.textView_moviedetail_title);
        releaseDateTextView = findViewById(R.id.textView_moviedetail_releasedate);
        overviewTextView = findViewById(R.id.textview_moviedetail_movieoverview);
        ratingProgressBar = findViewById(R.id.progresscircular_moviedetail_rating);
        posterFrameLayout = findViewById(R.id.framelayout_moviedetail_poster);
        crewCardView = findViewById(R.id.cardview_moviedetail_crew);
        castCardView = findViewById(R.id.cardview_moviedetail_cast);
        castRecyclerView = findViewById(R.id.recyclerview_moviedetail_cast);
        castRecyclerView.setHasFixedSize(true);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        castAdapter = new CastAdapter(new ArrayList<CastRoom>());
        castRecyclerView.setAdapter(castAdapter);


        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        crewRecyclerView = findViewById(R.id.recyclerview_moviedetail_crew);
        crewRecyclerView.setHasFixedSize(true);
        crewRecyclerView.setLayoutManager(gridLayoutManager);
        crewAdapter = new CrewAdapter(new ArrayList<CrewRoom>());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();
        posterFrameLayout.bringToFront();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        populateDatabase();
    }

    private void populateDatabase() {
        movieDao = MovieDatabase.getMovieDatabaseInsatnce(getApplicationContext()).getMovieDao();
        castMovieRoomDao = MovieDatabase.getMovieDatabaseInsatnce(getApplicationContext()).getCastMovieRoomDao();
        crewMovieRoomDao = MovieDatabase.getMovieDatabaseInsatnce(getApplicationContext()).getCrewMovieRoomDao();
        loadMovies();
    }

    private void loadMovies() {
        new AsyncTask<Void, Void, MovieRoom>() {
            @Override
            protected MovieRoom doInBackground(Void... voids) {
                return movieDao.getMovieById(movieId);
            }

            @Override
            protected void onPostExecute(MovieRoom movieRoom) {
                Log.d("TAG", movieRoom.toString());
                setMovieUi(movieRoom);
                loadCrewMembers();
            }
        }.execute();
    }

    private void loadCrewMembers() {
        new AsyncTask<Void, Void, List<CrewRoom>>() {
            @Override
            protected List<CrewRoom> doInBackground(Void... voids) {
                return crewMovieRoomDao.getCrewByMovieId(movieId);
            }

            @Override
            protected void onPostExecute(List<CrewRoom> crewRoom) {
                if (crewRoom.size() == 0) {
                    crewCardView.setVisibility(View.INVISIBLE);
                } else {
                    setCrewRecyclerViewUi(crewRoom);
                }
                loadCastMembers();
            }
        }.execute();
    }

    private void loadCastMembers() {
        new AsyncTask<Void, Void, List<CastRoom>>() {
            @Override
            protected List<CastRoom> doInBackground(Void... voids) {
                return castMovieRoomDao.getCastByMovieId(movieId);
            }

            @Override
            protected void onPostExecute(List<CastRoom> castRoom) {
                if (castRoom.size() == 0) {
                    castCardView.setVisibility(View.INVISIBLE);
                } else {
                    setCastRecyclerViewUi(castRoom);
                }
            }
        }.execute();
    }

    private void setMovieUi(MovieRoom movie) {
        int rating = 0;
        if (movie.getVoteAverage() != null) {
            rating = formatFloat(movie.getVoteAverage());
            if (rating == 0) {
                ratingTextView.setText(getResources().getString(R.string.not_rated));
            } else {
                ratingTextView.setText(String.valueOf(rating));
            }
            ratingProgressBar.setProgress(rating);
            ratingProgressBar.setForegroundStrokeColor(ContextCompat.getColor(getApplicationContext(), setProgressBarColor(rating)));
        } else {
            ratingTextView.setText(getResources().getString(R.string.not_rated));
            ratingProgressBar.setProgress(rating);
            ratingProgressBar.setForegroundStrokeColor(ContextCompat.getColor(getApplicationContext(), setProgressBarColor(rating)));
        }

        if (movie.getReleaseDate() != null) {
            releaseDateTextView.setText(parseDate(movie.getReleaseDate()));
        } else {
            releaseDateTextView.setText(getResources().getString(R.string.not_available));
        }
        if (movie.getTitle() != null) {
            titleTextView.setText(movie.getTitle());
        }
        if (movie.getPoster() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(movie.getPoster(), 0, movie.getPoster().length);
            posterImageView.setImageBitmap(bmp);
            coverImageView.setImageBitmap(bmp);
        }
        if (movie.getOverview() != null) {
            overviewTextView.setText(movie.getOverview());
        }

    }

    private void setCrewRecyclerViewUi(List<CrewRoom> crewRoom) {
        //todo better divier
        crewAdapter = new CrewAdapter(crewRoom);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider_horizontal);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider_verical);
        crewRecyclerView.addItemDecoration(new GridDividerItemDecoration(horizontalDivider, verticalDivider, 1));
        crewRecyclerView.setAdapter(crewAdapter);

    }

    private void setCastRecyclerViewUi(List<CastRoom> castRoom) {
        castAdapter = new CastAdapter(castRoom);

        castRecyclerView.setAdapter(castAdapter);
    }
}
