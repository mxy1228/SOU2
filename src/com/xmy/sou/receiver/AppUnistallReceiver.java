package com.xmy.sou.receiver;

import com.xmy.event.UnistallAppEvent;
import com.xmy.sou.db.AppDao;
import com.xmy.sou.log.SLog;

import de.greenrobot.event.EventBus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * 功能描述：监听APP卸载
 *
 * @date 2014年8月15日
 */
public class AppUnistallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SLog.d("unistall app info="+intent.getDataString());
		String packageName = intent.getDataString().replace("package:", "");
		if(new AppDao(context).delelte(packageName)){
			SLog.d("delte "+packageName+" in DB success");
			UnistallAppEvent e = new UnistallAppEvent();
			e.setPackageName(packageName);
			EventBus.getDefault().post(e);
		}else{
			SLog.e("delete "+packageName+" in DB failed");
		}
	}

}
