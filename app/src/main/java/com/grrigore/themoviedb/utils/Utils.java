package com.grrigore.themoviedb.utils;

import android.content.Context;

import com.grrigore.themoviedb.R;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
        if (rating > 0 && rating < 40) {
            return R.color.under40rating;
        } else if (rating >= 40 && rating < 70) {
            return R.color.under70rating;
        } else if (rating >= 70) {
            return R.color.over70rating;
        }

        return R.color.not_rated;
    }

    public static List<?> getSubList(List<?> list, int numberOfElements) {
        if (list.size() >= numberOfElements)
            return list.subList(0, numberOfElements);
        return list;
    }

    public static String parseDate(String date) {
        String datePattern = "dd MMMM, yyyy";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
            return localDate.format(dateTimeFormatter);
        } else {
            Date date1 = null;
            DateFormat outputFormatter = null;
            try {
                DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
                date1 = inputFormatter.parse(date);
                outputFormatter = new SimpleDateFormat(datePattern);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return outputFormatter.format(date1);
        }
    }
}
