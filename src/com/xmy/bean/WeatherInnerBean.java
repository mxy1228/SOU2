package com.xmy.bean;

import java.util.List;

public class WeatherInnerBean {

	private String currentCity;
	private List<W> weather_data;
	
	
	
	public String getCurrentCity() {
		return currentCity;
	}




	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}




	public List<W> getWeather_data() {
		return weather_data;
	}




	public void setWeather_data(List<W> weather_data) {
		this.weather_data = weather_data;
	}

}
