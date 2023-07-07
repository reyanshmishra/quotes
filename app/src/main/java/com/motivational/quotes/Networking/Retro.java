package com.motivational.quotes.Networking;

import com.motivational.quotes.Utils.Common;
import com.motivational.quotes.Utils.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Retro {

    static int cacheSize = 5 * 1024 * 1024; // 5 MB
    static Cache cache = new Cache(Common.getContext().getCacheDir(), cacheSize);

    private static Retrofit retrofit = null;


    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 10)
                    .build();
        } else {
            return originalResponse;
        }
    };

    private static final Interceptor OFFLINE_INTERCEPTOR = chain -> {
        Request request = chain.request();

        if (!Common.isNetworkAvailable()) {
            Logger.log("rewriting request");

            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        return chain.proceed(request);
    };


    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
            .addInterceptor(OFFLINE_INTERCEPTOR)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .cache(cache)
            .build();


    public static Retrofit getRetro() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Urls.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface Urls {
        String clientId = "dc95b0bfd0cd6f751b55d97ba4aa4f9af6380f4ac16dd926422c9eea6fcea122";
        String baseUrl = "https://api.unsplash.com/";

        String photos = "photos/?client_id=" + clientId;
        String pixabayApiKey = "8322358-bb32ad1dff53451623d1c27f8";
        String pixabayUrl = "https://pixabay.com/api/?key=8322358-bb32ad1dff53451623d1c27f8";

        String googleImageSeach = "https://www.googleapis.com/customsearch/v1?key=AIzaSyBneOFjDKWPflQ1vGm-l8PtnywHLUoMSfc&cx=017043316583969756526:zu5e_iracdy&&searchtype=image";


        String photoById = "photos/";
        String searchPhotos = baseUrl + "search/photos";
        String randomPhoto = "photos/random/?client_id=" + clientId;

        String itunesUrl = "http://itunes.apple.com/search?";
        String quotesUrl = "https://talaikis.com/api/quotes/";
    }


}