package com.xmy.sou.view;

import net.youmi.android.AdManager;
import net.youmi.android.onlineconfig.OnlineConfigCallBack;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.testin.agent.TestinAgent;
import com.umeng.analytics.MobclickAgent;
import com.xmy.sou.SouApplication;
import com.xmy.sou.log.SLog;


public abstract class BaseActivity extends SherlockFragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}
    
    protected abstract void initView();
    
    protected abstract void initData();
    
    protected abstract void initEvent();
    
    protected void showLongToast(int res){
    	Toast.makeText(this, res, Toast.LENGTH_LONG).show();
    }
    
    protected void showShortTaost(int res){
    	Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }
    
    protected void showLongToast(String str){
    	Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
    
    protected void showShortToast(String str){
    	Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(){
    	View view = getCurrentFocus();
    	if(view != null){
    		InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
    		inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    	}
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//友盟统计
    	MobclickAgent.onResume(this);
    	AdManager.getInstance(this).asyncGetOnlineConfig("isShowAD", new OnlineConfigCallBack() {
			
			@Override
			public void onGetOnlineConfigSuccessful(String key, String value) {
				if(key.equals("isShowAD")){
					if(value.equals("1")){
						//显示广告
						SouApplication.isShowAD = true;
						SLog.v("show AD");
					}else{
						//隐藏广告
						SouApplication.isShowAD = false;
						SLog.v("hide AD");
					}
				}
			}
			
			@Override
			public void onGetOnlineConfigFailed(String arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	//友盟统计
    	MobclickAgent.onPause(this);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	TestinAgent.onStart(this);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	TestinAgent.onStop(this);
    }
}
