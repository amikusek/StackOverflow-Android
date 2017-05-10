package com.amikusek.stackoverflow.util.converter;

import android.text.format.DateUtils;

public class DateConverter {

    private static final String ZERO_MIN = "0 minutes ago";
    private static final String JUST_NOW = "just now";

    public static String getFormattedDateForTimestamp(long timestamp) {

        String date = DateUtils.getRelativeTimeSpanString(
                timestamp * 1000,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS).toString();

        return date.equals(ZERO_MIN) ? JUST_NOW : date;
    }
}
