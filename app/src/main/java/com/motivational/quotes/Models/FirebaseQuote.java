package com.motivational.quotes.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebaseQuote {
    public ArrayList<String> quote;

    public FirebaseQuote() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("quote", quote);
        return result;
    }

}