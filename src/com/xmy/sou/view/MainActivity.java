package com.xmy.sou.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.xmy.sou.R;
import com.xmy.sou.db.AppDao;
import com.xmy.sou.entity.AppInfo;
import com.xmy.sou.log.SLog;
import com.xmy.sou.view.adapter.AppListAdapter;

public class MainActivity extends BaseActivity implements OnEditorActionListener,OnItemClickListener{

    private EditText mSearchET;
    private ListView mListView;
    	
    private AppDao mDao;
    private AppListAdapter mAdapter;
    private List<AppInfo> mData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mDao = new AppDao(this);
        initView();
        initEvent();
        initData();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		    	inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 500);
    	
    }


	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		if(arg1 == EditorInfo.IME_ACTION_SEARCH){
			//TODO
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		AppInfo info = (AppInfo)mListView.getItemAtPosition(arg2);
		if(info != null){
			PackageManager manager = MainActivity.this.getPackageManager();
			Intent intent = manager.getLaunchIntentForPackage(info.getPackageName());
			startActivity(intent);
		}
	}

	@Override
	protected void initView() {
	    this.mListView = (ListView)findViewById(R.id.main_lv);
    	this.mSearchET = (EditText)findViewById(R.id.main_et);
	}

	@Override
	protected void initData() {
	    if(mData == null){
    		mData = new ArrayList<AppInfo>();
    	}
    	if(mAdapter == null){
    		mAdapter = new AppListAdapter(this, mData);
    	}
    	mListView.setAdapter(mAdapter);
    	mListView.setOnItemClickListener(this);
	}

	@Override
	protected void initEvent() {
		this.mSearchET.setOnEditorActionListener(this);
    	this.mSearchET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String key = s.toString().trim();
				if(TextUtils.isEmpty(key)){
					mAdapter.changeData(mData);
				}else{
					List<AppInfo> data = mDao.search(key);
					if(data == null){
						SLog.e("data is null");
						return;
					}
					mAdapter.changeData(data);
				}
			}
		});
	}
    
    
}