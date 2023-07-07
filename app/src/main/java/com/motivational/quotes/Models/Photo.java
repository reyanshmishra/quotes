package com.motivational.quotes.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reyansh on 12/9/17.
 */

public class Photo {


    @SerializedName("total")
    public int total;
    @SerializedName("total_pages")
    public int total_pages;

    @SerializedName("results")
    public ArrayList<Photo> results;

    @SerializedName("id")
    public String id;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
    @SerializedName("width")
    public int width;
    @SerializedName("height")
    public int height;
    @SerializedName("color")
    public String color;
    @SerializedName("description")
    public String description;
    @SerializedName("urls")
    public Urls urls;
    @SerializedName("categories")
    public List<Categories> categories;
    @SerializedName("links")
    public Links links;
    @SerializedName("liked_by_user")
    public boolean liked_by_user;
    @SerializedName("likes")
    public int likes;
    @SerializedName("user")
    public User user;
    @SerializedName("current_user_collections")
    public List<Current_user_collections> current_user_collections;
    String s;

    public static class Urls {
        @SerializedName("raw")
        public String raw;
        @SerializedName("full")
        public String full;
        @SerializedName("regular")
        public String regular;
        @SerializedName("small")
        public String small;
        @SerializedName("thumb")
        public String thumb;
    }

    public static class Categories {
    }

    public static class Links {
        @SerializedName("self")
        public String self;
        @SerializedName("html")
        public String html;
        @SerializedName("download")
        public String download;
        @SerializedName("download_location")
        public String download_location;
    }

    public static class UserLinks {
        @SerializedName("self")
        public String self;
        @SerializedName("html")
        public String html;
        @SerializedName("photos")
        public String photos;
        @SerializedName("likes")
        public String likes;
        @SerializedName("portfolio")
        public String portfolio;
        @SerializedName("following")
        public String following;
        @SerializedName("followers")
        public String followers;
    }

    public static class Profile_image {
        @SerializedName("small")
        public String small;
        @SerializedName("medium")
        public String medium;
        @SerializedName("large")
        public String large;
    }

    public static class User {
        @SerializedName("id")
        public String id;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("username")
        public String username;
        @SerializedName("name")
        public String name;
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("twitter_username")
        public String twitter_username;
        @SerializedName("portfolio_url")
        public String portfolio_url;
        @SerializedName("bio")
        public String bio;
        @SerializedName("location")
        public String location;
        @SerializedName("links")
        public UserLinks links;
        @SerializedName("profile_image")
        public Profile_image profile_image;
        @SerializedName("total_likes")
        public int total_likes;
        @SerializedName("total_photos")
        public int total_photos;
        @SerializedName("total_collections")
        public int total_collections;
    }

    public static class Current_user_collections {
    }
}
