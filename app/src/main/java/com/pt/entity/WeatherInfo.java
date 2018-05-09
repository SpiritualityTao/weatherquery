package com.pt.entity;

/**
 * Created by 韬 on 2016-05-24.
 * 天气信息实体类
 */
public class WeatherInfo {

    private String date;
    private String weatherDes;
    private int minTem;
    private int maxTem;
    private String wind;
    private String picId;
    private String picId2;

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "date='" + date + '\'' +
                ", weatherDes='" + weatherDes + '\'' +
                ", minTem=" + minTem +
                ", maxTem=" + maxTem +
                ", wind='" + wind + '\'' +
                ", picId='" + picId + '\'' +
                ", picId2='" + picId2 + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherDes() {
        return weatherDes;
    }

    public void setWeatherDes(String weatherDes) {
        this.weatherDes = weatherDes;
    }

    public int getMinTem() {
        return minTem;
    }

    public void setMinTem(int minTem) {
        this.minTem = minTem;
    }

    public int getMaxTem() {
        return maxTem;
    }

    public void setMaxTem(int maxTem) {
        this.maxTem = maxTem;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getPicId2() {
        return picId2;
    }

    public void setPicId2(String picId2) {
        this.picId2 = picId2;
    }
}
