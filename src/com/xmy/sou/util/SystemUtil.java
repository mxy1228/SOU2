package com.xmy.sou.util;

import java.util.UUID;

import com.xmy.sou.log.SLog;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class SystemUtil {

	/**
	 * 
	 * 功能描述：deviceID的组成为：识别符来源标志： 1， wifi mac地址（wifi）； 2， IMEI（imei）； 3，序列号（sn）；
	 * 
	 * @param @return
	 * @return String
	 * 
	 */
	public static String getDeviceId(Context context) {

		StringBuilder deviceId = new StringBuilder();
		try {
			// wifi mac地址
//			WifiManager wifi = (WifiManager) context
//					.getSystemService(Context.WIFI_SERVICE);
//			WifiInfo info = wifi.getConnectionInfo();
//			String wifiMac = info.getMacAddress();
//			if (!TextUtils.isEmpty(wifiMac)) {
//				deviceId.append(wifiMac).append("_");
//			}
			// IMEI（imei）
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tm.getDeviceId();
			if (!TextUtils.isEmpty(imei)) {
				deviceId.append(imei).append("_");
			}
			// 序列号（sn）
			String sn = tm.getSimSerialNumber();
			if (!TextUtils.isEmpty(sn)) {
				deviceId.append(sn).append("_");
			}
		} catch (Exception e) {
			SLog.e(e);
		}
		return deviceId.toString();
	}
	
}
