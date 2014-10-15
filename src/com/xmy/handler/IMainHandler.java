package com.xmy.handler;

import java.util.List;

import com.xmy.sou.entity.SlidingMenuItem;

public interface IMainHandler {

	public void onLocation(double lnt,double lat);//接收到百度定位的经纬度
	
	public void onReceiveRootBG(String fromColor,String toColor);//根据时间计算背景颜色
	
	public void onLoadSlidingMenuData(List<SlidingMenuItem> list);//装载SlidingMenu数据
} 
