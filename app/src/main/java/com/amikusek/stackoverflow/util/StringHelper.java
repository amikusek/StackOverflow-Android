package com.amikusek.stackoverflow.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public class StringHelper {

    public static Spanned fromHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }
}
