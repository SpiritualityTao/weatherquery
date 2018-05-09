package com.pt.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.pt.entity.CityInfo;
import com.pt.entity.WeatherInfo;
import com.pt.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 韬 on 2016-05-30.
 * 数据增删改查Dao类
 */
public class WeatherDao {


    private static final String TAG = "WeatherDao";
    private static final String DB_NAME = "weather.db";
    private static final int DB_VERSION = 1;
    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper dbOpenHelper;

    public WeatherDao(Context context) {
        this.context = context;
    }
    //打开
    public void open() throws SQLiteException {
        dbOpenHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbOpenHelper.getWritableDatabase();
        }catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }
    //关闭
    public void close(){
        if(db != null){
            db.close();
            db = null;
        }
    }

    //添加省份名
    public void addProvince(List<String> province_datas){
        ContentValues values ;
        for (String province : province_datas){
            values = new ContentValues();

            values.put("province",province);
            db.insert(DatabaseHelper.PROVINCE_TABLE,null,values);
        }
    }

    //查询所有的省份信息
    public List<String> showProvince(){
        Cursor cursor = db.query(DatabaseHelper.PROVINCE_TABLE,new String[]{"province"},null,null,null,null,"_id asc");
        List<String> province_datas = new ArrayList<>();
        cursor.moveToFirst();
        int count = cursor.getCount();
        if(count == 0){
            return null;
        }else{
            for (int i = 0; i < count; i++) {
                province_datas.add(cursor.getString(0));
                cursor.moveToNext();
            }
            return province_datas;
        }

    }

    //添加对应省份的城市信息
    public  void addcityName(String province,List<String> cityNames){
        ContentValues values;
        for (String cityName:cityNames){
            values = new ContentValues();
            values.put("province",province);
            values.put("cityName",cityName);
            db.insert(DatabaseHelper.CITY_NAME,null,values);
        }
    }

    //查询对应省份的城市名列表
    public List<String> showCityByProvince(String province){
        List<String> city_datas = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.CITY_NAME,new String[]{"cityName"},"province=?",new String[]{province},null,null,"_id asc");
        cursor.moveToFirst();
        int count = cursor.getCount();
        if(count == 0){
            return null;
        }else {
            for (int i = 0; i < count; i++) {
                city_datas.add(cursor.getString(0));
                cursor.moveToNext();
            }
            return city_datas;
        }


    }



    //添加城市信息
    public long addCityInfo(CityInfo cityInfo){
        //插入的CityInfo转换为ContentValues
        ContentValues values = new ContentValues();
        values.put("province",cityInfo.getProvince());
        values.put("cityName",cityInfo.getCityName());
        values.put("todayInfo",cityInfo.getTodayInfo());
        values.put("time",cityInfo.getTime());
        values.put("aveMin",cityInfo.getAveMin());
        values.put("aveMax",cityInfo.getAveMax());
        values.put("airQuality",cityInfo.getAirQuality());
        addWeatherInfos(cityInfo.getWeatherInfos(),cityInfo.getCityName());
        return db.insert(DatabaseHelper.CITY_TABLE,null,values);
    }

    //添加城市天气信息
    public void  addWeatherInfos(List<WeatherInfo> weatherInfos,String cityName){
        Log.i(TAG, "addWeatherInfos: " + weatherInfos.toString());
        ContentValues values;
        for (int i = 0; i < weatherInfos.size(); i++) {
            values = new ContentValues();
            values.put("cityName",cityName);
            values.put("date",weatherInfos.get(i).getDate());
            values.put("weatherDes",weatherInfos.get(i).getWeatherDes());
            values.put("minTem",weatherInfos.get(i).getMinTem());
            values.put("maxTem",weatherInfos.get(i).getMaxTem());
            values.put("wind",weatherInfos.get(i).getWind());
            values.put("picId",weatherInfos.get(i).getPicId());
            values.put("picId2",weatherInfos.get(i).getPicId2());
            db.insert(DatabaseHelper.WEATHER_TABLE,null,values);
        }
    }

    //根据城市删除城市信息和天气表
    public void deleteDataByCityName(String cityName){
        db.delete(DatabaseHelper.WEATHER_TABLE,"cityName=?",new String[]{cityName});
        db.delete(DatabaseHelper.CITY_TABLE,"cityName=?",new String[]{cityName});
    }

    //删除表中所有的数据
    public void deleteAllDatas(){
        db.delete(DatabaseHelper.CITY_TABLE,null,null);
        db.delete(DatabaseHelper.WEATHER_TABLE,null,null);
        db.delete(DatabaseHelper.PROVINCE_TABLE,null,null);
        db.delete(DatabaseHelper.CITY_NAME,null,null);
    }

    //通过城市名查询城市信息，其中要在两张表中查找
    public CityInfo showCityInfo(String cityName){
        Cursor cityCursor = db.query(DatabaseHelper.CITY_TABLE,new String[]
                {"province","todayInfo","time","aveMax","aveMin","airQuality"},
                "cityName=?",new String[]{cityName},null,null,null);
        Cursor weatherCursor = db.query(DatabaseHelper.WEATHER_TABLE,new String[]
                {"date","weatherDes","maxTem","minTem","wind","picId","picId2"},
                "cityName=?",new String[]{cityName},null,null,null);
        return convertToCityInfo(cityCursor,weatherCursor,cityName);
    }

    private CityInfo convertToCityInfo(Cursor cityCursor, Cursor weatherCursor, String cityName) {
        CityInfo cityInfo = new CityInfo();
        List<WeatherInfo> weatherInfos = new ArrayList<>();
        int cityCount = cityCursor.getCount();

        if(cityCount == 0){
            return null;
        }else {
            cityCursor.moveToFirst();
            for (int i = 0; i < cityCount; i++) {
                cityInfo.setProvince(cityCursor.getString(0));
                cityInfo.setCityName(cityName);
                cityInfo.setTodayInfo(cityCursor.getString(1));
                cityInfo.setTime(cityCursor.getString(2));
                cityInfo.setAveMax(cityCursor.getInt(3));
                cityInfo.setAveMin(cityCursor.getInt(4));
                cityInfo.setAirQuality(cityCursor.getString(5));
            }

            int weatherCount = weatherCursor.getCount();
            weatherCursor.moveToFirst();
            WeatherInfo weatherInfo = null;
            for (int i = 0; i < weatherCount; i++) {
                weatherInfo = new WeatherInfo();
                weatherInfo.setDate(weatherCursor.getString(0));
                weatherInfo.setWeatherDes(weatherCursor.getString(1));
                weatherInfo.setMaxTem(weatherCursor.getInt(2));
                weatherInfo.setMinTem(weatherCursor.getInt(3));
                weatherInfo.setWind(weatherCursor.getString(4));
                weatherInfo.setPicId(weatherCursor.getString(5));
                weatherInfo.setPicId2(weatherCursor.getString(6));
                weatherInfos.add(weatherInfo);
                Log.i(TAG, "convertToCityInfo: " + weatherInfo.toString());
                weatherCursor.moveToNext();
            }
            cityInfo.setWeatherInfos(weatherInfos);
            return cityInfo;
        }
    }

    public void deleteProvince() {
        db.delete(DatabaseHelper.PROVINCE_TABLE,null,null);
    }

    public  void deleteCityByProvince(String province){
        db.delete(DatabaseHelper.CITY_NAME,"province=?",new String[]{province});
    }

}
