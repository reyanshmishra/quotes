package com.motivational.quotes.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by reyansh on 12/21/17.
 */

public class SearchPhoto {
    @SerializedName("total")
    public int total;
    @SerializedName("total_pages")
    public int total_pages;

    @SerializedName("results")
    public ArrayList<Photo> results;


}
