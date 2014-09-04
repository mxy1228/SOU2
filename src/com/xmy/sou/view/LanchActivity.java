package com.xmy.sou.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.xmy.event.SaveAppEvent;
import com.xmy.sou.R;
import com.xmy.sou.db.AppDao;
import com.xmy.sou.widget.LanchProgressView;
import com.xmy.sou.widget.StreamView;

import de.greenrobot.event.EventBus;

public class LanchActivity extends BaseActivity {
	
    private AppDao mDao;
    	
    private TextView mRateTV;
    private RelativeLayout mContainerRL;
//    private ImageView mIV;
    private ScrollView mSV;
    private StreamView mStreamView;
    
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
			mDao.saveList(appList);
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
	    this.mDao = new AppDao(this);
//	    mIV.setTranslationX(0);
//	    mIV.setTranslationY(0);
//	    ViewPropertyAnimator propertyAnim = mIV.animate()
//				.translationX(0)
//				.translationY(-)
//				.setDuration(20 * 1000);
//		propertyAnim.start();
//	    jump2();
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
	 * 
	 * 功能描述：判断DB里的数据是否为空，若空则扫描应用列表并存储，否则直接进入MainActivity
	 *
	 * @param 
	 * @return void
	 *
	 */
	private void jump2(){
	    if(mDao.isEmpty()){
	    	mSaveThread.start();
	    }else{
	    	jump2Main();
	    }
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
//	    	int scrollY = mSV.getScrollY();
	    	mSV.smoothScrollTo(0, rate * 40);
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
}
