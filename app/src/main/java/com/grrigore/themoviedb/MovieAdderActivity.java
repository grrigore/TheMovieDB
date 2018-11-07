package com.grrigore.themoviedb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.grrigore.themoviedb.data.MovieDao;
import com.grrigore.themoviedb.data.MovieDatabase;
import com.grrigore.themoviedb.data.MovieRoom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.grrigore.themoviedb.utils.Constants.PICK_IMAGE;

public class MovieAdderActivity extends AppCompatActivity {

    //todo refactor methods
    //todo refactor variable name
    private EditText titleEditText;
    private EditText overviewEditText;
    private EditText imdbIdEditText;

    private MovieRoom movie;
    private byte[] poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_adder);

        movie = new MovieRoom();

        titleEditText = findViewById(R.id.edittext_addmovie_title);
        overviewEditText = findViewById(R.id.edittext_addmovie_overview);
        imdbIdEditText = findViewById(R.id.edittext_addmovie_imdbid);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.adder_activity_title));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void selectFiles(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.poster_select_title)), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.poster_select_error), Toast.LENGTH_LONG).show();
            } else {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    poster = stream.toByteArray();
                    bitmap.recycle();

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.poster_select_success), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.poster_select_error), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void addMovie(View view) {
        movie.setTitle(titleEditText.getText().toString());
        movie.setOverview(overviewEditText.getText().toString());
        movie.setId(Integer.valueOf(imdbIdEditText.getText().toString()));
        movie.setPoster(poster);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MovieDao movieDao = MovieDatabase.getMovieDatabaseInsatnce(getApplicationContext()).getMovieDao();
                movieDao.insert(movie);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.add_movie_success), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }.execute();
    }
}
