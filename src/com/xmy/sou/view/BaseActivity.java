package com.xmy.sou.view;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;


public abstract class BaseActivity extends SherlockFragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	}
    
    protected abstract void initView();
    
    protected abstract void initData();
    
    protected abstract void initEvent();
}
