package com.motivational.quotes.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by reyansh on 2/3/18.
 */

public class GoogleImages {


    @SerializedName("items")
    public List<Items> mItems;

    public static class Items {
        @SerializedName("pagemap")
        public PageMap mPageMap;
    }

    public static class PageMap {
        @SerializedName("cse_image")
        public List<CseImage> mCseImage;

        @SerializedName("cse_thumbnail")
        public List<Csethumbnail> mThumnail;
    }


    public static class Csethumbnail {
        @SerializedName("src")
        public String mSrc;

    }

    public static class CseImage {
        @SerializedName("src")
        public String mSrc;

    }

    @SerializedName("queries")
    public Queries mQueries;

    public static class Queries {
        @SerializedName("nextPage")
        public List<NextPage> mNextPages;

    }

    public static class NextPage {

    }


}
