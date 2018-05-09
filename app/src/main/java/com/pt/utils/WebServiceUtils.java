package com.pt.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by 韬 on 2016-05-24.
 */
public class WebServiceUtils {

    private static final String TAG = "WebServiceUtils";

    // 定义WebService的命名空间
    static final String SERVICE_NS = "http://WebXml.com.cn/";
    // 定义WebService提供服务的URL
    static final String SERVICE_URL = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx";
/**
 * 调用webservice方法
 * @param methodName  方法名
 * @param property   传递的参数，比如说查询天气，要传递城市名
 * @return
 */


    public  static SoapObject invokingMethod(String methodName,String property){

        //创建HttpTransportSE传输对象
        HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
        ht.debug = true;
        //使用Soap1.1
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject，其构造方法为命名空间和方法名
        SoapObject soapObject = new SoapObject(SERVICE_NS,methodName);
        if(property != null) {
            if ("getSupportCityString".equals(methodName)) {
                soapObject.addProperty("theRegionCode", property);
            } else if ("getWeather".equals(methodName)) {
                soapObject.addProperty("theCityCode", property);
                soapObject.addProperty("theUserID","2384ea2d3dbc42f5a65512e1ab5fc676");
            }
        }
        envelope.bodyOut = soapObject;
        // 设置与.NET提供的WebService保持较好的兼容性
        envelope.dotNet = true;
        try {
            //调用webservice
            ht.call(SERVICE_NS + methodName , envelope);
            if(envelope.getResponse() != null){
                SoapObject result = (SoapObject) envelope.bodyIn;
                Log.i(TAG, "invokingMethod: " + result.toString());
                SoapObject detail = (SoapObject) result.getProperty(methodName+"Result");
                Log.i(TAG, "invokingMethod: " + detail.toString());
                return detail;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }




}
