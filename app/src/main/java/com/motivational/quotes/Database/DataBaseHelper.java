package com.motivational.quotes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.motivational.quotes.Models.Photo;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Logger;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyQuote.db";
    public static final int DATABASE_VERSION = 1;
    public static final String FAVORITES_PHOTO_TABLE = "FavoritesPhotosTable";
    public static final String _ID = "_id";
    public static final String PHOTO_ID = "photoId";
    public static final String PHOTO_THUMB = "photoThumb";
    public static final String FAVORITES_TABLE = "FavoritesTable";
    private static DataBaseHelper mDatabaseHelper;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static synchronized DataBaseHelper getDatabaseHelper(Context context) {
        if (mDatabaseHelper == null)
            mDatabaseHelper = new DataBaseHelper(context.getApplicationContext());
        return mDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        //Equalizer table.
        String[] favoritesTableCols = {PHOTO_ID, PHOTO_THUMB,};

        String[] favoritesTableColTypes = {
                "TEXT", "TEXT"};

        String createFavoritesTable = buildCreateStatement(FAVORITES_TABLE,
                favoritesTableCols,
                favoritesTableColTypes);

        db.execSQL(createFavoritesTable);

        Logger.log("EQ TABLE CREATED");

    }


    public ArrayList<Photo> getFavoritePhotos() {
        ArrayList<Photo> photos = new ArrayList<>();
        Cursor cursor = getDatabase().rawQuery("SELECT * FROM " + FAVORITES_TABLE + " limit 40", null);
        if (cursor.moveToFirst()) {
            do {
                Photo photo = new Photo();
                photo.id = cursor.getString(cursor.getColumnIndex(PHOTO_ID));

                Photo.Urls urls = new Photo.Urls();
                urls.thumb = cursor.getString(cursor.getColumnIndex(PHOTO_THUMB));
                photo.urls = urls;


                photos.add(photo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return photos;
    }


    public void addToFavorites(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(PHOTO_ID, photo.id);
        values.put(PHOTO_THUMB, photo.urls.thumb);


        if (!isAlreadyInFavorites(photo.id)) {
            getDatabase().insertOrThrow(FAVORITES_TABLE, null, values);
            Toast.makeText(mContext, R.string.photo_added_to_favorites, Toast.LENGTH_SHORT).show();
        } else {
            getDatabase().delete(FAVORITES_TABLE, PHOTO_ID + "= " + photo.id, null);
            Toast.makeText(mContext, R.string.photo_removed_from_favorites, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Add songs to favorites table if it is already there remove it.
     */
    private boolean isAlreadyInFavorites(String photoId) {
        String rawQuery = "SELECT DISTINCT " + PHOTO_ID + " FROM " + FAVORITES_TABLE + " WHERE " + PHOTO_ID + "=" + "'"
                + photoId + "'";
        Cursor cursor = getDatabase().rawQuery(rawQuery, null);
        return !(cursor != null && cursor.getCount() == 0);
    }


    /**
     * Constructs a fully formed CREATE statement using the input
     * parameters.
     */

    private String buildCreateStatement(String tableName, String[] columnNames, String[] columnTypes) {
        String createStatement = "";
        if (columnNames.length == columnTypes.length) {
            createStatement += "CREATE TABLE IF NOT EXISTS " + tableName + "("
                    +
                    _ID + " INTEGER PRIMARY KEY, ";

            for (int i = 0; i < columnNames.length; i++) {

                if (i == columnNames.length - 1) {
                    createStatement += columnNames[i]
                            + " "
                            + columnTypes[i]
                            + ")";
                } else {
                    createStatement += columnNames[i]
                            + " "
                            + columnTypes[i]
                            + ", ";
                }
            }
        }
        return createStatement;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    protected void finalize() {
        try {
            getDatabase().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized SQLiteDatabase getDatabase() {
        if (mDatabase == null)
            mDatabase = getWritableDatabase();
        return mDatabase;
    }

}
