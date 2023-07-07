package com.motivational.quotes.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by reyansh on 12/11/17.
 */

public class SinglePhoto {

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
    @SerializedName("slug")
    public String slug;
    @SerializedName("exif")
    public Exif exif;
    @SerializedName("views")
    public int views;
    @SerializedName("downloads")
    public int downloads;

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
        @SerializedName("custom")
        public String custom;

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

    public static class Exif {
        @SerializedName("make")
        public String make;
        @SerializedName("model")
        public String model;
        @SerializedName("exposure_time")
        public String exposure_time;
        @SerializedName("aperture")
        public String aperture;
        @SerializedName("focal_length")
        public String focal_length;
        @SerializedName("iso")
        public String iso;
    }
}
