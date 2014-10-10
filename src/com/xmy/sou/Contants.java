package com.xmy.sou;

public class Contants {

	/**
	 * 小米云存储上保存用户信息的表名
	 */
	public static final String CLOUD_TABLE_NAME = "users";
	
	/**
	 * 小米云存储上保存用户设备id的字段
	 */
	public static final String CLOUD_COLUM_DEVICE_ID = "device_id";
	
	/**
	 * 小米云存储上保存用户apps历史的字段
	 */
	public static final String CLOUD_COLUM_APP_HISTORY = "app_history";
	
	/**
	 * 小米云存储上保存用户创建时间的字段
	 */
	public static final String CLOUD_COLUM_TIME_STAMP = "time_stamp";
	
	/**
	 * 小米APPID
	 */
	public static final Long XIAOMI_APP_ID = 2882303761517238400l;
	public static final int TIME_OUT = 15 * 1000;//网络连接超时时间
}
