package com.xmy.sou.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.xmy.bean.WeatherBean;
import com.xmy.bean.WeatherToShowBean;
import com.xmy.itf.IMainActivity;
import com.xmy.presenter.MainPresenter;
import com.xmy.sou.R;
import com.xmy.sou.db.AppDao;
import com.xmy.sou.entity.AppInfo;
import com.xmy.sou.http.MyHttpRequest;
import com.xmy.sou.log.SLog;
import com.xmy.sou.view.adapter.AppListAdapter;
import com.xmy.sou.widget.StreamView;

public class MainActivity extends BaseActivity implements OnEditorActionListener,OnItemClickListener,IMainActivity{

    private EditText mSearchET;
    private ListView mListView;
    private StreamView mStreamView;
    private ActionBar mActionBar;
    	
    private AppDao mDao;
    private AppListAdapter mAdapter;
    private List<AppInfo> mData;
    private MainPresenter mPresenter;
    
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
    	this.mStreamView = (StreamView)findViewById(R.id.stream_view);
    	this.mActionBar = getActionBar();
    	int[] res = new int[2];
    	res[0] = R.drawable.rain1;
    	res[1] = R.drawable.rain2;
    	this.mStreamView.setResource(res);
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
    	mPresenter = new MainPresenter(this);
    	mPresenter.requstLocate(getApplicationContext());
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

	/**
	 * 接收到百度定位结果
	 */
	@Override
	public void onLocation(double lnt, double lat) {
		new MyHttpRequest().weatherReq(lnt, lat, new MyHttpRequest.ReqHandler<WeatherToShowBean>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(WeatherToShowBean t) {
				mActionBar.setIcon(t.getRes());
				mActionBar.setTitle(t.getWeather());
//				Toast.makeText(MainActivity.this, t.getResults().get(0).getWeather_data().get(0).getWeather(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure() {
				// TODO Auto-generated method stub
				
			}
		});
	}
    
    
}
