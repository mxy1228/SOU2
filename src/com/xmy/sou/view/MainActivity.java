package com.xmy.sou.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Window;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.xmy.bean.WeatherToShowBean;
import com.xmy.itf.IMainActivity;
import com.xmy.presenter.MainPresenter;
import com.xmy.sou.R;
import com.xmy.sou.db.AppDao;
import com.xmy.sou.entity.AppInfo;
import com.xmy.sou.http.MyHttpRequest;
import com.xmy.sou.log.SLog;
import com.xmy.sou.view.adapter.AppListAdapter;

public class MainActivity extends BaseActivity implements OnEditorActionListener,OnItemClickListener,IMainActivity,OnClickListener,OnItemLongClickListener{

    private EditText mSearchET;
    private ListView mListView;
    private LinearLayout mRootView;
    private ActionBar mActionBar;
    private TextView mActionBarTV;
    private ImageButton mClearIBtn;
    private PopupWindow mUnistallPopView;
    private ImageButton mUnistallIBtn;
    	
    private AppDao mDao;
    private AlphaInAnimationAdapter mAlphaAdapter;
    private List<AppInfo> mData;
    private MainPresenter mPresenter;
    private AlphaAnimation mInAnim;
    private AlphaAnimation mOutAnim;
    private Animation mShakeAnim;
//    private int mCurAnimItemIndex = -1;
    private View mCurAnimItemView;//当前显示动画的Item
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);
        mDao = new AppDao(this);
        initView();
        initEvent();
        initData();
    }
    
    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
    	return true;
    }
    
	@Override
    protected void onResume() {
    	super.onResume();
    	//请求定位
    	mPresenter.requstLocate(getApplicationContext());
    	mPresenter.requestRootViewBG(this);
    	setSupportProgressBarIndeterminateVisibility(true);
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
    	this.mRootView = (LinearLayout)findViewById(R.id.main_rootview);
    	LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View actionbarView = inflater.inflate(R.layout.actionbar, null);
    	this.mActionBarTV = (TextView)actionbarView.findViewById(R.id.actionbar_tv);
    	this.mActionBar = getSupportActionBar();
    	ActionBar.LayoutParams ctsParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT
    			, ActionBar.LayoutParams.WRAP_CONTENT);
    	this.mClearIBtn = (ImageButton)findViewById(R.id.main_clear_ibtn);
    	ctsParams.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
    	this.mActionBar.setCustomView(actionbarView,ctsParams);
    	this.mActionBar.setDisplayShowCustomEnabled(true);
    	this.mInAnim = new AlphaAnimation(0.0f, 1.0f);
    	this.mInAnim.setDuration(500);
    	this.mInAnim.setFillAfter(true);
    	this.mOutAnim = new AlphaAnimation(1.0f, 0.0f);
    	this.mOutAnim.setDuration(500);
    	this.mOutAnim.setFillAfter(true);
    	this.mShakeAnim = new AnimationUtils().loadAnimation(MainActivity.this, R.anim.shake);
    	View unistallView = inflater.inflate(R.layout.unistall_ibtn, null);
    	this.mUnistallIBtn = (ImageButton)unistallView.findViewById(R.id.unistall_ibtn);
    	this.mUnistallIBtn.setOnClickListener(this);
    	this.mUnistallPopView = new PopupWindow(unistallView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
	}

	@Override
	protected void initData() {
	    if(mData == null){
    		mData = new ArrayList<AppInfo>();
    	}
    	if(mAlphaAdapter == null){
    		AppListAdapter adapter = new AppListAdapter(this, mData);
    		mAlphaAdapter = new AlphaInAnimationAdapter(adapter);
    		mAlphaAdapter.setAbsListView(mListView);
    	}
    	mListView.setAdapter(mAlphaAdapter);
    	mPresenter = new MainPresenter(this);
	}

	@Override
	protected void initEvent() {
		this.mUnistallIBtn.setOnClickListener(this);
		this.mListView.setOnItemClickListener(this);
		this.mListView.setOnItemLongClickListener(this);
		this.mClearIBtn.setOnClickListener(this);
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
				//结束摇动动画
				clearListViewItemAnimation();
				mData.clear();
				mAlphaAdapter.reset();
				if(!TextUtils.isEmpty(key)){
					if(!mClearIBtn.isShown()){
						mClearIBtn.startAnimation(mInAnim);
						mClearIBtn.setVisibility(View.VISIBLE);
					}
					List<AppInfo> data = mDao.search(key);
					if(data == null){
						SLog.e("data is null");
						return;
					}
					mData.addAll(data);
				}else{
					if(mClearIBtn.isShown()){
						mClearIBtn.startAnimation(mOutAnim);
						mClearIBtn.setVisibility(View.GONE);
					}
				}
				mAlphaAdapter.notifyDataSetChanged();
			}
		});
    	this.mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				clearListViewItemAnimation();
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
				setSupportProgressBarIndeterminateVisibility(false);
				mActionBarTV.setText(t.getTemperature());
				mActionBar.setIcon(t.getRes());
				mActionBar.setTitle(Html.fromHtml("<font color=\"white\">"+t.getWeather()+"</font>"));
			}

			@Override
			public void onFailure() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onReceiveRootBG(String fromColor, String toColor) {
		GradientDrawable d = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.parseColor(fromColor),Color.parseColor(toColor)});
		mRootView.setBackgroundDrawable(d);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_clear_ibtn:
			mSearchET.setText("");
			break;
		//卸载应用
		case R.id.unistall_ibtn:
			String packageName = v.getTag().toString();
			if(packageName != null){
				Uri packageUri = Uri.parse("package:"+packageName);
				Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		//长按删除
		AppInfo info = (AppInfo)mListView.getItemAtPosition(position);
//		int isSystemApp = Settings.Secure.getInt(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS,0);
		if(info != null){
			mCurAnimItemView = view;
			view.startAnimation(mShakeAnim);
			int[] location = new int[2];
			view.getLocationOnScreen(location);
			mUnistallPopView.showAtLocation(view.findViewById(R.id.app_item_content_ll), Gravity.RIGHT|Gravity.TOP, location[0], location[1]);
			mUnistallIBtn.setTag(info.getPackageName());
		}
		return true;
	}
	
	/**
	 * 清除ListView Item的动画
	 */
	private void clearListViewItemAnimation(){
		if(mCurAnimItemView != null){
			mCurAnimItemView.clearAnimation();
			mUnistallPopView.dismiss();
		}
	}
    
}
