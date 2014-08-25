package com.xmy.sou.http;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.xmy.bean.WeatherBean;
import com.xmy.bean.WeatherToShowBean;
import com.xmy.sou.R;
import com.xmy.sou.log.SLog;

public class MyHttpRequest {

	private static final String WEATHER_URL = "http://api.map.baidu.com/";
//			"http://api.36wu.com/Weather/GetWeatherByLocation";//根据经纬度请求天气的API
	
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
	public <T> void weatherReq(double lng,double lat,final ReqHandler<WeatherToShowBean> handler){
		String uri = "telematics/v3/weather?"+"location="+lng+","+lat+"&output=json"+"&ak=A72e372de05e63c8740b2622d0ed8ab1";
		String url = WEATHER_URL + uri;
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
					String weather = bean.getResults().get(0).getWeather_data().get(0).getWeather();
					WeatherToShowBean tBean = new WeatherToShowBean();
					if(weather.contains("沙") || weather.contains("尘") || weather.contains("霾") || weather.contains("雾")){
						tBean.setRes(R.drawable.weather_sha);
					}else if(weather.equals("晴")){
						tBean.setRes(R.drawable.weather_qing);
					}else if(weather.equals("多云")){
						tBean.setRes(R.drawable.weather_cloud);
					}else if(weather.contains("阴")){
						tBean.setRes(R.drawable.weather_yin);
					}else if(weather.contains("雷")){
						tBean.setRes(R.drawable.weather_lei);
					}else if(weather.contains("雨")){
						tBean.setRes(R.drawable.weather_dayu);
					}else if(weather.contains("雪")){
						tBean.setRes(R.drawable.weather_xiaoxue);
					}
					tBean.setWeather(weather);
					handler.onSuccess(tBean);
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
