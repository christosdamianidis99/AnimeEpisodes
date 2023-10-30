package com.example.animeepisodes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AnimeEpisodes.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public static final String TABLE_NAME = "my_anime_episodes_db";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "anime_title";

    public static final String COLUMN_GENRES = "anime_genres";
    public static final String COLUMN_CONTENT = "anime_content";
    public static final String COLUMN_TOTALEPISODES = "anime_total_episode";
    public static final String COLUMN_IMAGE = "anime_image";
    public static final String COLUMN_EPISODE = "anime_episode";
    public static final String COLUMN_SEASON = "anime_season";

    public static final String TABLE_NAME_SEARCH = "search_anime_episodes_db";

    public static final String COLUMN_ID_SEARCH = "_id";
    public static final String COLUMN_TITLE_SEARCH = "anime_title";
    public static final String COLUMN_GENRES_SEARCH = "anime_genres";
    public static final String COLUMN_CONTENT_SEARCH = "anime_content";
    public static final String COLUMN_TOTALEPISODES_SEARCH = "anime_total_episode";
    public static final String COLUMN_IMAGE_SEARCH = "anime_image";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY , " +
                COLUMN_TITLE + " TEXT," +
                COLUMN_GENRES + " TEXT," +
                COLUMN_TOTALEPISODES+" TEXT," +
                COLUMN_CONTENT+" TEXT," +
                COLUMN_IMAGE+" TEXT," +
                COLUMN_SEASON + " INTEGER, " +
                COLUMN_EPISODE + " INTEGER);";

        String query_search = "CREATE TABLE " + TABLE_NAME_SEARCH +
                " (" + COLUMN_ID_SEARCH + " INTEGER PRIMARY KEY , " +
                COLUMN_TITLE_SEARCH + " TEXT," +
                COLUMN_GENRES_SEARCH + " TEXT," +
                COLUMN_CONTENT_SEARCH+" TEXT," +
                COLUMN_TOTALEPISODES_SEARCH+" TEXT," +
                COLUMN_IMAGE_SEARCH+" TEXT);";

        db.execSQL(query);
        db.execSQL(query_search);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SEARCH);
        onCreate(db);

    }
    public boolean isTableEmpty(String TABLENAME) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "+ TABLENAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count == 0;
    }
    void addAnime(String id,String title,String genre,String totalepisodes,String content,String season,String episode,String image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID,id);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_GENRES, genre);
        cv.put(COLUMN_TOTALEPISODES, totalepisodes);
        cv.put(COLUMN_CONTENT, content);
        cv.put(COLUMN_SEASON, season);
        cv.put(COLUMN_EPISODE, episode);
        cv.put(COLUMN_IMAGE, image);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Data Failed", Toast.LENGTH_SHORT).show();
        }
    }

    void addSearchAnime(String id,String title,String genre,String totalepisodes,String content,String image)
    {  SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID_SEARCH,id);
        cv.put(COLUMN_TITLE_SEARCH, title);
        cv.put(COLUMN_GENRES_SEARCH, genre);
        cv.put(COLUMN_TOTALEPISODES, totalepisodes);
        cv.put(COLUMN_CONTENT_SEARCH, content);
        cv.put(COLUMN_TOTALEPISODES_SEARCH, totalepisodes);
        cv.put(COLUMN_IMAGE_SEARCH, image);

        long result = db.insert(TABLE_NAME_SEARCH, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Data Failed", Toast.LENGTH_SHORT).show();
        }

    }
    Cursor readAllAnim()
    {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    Cursor readAllSearch()
    {
        String query = "SELECT * FROM " + TABLE_NAME_SEARCH;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    void updateAnime(String row_id,String title,int season, int episode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_SEASON, season);
        cv.put(COLUMN_EPISODE, episode);


        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
    }


    void updateSeason(String row_id, int season)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SEASON, season);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
    }

    void updateEpisode(String row_id,int episode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EPISODE, episode);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRowAnime(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted Successfully.", Toast.LENGTH_SHORT).show();
        }
    }
}
