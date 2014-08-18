package com.xmy.sou.entity;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private String name;
	private String packageName;
	private String versionName;
	private int versionCode;
	private String sort_key;
	private long lastOpenTime;
	private int openCount;
	private Drawable icon;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getSort_key() {
		return sort_key;
	}
	public void setSort_key(String sort_key) {
		this.sort_key = sort_key;
	}
	public long getLastOpenTime() {
		return lastOpenTime;
	}
	public void setLastOpenTime(long lastOpenTime) {
		this.lastOpenTime = lastOpenTime;
	}
	public int getOpenCount() {
		return openCount;
	}
	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
}
