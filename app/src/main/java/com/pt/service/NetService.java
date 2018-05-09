package com.pt.service;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络服务主要是从网络中获得所在城市
 */
public class NetService {

	public static final String IP_ADD= "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
	private static final String TAG = "NetService";

	/**
	 * 通过IP主机外网IP取得城市名
	 * @return
     */
	public static String getCity() {
		String city;
		try {
			String address = IP_ADD;
			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setUseCaches(false);

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream in = connection.getInputStream();

				//包装输入流
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));

				String tmpString = "";
				StringBuilder retJSON = new StringBuilder();
				while ((tmpString = reader.readLine()) != null) {
					retJSON.append(tmpString + "\n");
				}

				JSONObject jsonObject = new JSONObject(retJSON.toString());
				String code = jsonObject.getString("code");
				if (code.equals("0")) {
					JSONObject data = jsonObject.getJSONObject("data");
					city = data.getString("region").split("省")[0].toString() + " " + data.getString("city").split("市")[0].toString();
					Log.i(TAG, "getCity: " + city);
				} else {
					city = null;
				}
			} else {
				city = null;
			}
		} catch (Exception e) {
			city = null;
		}
		return city;
	}
}
