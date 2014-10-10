package com.xmy.handler;

public interface IMainHandler {

	public void onLocation(double lnt,double lat);//接收到百度定位的经纬度
	
	public void onReceiveRootBG(String fromColor,String toColor);//根据时间计算背景颜色
}
