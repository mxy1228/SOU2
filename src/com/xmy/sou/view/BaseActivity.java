package com.xmy.sou.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.testin.agent.TestinAgent;
import com.umeng.analytics.MobclickAgent;


public abstract class BaseActivity extends SherlockFragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
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
