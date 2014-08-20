package com.xmy.sou.http;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xmy.sou.log.SLog;

public class MyHttpHandler extends AsyncHttpResponseHandler {

	private String url;
	
	public void setUrl(String u){
		this.url = u;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onSuccess(int statusCode, String content) {
		super.onSuccess(statusCode, content);
		SLog.d(url+":onSuccess="+content);
	}
	
	@Override
	public void onFailure(Throwable error, String content) {
		super.onFailure(error, content);
		SLog.d(url+":onFailure="+content);
	}
}
