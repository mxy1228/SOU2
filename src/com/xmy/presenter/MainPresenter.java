package com.xmy.presenter;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.xmy.itf.IMainActivity;

public class MainPresenter {
	
	private IMainActivity mIMainActivity;
	
	public MainPresenter(IMainActivity i){
		this.mIMainActivity = i;
	}

	/**
	 * 发起百度定位
	 * @param ctx
	 */
	public void requstLocate(Context ctx){
		final LocationClient client = new LocationClient(ctx);
		BDLocationListener listener = new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				mIMainActivity.onLocation(arg0.getLongitude(), arg0.getLatitude());
				client.stop();
			}
		};
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5s
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		client.setLocOption(option);
		client.registerLocationListener(listener);
		client.start();
		client.requestLocation();
	}
}
