package com.xmy.sou.receiver;

import java.util.List;

import com.xmy.sou.db.AppDao;
import com.xmy.sou.log.SLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

/**
 * 
 * 功能描述：监听APP安装及更新
 *
 * @date 2014年8月15日
 */
public class AppInstallReceiver extends BroadcastReceiver {

	private AppDao mDao;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		SLog.d("install app info="+intent.getDataString());
		String packageName = intent.getDataString().replace("package:", "");
		List<PackageInfo> appList = context.getPackageManager().getInstalledPackages(0);
		this.mDao = new AppDao(context);
		new Thread(new MyRunnable(packageName,appList)).start();
	}

	private class MyRunnable implements Runnable{
		String packageName;
		List<PackageInfo> infos;
		
		public MyRunnable(String pName,List<PackageInfo> list){
			packageName = pName;
			infos = list;
		}
		
		@Override
		public void run() {
			for(PackageInfo info : infos){
				if(info.packageName.equals(packageName)){
					SLog.d("save "+packageName+" success");
					mDao.saveOrUpdate(info);
					break;
				}
			}
		}
		
	}
}
