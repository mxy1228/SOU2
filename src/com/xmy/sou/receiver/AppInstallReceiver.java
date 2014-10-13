package com.xmy.sou.receiver;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

import com.xmy.handler.IAppDataHandler;
import com.xmy.presenter.AppDataPresenter;
import com.xmy.sou.entity.AppInfo;
import com.xmy.sou.log.SLog;

/**
 * 
 * 功能描述：监听APP安装及更新
 *
 * @date 2014年8月15日
 */
public class AppInstallReceiver extends BroadcastReceiver implements IAppDataHandler{

	private AppDataPresenter mDataPresenter;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		SLog.d("install app info="+intent.getDataString()+" action="+intent.getAction());
		String packageName = intent.getDataString().replace("package:", "");
		List<PackageInfo> appList = context.getPackageManager().getInstalledPackages(0);
		mDataPresenter = new AppDataPresenter(this,context);
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
					mDataPresenter.saveOrUpdate(info);
					break;
				}
			}
		}
		
	}

	@Override
	public void isEmpty(boolean empty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSearchResult(List<AppInfo> result) {
		// TODO Auto-generated method stub
		
	}
}
