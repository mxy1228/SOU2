package com.xmy.presenter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.format.Time;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.xmy.handler.IMainHandler;
import com.xmy.sou.R;
import com.xmy.sou.entity.SlidingMenuItem;
import com.xmy.sou.log.SLog;

public class MainPresenter {
	
	private IMainHandler mIMainActivity;
	
	public MainPresenter(IMainHandler i){
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
	
	/**
	 * 根据当前系统时间设置背景颜色
	 * @param ctx
	 */
	public void requestRootViewBG(Context ctx){
		Time t = new Time();
		t.setToNow();
		int hour = t.hour;
		SLog.d("current hour = "+hour);
		String[] colors = null;
		if(hour >= 0 && hour < 6){
			colors = ctx.getResources().getStringArray(R.array.a_color);
		}else if(hour >= 6 && hour < 9){
			colors = ctx.getResources().getStringArray(R.array.b_color);
		}else if(hour >= 9 && hour < 12){
			colors = ctx.getResources().getStringArray(R.array.c_color);
		}else if(hour >= 12 && hour < 16){
			colors = ctx.getResources().getStringArray(R.array.d_color);
		}else if(hour >= 16 && hour < 18){
			colors = ctx.getResources().getStringArray(R.array.e_color);
		}else if(hour >= 18 && hour < 21){
			colors = ctx.getResources().getStringArray(R.array.f_color);
		}else if(hour >= 21 && hour < 24){
			colors = ctx.getResources().getStringArray(R.array.g_color);
		}
		mIMainActivity.onReceiveRootBG(colors[0], colors[1]);
	}
	
	/**
	 * 启动DB检测服务
	 * @param ctx
	 */
	public void startCheckService(Context ctx){
		
	}
	
	/**
	 * 装载SlidingMenu的数据
	 */
	public void initSlidingMenuData(String[] arrays){
		List<SlidingMenuItem> data = new ArrayList<SlidingMenuItem>();
		for(int i=0;i<arrays.length;i++){
			//slipt后props中的内容为：
			//[0] 按钮标题
			//[1] 点击后要执行的Action
			String[] props = arrays[i].split("#");
			SlidingMenuItem item = new SlidingMenuItem();
			item.setmTitle(props[0]);
			item.setmAction(props.length==1?"":props[1]);
			data.add(item);
		}
		mIMainActivity.onLoadSlidingMenuData(data);
	}
	
	
}
