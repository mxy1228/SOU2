package com.xmy.bean;

import java.util.List;

public class WeatherBean {

	private String status;
	private String error;
	private String date;
	private List<WeatherInnerBean> results;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<WeatherInnerBean> getResults() {
		return results;
	}
	public void setResults(List<WeatherInnerBean> results) {
		this.results = results;
	}
	
	
}
