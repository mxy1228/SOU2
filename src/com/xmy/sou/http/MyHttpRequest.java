package com.xmy.sou.http;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.loopj.android.http.RequestParams;
import com.xmy.bean.WeatherBean;
import com.xmy.sou.log.SLog;

public class MyHttpRequest {

	private static final String WEATHER_URL = "http://api.36wu.com/Weather/GetWeatherByLocation";//根据经纬度请求天气的API
	
	public interface ReqHandler<T>{
		public void onStart();
		public void onSuccess(T t);
		public void onFailure();
	}
	
	private ObjectMapper getMapper(){
		return new ObjectMapper().configure(
				DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
				true).configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	/**
	 * 根据经纬度获取实时天气
	 * @param <T>
	 */
	public <T> void weatherReq(double lng,double lat,final ReqHandler<WeatherBean> handler){
		String url = WEATHER_URL + "?lng="+lng+"&lat="+lat+"&output=json";
		new MyHttpClient().doGet(url, null, new MyHttpHandler(){
			
			@Override
			public void onStart() {
				handler.onStart();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					super.onSuccess(statusCode, content);
					WeatherBean bean = getMapper().readValue(content, new TypeReference<WeatherBean>() {
					});
					handler.onSuccess(bean);
				} catch (Exception e) {
					SLog.e(e);
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				handler.onFailure();
			}
			
		});
	}
	
}
