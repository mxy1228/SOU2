package com.xmy.sou.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class DetectAppLanchService extends Service {

	private static final int PERIOD = 4 * 1000;//每4s进行一次监听
	
	private ActivityManager mAM;
	private Timer mTimer;
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mAM == null){
			mAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		}
		this.mTimer = new Timer();
		this.mTimer.scheduleAtFixedRate(new MyTimerTask(), 0, PERIOD);
		//以保证Service进程被干掉后可以重启
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 循环对用户启动的APP进行监听
	 *
	 */
	private class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			List<ActivityManager.RunningTaskInfo> taskInfo = mAM.getRunningTasks(1);
			ComponentName topActivity = taskInfo.get(0).topActivity;
			String packageName = topActivity.getPackageName();
			
		}
		
	}

}
