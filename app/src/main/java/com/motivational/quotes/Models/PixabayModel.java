package com.motivational.quotes.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by reyansh on 3/11/18.
 */

public class PixabayModel {

    @SerializedName("total")
    public int total;
    @SerializedName("hits")
    public List<Hits> hits;
    @SerializedName("totalHits")
    public int totalhits;

    public static class Hits {
        @SerializedName("imageHeight")
        public int imageheight;
        @SerializedName("userImageURL")
        public String userimageurl;
        @SerializedName("id")
        public int id;
        @SerializedName("type")
        public String type;
        @SerializedName("user")
        public String user;
        @SerializedName("user_id")
        public int userId;
        @SerializedName("imageWidth")
        public int imagewidth;
        @SerializedName("webformatURL")
        public String webformaturl;
        @SerializedName("previewURL")
        public String previewurl;
        @SerializedName("pageURL")
        public String pageurl;
        @SerializedName("downloads")
        public int downloads;
        @SerializedName("comments")
        public int comments;
        @SerializedName("previewWidth")
        public int previewwidth;
        @SerializedName("webformatWidth")
        public int webformatwidth;
        @SerializedName("views")
        public int views;
        @SerializedName("webformatHeight")
        public int webformatheight;
        @SerializedName("tags")
        public String tags;
        @SerializedName("favorites")
        public int favorites;
        @SerializedName("likes")
        public int likes;
        @SerializedName("previewHeight")
        public int previewheight;
    }

}
