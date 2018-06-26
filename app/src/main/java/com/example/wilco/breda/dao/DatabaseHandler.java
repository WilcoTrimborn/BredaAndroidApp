package com.example.wilco.breda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wilco.breda.domain.BredaPhoto;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Wilco on 25-6-2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "BredaPhotos";

    private final String TABLE_PHOTOS = "TABLE_Photos";
    private final String[] TABLES = {this.TABLE_PHOTOS};

    private static final String KEY_ID = "id";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_MATERIAL = "material";
    private static final String KEY_UNDERGROUND = "underground";
    private static final String KEY_PHOTOURL = "photo_url";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creates database if nonexistent.
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String table : this.TABLES) {
            String CREATE_TABLE = "CREATE TABLE " + table + "(" + this.KEY_ID + " TEXT PRIMARY KEY," + this.KEY_LOCATION + " TEXT," + this.KEY_ARTIST + " TEXT,"
                    + this.KEY_DESCRIPTION + " TEXT," + this.KEY_MATERIAL + " TEXT," + this.KEY_UNDERGROUND + " TEXT," + this.KEY_PHOTOURL + " TEXT" + ")";
            db.execSQL(CREATE_TABLE);
        }
    }

    //Deletes and remakes the database if it gets upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String table : this.TABLES) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        this.onCreate(db);
    }

    public void addPhoto(BredaPhoto bredaPhoto) {
        SQLiteDatabase db = this.getWritableDatabase(); //Kinda makes database connection, needs to be writable

        ContentValues values = new ContentValues();
        values.put(this.KEY_ID, bredaPhoto.getPhotoID());
        values.put(this.KEY_LOCATION, bredaPhoto.getLocation());
        values.put(this.KEY_ARTIST, bredaPhoto.getArtist());
        values.put(this.KEY_DESCRIPTION, bredaPhoto.getDescription());
        values.put(this.KEY_MATERIAL, bredaPhoto.getMaterial());
        values.put(this.KEY_UNDERGROUND, bredaPhoto.getUnderground());
        values.put(this.KEY_PHOTOURL, bredaPhoto.getPhotoUrl());
        String tableName = "photos";
        if(Arrays.asList(this.TABLES).contains(tableName)) {
            db.insert(tableName, null, values);
            Log.i("DBAddPhoto", "Added new photo to table " + tableName);
        } else {
            Log.i("DBAddPhoto", "Could not find table " + tableName);
        }
        db.close();
    }

    //Gets one photo
    public BredaPhoto getPhoto(String id, String cameraName) {
        SQLiteDatabase db = this.getReadableDatabase(); //Kinda makes database connection, don't need to write
        String tableName = "photos" + cameraName.toUpperCase();
        if(!Arrays.asList(this.TABLES).contains(tableName)) {
            return null;
        }
        Cursor cursor = db.query(tableName, new String[] {this.KEY_ID, this.KEY_LOCATION, this.KEY_ARTIST, this.KEY_DESCRIPTION, this.KEY_MATERIAL, this.KEY_UNDERGROUND, this.KEY_PHOTOURL}, KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        BredaPhoto nf = new BredaPhoto(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));

        cursor.close();
        db.close();
        return nf;

    }

    //Gets all photos.
    public ArrayList<BredaPhoto> getAllPhotos(String camera) {
        ArrayList<BredaPhoto> photos = new ArrayList<>();

        String tableName = "photos" + camera.toUpperCase();
        if(!Arrays.asList(this.TABLES).contains(tableName)) {
            return null;
        }

        String selectQuery = "SELECT * FROM " + tableName;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                String photoId = cursor.getString(0);
                String location = cursor.getString(1);
                String artist = cursor.getString(2);
                String description = cursor.getString(3);
                String material = cursor.getString(4);
                String underground = cursor.getString(5);
                String photoUrl = cursor.getString(6);
                BredaPhoto nf = new BredaPhoto(photoId, location, artist, description, material, underground, photoUrl);
                photos.add(nf);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return photos;
    }

    //Gives amount of photos.
    public int getPhotosCount(String camera) {
        String tableName = "photos" + camera.toUpperCase();
        if(!Arrays.asList(this.TABLES).contains(tableName)) {
            Log.i("getPhotosCount", "Wrong Table name");
            return 0;
        }
        String countQuery = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        Log.i("getPhotosCount", "Counted " + count + " photos");

        db.close();
        return count;
    }

    //Clears the table with photos.
    public void clearTable(String camera) {
        String tableName = "photos" + camera.toUpperCase();
        if (!Arrays.asList(this.TABLES).contains(tableName)) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tableName);

        db.close();
    }
}
