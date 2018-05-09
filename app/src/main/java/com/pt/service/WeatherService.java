package com.pt.service;

import android.util.Log;

import com.pt.entity.CityInfo;
import com.pt.entity.WeatherInfo;
import com.pt.utils.WebServiceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 韬 on 2016-05-24.
 */
public class WeatherService {

    private static final String TAG = "WeatherService";
    
    /**
     * 得带省份中国省名列表
     * @return
     */
    public  static List<String> getProvinceList( ){
        //调用的得到省份的方法名
        String methodName = "getRegionProvince";
        SoapObject soapObject = WebServiceUtils.invokingMethod(methodName,null);
        return parseProvinceOrCity(soapObject);
    }

    /**
     * 得到城市名列表
     * @param province  省份
     * @return
     */
    public  static List<String> getCityList(String province){
        //调用得到城市的方法名
        String methodName = "getSupportCityString";
        SoapObject soapObject = WebServiceUtils.invokingMethod(methodName,province);
        return  parseProvinceOrCity(soapObject);
    }

    /**
     * 得到天气
     * @param city 城市名
     * @return
     */
    public static CityInfo getWeather(String city){
        //得到天气的方法名
        String methodName = "getWeather";
        SoapObject soapObject = WebServiceUtils.invokingMethod(methodName,city);
        Log.i(TAG, "getWeather: " + soapObject.toString());
        return parseCityWeather(soapObject);
    }

    /**
     * 通过webservice传递过来的SoapObject解析成城市的相关天气信息
     * @param soapObject
     * @return
     */
    private static CityInfo parseCityWeather(SoapObject soapObject) {
        CityInfo cityInfo = new CityInfo();
        int aveMax = 0,aveMin = 0;
        List<WeatherInfo> weatherInfos = new ArrayList<>();
        String province = soapObject.getProperty(0).toString();
        String cityName = soapObject.getProperty(1).toString();
        String time = soapObject.getProperty(3).toString().split(" ")[0].toString();
        String todayInfo = soapObject.getProperty(4).toString();
        String airQuality = soapObject.getProperty(5).toString().split("。")[1];

        for (int i = 0; i < 5; i++) {
            WeatherInfo weatherInfo = new WeatherInfo();
            String date = soapObject.getProperty(7 + 5 * i).toString();
            weatherInfo.setDate(date.split(" ")[0]);
            weatherInfo.setWeatherDes(date.split(" ")[1]);
            String temperature = soapObject.getProperty(8 + 5 * i).toString();
            /* 12℃/24℃ */
            int minTem = Integer.parseInt(temperature.substring(0,temperature.indexOf("℃")));
            int maxTem = Integer.parseInt(temperature.substring(temperature.indexOf("/")+1,temperature.lastIndexOf("℃")));
            weatherInfo.setMaxTem(maxTem);
            weatherInfo.setMinTem(minTem);
            int aveTem = (minTem + maxTem) / 2;
            if(i == 0){
                aveMax = aveTem;
                aveMin = aveTem;
            }else {
                if (aveMax < aveTem)
                    aveMax = aveTem;
                if (aveMin > aveTem)
                    aveMin = aveTem;
            }
            String wind = soapObject.getProperty(9 + 5 * i).toString();
            weatherInfo.setWind(wind);
            String picId = soapObject.getProperty(10 + 5 * i).toString();
            weatherInfo.setPicId(picId);
            String picId2 = soapObject.getProperty(11 + 5 * i).toString();
            weatherInfo.setPicId2(picId2);
            weatherInfos.add(weatherInfo);
        }
        cityInfo.setAirQuality(airQuality);
        cityInfo.setTime(time);
        cityInfo.setProvince(province);
        cityInfo.setCityName(cityName);
        cityInfo.setTodayInfo(todayInfo);
        cityInfo.setWeatherInfos(weatherInfos);
        cityInfo.setAveMax(aveMax);
        cityInfo.setAveMin(aveMin);
        return cityInfo;
    }

    /**
     * 解析SoapObject为省份和城市的集合
     * @param detail
     * @return
     */
    private static List<String> parseProvinceOrCity(SoapObject detail) {
        List<String> result = new ArrayList<>();
        if(detail == null || detail.getPropertyCount() == 0){
            return null;
        }else {
            for (int i = 0; i < detail.getPropertyCount(); i++) {
                String data = detail.getProperty(i).toString().split(",")[0];
                result.add(data);
            }
            return result;
        }
    }
}
