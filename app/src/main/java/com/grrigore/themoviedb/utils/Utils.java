package com.grrigore.themoviedb.utils;

import android.content.Context;

import com.grrigore.themoviedb.R;

import java.io.File;
import java.util.List;

public class Utils {

    public static int formatFloat(float number) {
        number *= 10;
        return (int) number;
    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public static int setProgressBarColor(int rating) {
        if (rating != 0 && rating < 40) {
            return R.color.under40rating;
        } else if (rating >= 40 && rating < 70) {
            return R.color.under70rating;
        } else if (rating >= 70) {
            return R.color.over70rating;
        }

        return R.color.over70rating;
    }

    public static List<?> getSubList(List<?> list, int numberOfElements) {
        if (list.size() >= numberOfElements)
            return list.subList(0, numberOfElements);
        return list;
    }
}
