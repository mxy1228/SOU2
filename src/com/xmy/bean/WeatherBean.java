package com.xmy.bean;

public class WeatherBean {

	private String status;
	private String message;
	private WeatherInnerBean data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public WeatherInnerBean getData() {
		return data;
	}
	public void setData(WeatherInnerBean data) {
		this.data = data;
	}
	
	
}
