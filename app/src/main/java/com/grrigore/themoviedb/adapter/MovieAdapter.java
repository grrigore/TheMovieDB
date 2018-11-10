package com.grrigore.themoviedb.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.grrigore.themoviedb.R;
import com.grrigore.themoviedb.data.MovieRoom;

import java.util.List;

import static com.grrigore.themoviedb.utils.Utils.formatFloat;
import static com.grrigore.themoviedb.utils.Utils.parseDate;
import static com.grrigore.themoviedb.utils.Utils.setProgressBarColor;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieRoom> movies;
    private ItemClickListener itemClickListener;
    private Context context;

    public MovieAdapter(List<MovieRoom> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    public void setMovies(List<MovieRoom> movies) {
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, viewGroup, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        MovieRoom movie = movies.get(i);
        int rating = 0;
        if (movie.getVoteAverage() != null) {
            rating = formatFloat(movie.getVoteAverage());
            if (rating == 0) {
                movieViewHolder.ratingTextView.setText(context.getResources().getString(R.string.not_rated));
            } else {
                movieViewHolder.ratingTextView.setText(String.valueOf(rating));
            }
            movieViewHolder.circularProgressBar.setProgress(rating);
            movieViewHolder.circularProgressBar.setForegroundStrokeColor(ContextCompat.getColor(context, setProgressBarColor(rating)));
        } else {
            movieViewHolder.ratingTextView.setText(context.getResources().getString(R.string.not_rated));
            movieViewHolder.circularProgressBar.setProgress(rating);
            movieViewHolder.circularProgressBar.setForegroundStrokeColor(ContextCompat.getColor(context, setProgressBarColor(rating)));
        }
        if (movie.getReleaseDate() != null) {
            movieViewHolder.releaseDateTextView.setText(parseDate(movie.getReleaseDate()));
        } else {
            movieViewHolder.releaseDateTextView.setText(context.getResources().getString(R.string.not_available));
        }
        if (movie.getTitle() != null) {
            movieViewHolder.titleTextView.setText(movie.getTitle());
        }
        if (movie.getPoster() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(movie.getPoster(), 0, movie.getPoster().length);
            movieViewHolder.posterImageView.setImageBitmap(bmp);
        } else {
            movieViewHolder.posterImageView.setImageResource(R.drawable.ic_broken_image_black_50dp);
        }
        if (movie.getOverview() != null) {
            movieViewHolder.descriptionTextView.setText(movie.getOverview());
        }
    }


    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        } else {
            return movies.size();
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, MovieRoom movie);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView descriptionTextView;
        private TextView titleTextView;
        private TextView releaseDateTextView;
        private TextView ratingTextView;
        private CircularProgressBar circularProgressBar;
        private ImageView posterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.textview_main_description);
            titleTextView = itemView.findViewById(R.id.textView_main_title);
            releaseDateTextView = itemView.findViewById(R.id.textView_main_releasedate);
            ratingTextView = itemView.findViewById(R.id.textview_main_rating);
            posterImageView = itemView.findViewById(R.id.imageview_main_poster);
            circularProgressBar = itemView.findViewById(R.id.progresscircular_main_rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, movies.get(getAdapterPosition()));
            }
        }
    }
}
