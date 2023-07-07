package com.motivational.quotes.Utils;

import android.graphics.Typeface;

import java.util.Hashtable;

//Caches the custom fonts in memory to improve rendering performance.
public class TypefaceHelper {

    public static final String WARSAWGOTHIC = "warsaw.otf";

    public static final String TYPEFACE_FOLDER = "fonts";
    private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<>(4);

    public static Typeface getTypeface(String fileName) {
        Typeface tempTypeface = sTypeFaces.get(fileName);

        if (tempTypeface == null) {
            String fontPath = new StringBuilder(TYPEFACE_FOLDER).append('/')
                    .append(fileName)
                    .toString();

            tempTypeface = Typeface.createFromAsset(Common.getContext().getAssets(), fontPath);
            sTypeFaces.put(fileName, tempTypeface);
        }

        return tempTypeface;
    }

}
