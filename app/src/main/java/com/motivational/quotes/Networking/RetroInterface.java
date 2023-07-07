package com.motivational.quotes.Networking;

import com.motivational.quotes.Models.GoogleImages;
import com.motivational.quotes.Models.Photo;
import com.motivational.quotes.Models.PixabayModel;
import com.motivational.quotes.Models.Quote;
import com.motivational.quotes.Models.SinglePhoto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by REYANSH on 3/13/2017.
 */

public interface RetroInterface {

    @GET(Retro.Urls.photos)
    Call<ArrayList<Photo>> getPhotos(@Query("per_page") int perPage, @Query("page") int pageNumber);


    @GET(Retro.Urls.googleImageSeach)
    Call<GoogleImages> searchGoogle(@Query("q") String query,@Query("start")  int index);


    @GET(Retro.Urls.pixabayUrl)
    Call<PixabayModel> searchPixabay(@Query("q") String query, @Query("page")  int pageNumber,@Query("per_page") int perPage);


    @GET(Retro.Urls.randomPhoto)
    Call<Photo> randomPhoto();

    @GET
    Call<SinglePhoto> getPhotoById(@Url String url, @Query("client_id") String clientId);

    @GET(Retro.Urls.quotesUrl)
    Call<ArrayList<Quote>> getQuotes();

    @GET(Retro.Urls.searchPhotos)
    Call<Photo> searchPhotos(@Query("client_id") String clientId, @Query("query") String query, @Query("page") int page, @Query("per_page") int perPage);


}
