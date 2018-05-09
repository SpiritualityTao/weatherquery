package com.pt.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 韬 on 2016-05-30.
 * 数据库
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    
    public static final String CITY_TABLE = "cityInfo";
    public static final String WEATHER_TABLE = "weatherInfo";
    public static final String PROVINCE_TABLE = "province";
    public static final String CITY_NAME = "cityName";

    private static final String KEY_ID = "_id";

    private static final String PROVINCE_SQL= "create table " + PROVINCE_TABLE +
            " (" + KEY_ID + " integer primary key autoincrement, " +
            "province text not null)";

    private static final String CITY_SQL = "create table " + CITY_NAME +
            " (" + KEY_ID + " integer primary key autoincrement, " +
            "province text not null," +
            "cityName text not null)";

    private static  final String CITY_CREATE = "create table " + CITY_TABLE +
            " (" + KEY_ID + " integer primary key autoincrement, " +
            "province text not null," +
            "cityName text not null," +
            "todayInfo text not null," +
            "time text not null," +
            "aveMax integer," +
            "aveMin integer," +
            "airQuality text not null)";
    private static final String WEATHER_CREATE = "create table " +  WEATHER_TABLE +
            " (" + KEY_ID + " integer primary key autoincrement, " +
            "cityName text not null," +
            "date text not null," +
            "weatherDes text not null," +
            "maxTem integer," +
            "minTem integer," +
            "wind text not null," +
            "picId text not null," +
            "picId2 text not null)";
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: " + PROVINCE_SQL);
        db.execSQL(PROVINCE_SQL);
        db.execSQL(CITY_SQL);
        db.execSQL(CITY_CREATE);
        db.execSQL(WEATHER_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROVINCE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CITY_NAME);
        onCreate(db);
    }
}
