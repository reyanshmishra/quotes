package com.motivational.quotes.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by reyansh on 12/10/17.
 */

public class Quote {


    @SerializedName("quote")
    public String quote;
    @SerializedName("author")
    public String author;
    @SerializedName("cat")
    public String cat;
}
