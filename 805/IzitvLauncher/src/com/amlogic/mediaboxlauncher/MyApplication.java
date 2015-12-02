package com.amlogic.mediaboxlauncher;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	private static MyApplication instance;
	private static String strCity;
	private static String strWeather;
	private static long time;
	private String result;
	
	private  MyApplication(){
		
	}

	public static MyApplication getMyApplication() {

		if (instance == null) {
			instance = new MyApplication();
		}

		return instance;
	}

	@Override
	public void onCreate() {
		new Thread(){
			public void run() {	
			//http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=bd82977b86bf27fb59a04b61b657fb6f		 appid=1eceb5a4d31f6a74bbf1dbf4ac3712ec	
				strCity=getCity(getRequestResult("http://api.izinode.com/weather"));
				strWeather=getWeather(getRequestResult("http://api.izinode.com/weather"));
				String str=getRequestResult("http://api.izinode.com/weather");
				Log.v("MyApplication", str);
				Log.v("MyApplication", "cuty:"+strCity+"weather:"+strWeather);
			};
			
		}.start();
		super.onCreate();
	}

	
	private String getRequestResult(String str) {
		HttpGet httpGet = new HttpGet(str);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.e("MyApplication", result);
		return result;
	}

	private String getCity(String result) {
		String city_name = null;
		try {
			JSONObject jObject = new JSONObject(result);
			city_name = jObject.getString("city");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return city_name;
	}

	private String getWeather(String wethJson) {
		JSONObject object = null;
		JSONObject array = null;
		String dec = null;
		String temp=null;
		try {
			object = new JSONObject(wethJson);
			array = object.getJSONObject("weather");
			dec = array.getString("des");
			temp=array.getString("temp");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return dec+""+temp;
	}


}
