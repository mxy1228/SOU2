package com.xmy.sou.view;

import java.util.List;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.testin.agent.TestinAgent;
import com.umeng.update.UmengUpdateAgent;
import com.xmy.event.SaveAppEvent;
import com.xmy.handler.IAppDataHandler;
import com.xmy.presenter.AppDataPresenter;
import com.xmy.sou.R;
import com.xmy.sou.entity.AppInfo;
import com.xmy.sou.widget.LanchProgressView;
import com.xmy.sou.widget.StreamView;

import de.greenrobot.event.EventBus;

public class LanchActivity extends BaseActivity{
	
    	
    private TextView mRateTV;
    private RelativeLayout mContainerRL;
    private ScrollView mSV;
    private StreamView mStreamView;
    
    private AppDataPresenter mPresenter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lanch);
		initView();
		initEvent();
		initData();
	}
	
	private Thread mSaveThread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			List<PackageInfo> appList = getPackageManager().getInstalledPackages(0);
			mPresenter.saveList(appList);
		}
	});
	

	@Override
	protected void initView() {
	    this.mRateTV = (TextView)findViewById(R.id.lanch_tv);
	    this.mContainerRL = (RelativeLayout)findViewById(R.id.lanch_container);
	    this.mSV = (ScrollView)findViewById(R.id.lanch_sv);
	    this.mStreamView = (StreamView)findViewById(R.id.lanch_stream_view);
	    this.mStreamView.setResource(R.drawable.sky);
	    LanchProgressView progressView = new LanchProgressView(this);
	    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT
	    		, RelativeLayout.LayoutParams.WRAP_CONTENT);
	    lp.addRule(RelativeLayout.CENTER_IN_PARENT);
	    progressView.setLayoutParams(lp);
	    this.mContainerRL.addView(progressView);
	}

	@SuppressLint("NewApi")
	@Override
	protected void initData() {
		//初始化多米
	    AdManager.getInstance(this).init("364f2c0ed067aae7", "b5db7cd7e5a3c617", false);
	    //异步加载多米插屏广告
	    SpotManager.getInstance(this).loadSpotAds();
	    //设置多米插屏广告竖向显示
	    SpotManager.getInstance(this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
	    //初始化云测
	    TestinAgent.init(this, "9a0392c0f997791c591ea43e8bc6c649");
		checkUpdate();
	    mPresenter = new AppDataPresenter(new MyDataHandler(), LanchActivity.this);
	    jump2();
	}

	@Override
	protected void initEvent() {
	    EventBus.getDefault().registerSticky(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	/**
	 * 通过友盟自动更新功能检查是否有最新版本
	 */
	private void checkUpdate(){
		UmengUpdateAgent.update(this);
	}
	
	/**
	 * 
	 * 功能描述：判断DB里的数据是否为空，若空则扫描应用列表并存储，否则直接进入MainActivity
	 *
	 * @param 
	 * @return void
	 *
	 */
	private void jump2(){
		mPresenter.isEmpty();
	}
	
	/**
	 * 
	 * 功能描述：接收DB保存信息回调，在Activity中显示进度
	 *
	 * @param @param event
	 * @return void
	 *
	 */
	public void onEventMainThread(SaveAppEvent event) {
	    int rate = (int)(event.getRate() * 100);
	    this.mRateTV.setText(rate + "%");
	    if(rate == 100){
	    	jump2Main();
	    }
	}
	
	/**
	 * 
	 * 功能描述：跳转到MainActivity
	 *
	 * @param 
	 * @return void
	 *
	 */
	private void jump2Main(){
	    Intent intent = new Intent(this,MainActivity.class);
	    startActivity(intent);
	    LanchActivity.this.finish();
	}
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//		Bundle b = arg2.getExtras();
//		String token = b.getString("access_token");
//		String matchKey = b.getString("mac_key");
//		SLog.d("xiaomi token = "+token+" and matchKey = "+matchKey);
//		//开启同步到云数据库的Service
//    	Intent intent = new Intent(this,XiaomiCloudSyncService.class);
//    	intent.putExtras(b);
//    	intent.putExtra("access_token", token);
//    	intent.putExtra("match_key", matchKey);
//    	intent.putExtra("apps", event.getApps());
//    	startService(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * AppData数据操作回调
	 * @author xumengyang
	 *
	 */
	private class MyDataHandler implements IAppDataHandler{

		@Override
		public void isEmpty(boolean empty) {
			 if(empty){
			    mSaveThread.start();
			 }else{
			    jump2Main();
			 }
		}

		@Override
		public void onSearchResult(List<AppInfo> result) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
