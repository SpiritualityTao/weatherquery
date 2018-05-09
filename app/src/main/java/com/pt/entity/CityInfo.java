package com.pt.entity;

import java.util.List;

/**
 * Created by 韬 on 2016-05-24.
 * 城市信息实体类
 */
public class CityInfo {

    private String province;
    private String cityName;
    private String todayInfo;
    private String time;
    private List<WeatherInfo> WeatherInfos;    //5天的天气信息
    private int aveMax;             //5天内的最高平均气温
    private int aveMin;             //5天内的最低平均气温
    private String airQuality;

    @Override
    public String toString() {
        return "CityInfo{" +
                "province='" + province + '\'' +
                ", cityName='" + cityName + '\'' +
                ", todayInfo='" + todayInfo + '\'' +
                ", WeatherInfos=" + WeatherInfos +
                ", aveMax=" + aveMax +
                ", aveMin=" + aveMin +
                ", airQuality='" + airQuality + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public String getTodayInfo() {
        return todayInfo;
    }

    public void setTodayInfo(String todayInfo) {
        this.todayInfo = todayInfo;
    }

    public int getAveMax() {
        return aveMax;
    }

    public void setAveMax(int aveMax) {
        this.aveMax = aveMax;
    }

    public int getAveMin() {
        return aveMin;
    }

    public void setAveMin(int aveMin) {
        this.aveMin = aveMin;
    }

    public List<WeatherInfo> getWeatherInfos() {
        return WeatherInfos;
    }

    public void setWeatherInfos(List<WeatherInfo> weatherInfos) {
        WeatherInfos = weatherInfos;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
