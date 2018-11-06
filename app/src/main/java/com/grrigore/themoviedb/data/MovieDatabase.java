package com.grrigore.themoviedb.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import static com.grrigore.themoviedb.utils.Constants.DB_NAME;

@Database(entities = {MovieRoom.class, CastRoom.class, CrewRoom.class, CastMovieRoom.class, CrewMovieRoom.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static volatile MovieDatabase movieDatabaseInstance;

    public static synchronized MovieDatabase getMovieDatabaseInsatnce(Context context) {
        if (movieDatabaseInstance == null) {
            movieDatabaseInstance = create(context);
        }

        return movieDatabaseInstance;
    }

    private static MovieDatabase create(final Context context) {
        return Room.databaseBuilder(context,
                MovieDatabase.class,
                DB_NAME).build();
    }

    public abstract MovieDao getMovieDao();

    public abstract CrewDao getCrewDao();

    public abstract CastDao getCastDao();

    public abstract CastMovieRoomDao getCastMovieRoomDao();

    public abstract CrewMovieRoomDao getCrewMovieRoomDao();
}
