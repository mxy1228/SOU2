package com.xmy.sou.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.xiaomi.infra.galaxy.android.GalaxyOAuthClient;
import com.xiaomi.infra.galaxy.common.model.MiSatRequest;
import com.xiaomi.infra.galaxy.common.model.OAuthProvider;
import com.xiaomi.infra.galaxy.common.model.Result;
import com.xiaomi.infra.galaxy.common.model.SetRequest;
import com.xiaomi.infra.galaxy.common.model.SetResult;
import com.xmy.sou.Contants;
import com.xmy.sou.log.SLog;
import com.xmy.sou.util.SystemUtil;

public class XiaomiCloudSyncService extends IntentService {

	private GalaxyOAuthClient mClient;
	
	public XiaomiCloudSyncService(){
		super("XiaomiCloudSyncService");
		init();
	}
	
	/**
	 * 构造函数：
	 * @autor xumengyang
	 * @param name
	 */
	public XiaomiCloudSyncService(String name) {
		super("XiaomiCloudSyncService");
		init();
	}
	
	private void init(){
		mClient = GalaxyOAuthClient.createInstance(String.valueOf(Contants.XIAOMI_APP_ID));
	}

	@Override
	protected void onHandleIntent(Intent intent) {
//		String apps = intent.getStringExtra("apps");
//		if(apps == null){
//			SLog.e("apps is null");
//			return;
//		}
		Bundle b = intent.getExtras();
		String token = b.getString("access_token");
		String macKey = b.getString("mac_key");
		if(miAuthLogin(token, macKey)){
			set();
		}
	}
	
	private boolean miAuthLogin(String token,String macKey){
		MiSatRequest satReq = new MiSatRequest();
		satReq.setAppId(String.valueOf(Contants.XIAOMI_APP_ID));
		satReq.setOauthAppId(String.valueOf(Contants.XIAOMI_APP_ID));
		satReq.setOauthProvider(OAuthProvider.XiaoMi.name());
		satReq.setAccessToken(token);
		satReq.setMacKey(macKey);
		return mClient.getstorageAccessToken(satReq);
	}

	/**
	 * 
	 *
	 * @param @return
	 * @return boolean
	 *
	 */
	private boolean set(){
		try {
			String deviceID = SystemUtil.getDeviceId(this);
			if(deviceID == null){
				SLog.e("deviceID is null");
				return true;
			}
			SLog.d("deviceID = "+deviceID);
			SetRequest setReq = new SetRequest();
			setReq.setTableName(Contants.CLOUD_TABLE_NAME);
			setReq.addKey(Contants.CLOUD_COLUM_DEVICE_ID, deviceID);
			setReq.addAttributeValue(Contants.CLOUD_COLUM_TIME_STAMP, System.currentTimeMillis());
			SetResult setResult = mClient.set(setReq);
			if(setResult.getCode() == Result.SUCCESS){
				SLog.d("查询云数据deviceid = "+deviceID+" 结果："+setResult.getMessage());
			}else{
				SLog.e("查询云数据错误：getResult.getCode = "+setResult.getCode()+":"+setResult.getMessage());
			}
		} catch (Exception e) {
			SLog.e(e);
		}
		return false;
	}
	
	private void get(){
		try {
			
		} catch (Exception e) {
			SLog.e(e);
		}
	}
}
