package com.xmy.sou.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
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

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.xmy.bean.ADBean;
import com.xmy.bean.WeatherToShowBean;
import com.xmy.event.UnistallAppEvent;
import com.xmy.handler.IMainHandler;
import com.xmy.presenter.MainPresenter;
import com.xmy.sou.R;
import com.xmy.sou.SouApplication;
import com.xmy.sou.db.AppDao;
import com.xmy.sou.entity.AppInfo;
import com.xmy.sou.entity.SlidingMenuItem;
import com.xmy.sou.http.MyHttpRequest;
import com.xmy.sou.log.SLog;
import com.xmy.sou.view.adapter.AppListAdapter;
import com.xmy.sou.view.adapter.SlidingMenuAdapter;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements OnEditorActionListener,OnItemClickListener,IMainHandler,OnClickListener,OnItemLongClickListener{

    private EditText mSearchET;
    private ListView mListView;
    private LinearLayout mRootView;
    private ActionBar mActionBar;
    private TextView mActionBarTV;
    private ImageButton mClearIBtn;
    private PopupWindow mUnistallPopView;
    private ImageButton mUnistallIBtn;
//    private Button mSuggestionBtn;
    private SlidingMenu mMenu;
    private ListView mSlidingMenuLV;
    private TextView mVersionTV;
    private LinearLayout mADContainerLL;
    private AdView mADView;//有米广告
    	
    private AppDao mDao;
    private AlphaInAnimationAdapter mAlphaAdapter;
    private List<AppInfo> mData;
    private MainPresenter mPresenter;
    private AlphaAnimation mInAnim;
    private AlphaAnimation mOutAnim;
    private Animation mShakeAnim;
//    private int mCurAnimItemIndex = -1;
    private View mCurAnimItemView;//当前显示动画的Item
    private GradientDrawable mBGDrawable;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mFirstOnResume = true;//如果是第一次打开则不显示多米插屏广告
    
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
    public boolean onCreateOptionsMenu(Menu menu) {
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case android.R.id.home:
			if(mMenu.isMenuShowing()){
				mMenu.showContent();
			}else{
				mMenu.showMenu();
			}
			break;

		default:
			break;
		}
    	return true;
    }
    
	@Override
    protected void onResume() {
    	super.onResume();
    	//请求定位
    	mPresenter.requstLocate(getApplicationContext());
    	mPresenter.requestRootViewBG(this);
    	setSupportProgressBarIndeterminateVisibility(true);
    	if(SouApplication.isShowAD){
    		//显示插屏广告
    		if(mFirstOnResume){
        		mFirstOnResume = false;
        	}else{
        		SpotManager.getInstance(this).showSpotAds(this);
        	}
    	}else{
//    		//隐藏广告
//    		mADContainerLL.setVisibility(View.GONE);
    	}
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//退出APP时注销多米广播
		SpotManager.getInstance(this).unregisterSceenReceiver();
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

	@SuppressLint("NewApi")
	@Override
	protected void initView() {
	    this.mListView = (ListView)findViewById(R.id.main_lv);
    	this.mSearchET = (EditText)findViewById(R.id.main_et);
    	this.mRootView = (LinearLayout)findViewById(R.id.main_rootview);
    	LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View actionbarView = inflater.inflate(R.layout.actionbar, null);
    	this.mActionBarTV = (TextView)actionbarView.findViewById(R.id.actionbar_tv);
    	this.mActionBar = getActionBar();
    	ActionBar.LayoutParams ctsParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT
    			, ActionBar.LayoutParams.WRAP_CONTENT);
    	this.mClearIBtn = (ImageButton)findViewById(R.id.main_clear_ibtn);
    	ctsParams.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
    	this.mActionBar.setCustomView(actionbarView,ctsParams);
    	this.mActionBar.setDisplayShowCustomEnabled(true);
    	this.mActionBar.setLogo(R.drawable.actionbar_home_btn);
    	this.mActionBar.setHomeButtonEnabled(true);
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
    	this.mADContainerLL = (LinearLayout)findViewById(R.id.main_ad_container);
    	this.mADView = new AdView(this, AdSize.FIT_SCREEN);
    	this.mADContainerLL.addView(mADView);
    	initSlidingMenu();
	}
	
	private void initSlidingMenu(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		mMenu = new SlidingMenu(this);
		mMenu.setMode(SlidingMenu.LEFT);
		mMenu.setTouchModeAbove(SlidingMenu.LEFT);
//		menu.setShadowWidthRes(resId);设置阴影图片的宽度
//		menu.setShadowDrawable(resId);设置阴影图片
		mMenu.setBehindOffset((mScreenWidth/5)*3);//设置划出主页面显示的剩余宽度
		mMenu.attachToActivity(this, SlidingMenu.LEFT, true);
		mMenu.setOnOpenedListener(new OnOpenedListener() {
			
			@Override
			public void onOpened() {
				// TODO Auto-generated method stub
				
			}
		});
		mMenu.setMenu(R.layout.sliding_menu);
		this.mSlidingMenuLV = (ListView)mMenu.getMenu().findViewById(R.id.sliding_menu_lv);
		try {
			//获取版本号
			PackageManager pm = getPackageManager();
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			this.mVersionTV = (TextView)mMenu.getMenu().findViewById(R.id.menu_version_tv);
			this.mVersionTV.setText(getString(R.string.version, info.versionName));
		} catch (Exception e) {
			SLog.e(e);
		}
		
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
    	this.mPresenter.initSlidingMenuData(getResources().getStringArray(R.array.sliding_menu_actions));
    	EventBus.getDefault().register(this);
	}

	@Override
	protected void initEvent() {
		this.mSlidingMenuLV.setOnItemClickListener(new SlidingMenuLVItemClick());
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
//				mActionBar.setIcon(t.getRes());
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
		mBGDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.parseColor(fromColor),Color.parseColor(toColor)});
		mRootView.setBackgroundDrawable(mBGDrawable);
		
		Bitmap btm = Bitmap.createBitmap(mScreenWidth/3, mScreenHeight, Config.ARGB_8888);
		Canvas c = new Canvas(btm);
		mBGDrawable.setBounds(0, 0, c.getWidth(), c.getHeight());
		mBGDrawable.draw(c);
		this.mMenu.setBackgroundDrawable(new BitmapDrawable(btm));
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

	/**
	 * APP列表Item点击
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		//长按删除
		AppInfo info = (AppInfo)mListView.getItemAtPosition(position);
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
	 * SlidingMenu LsitView Item点击
	 * @author xumengyang
	 *
	 */
	private class SlidingMenuLVItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SlidingMenuItem item = (SlidingMenuItem)mSlidingMenuLV.getAdapter().getItem(position);
			if(item.getmAction() != null && !TextUtils.isEmpty(item.getmAction())){
				Intent intent = new Intent(item.getmAction());
				startActivity(intent);
			}
		}
		
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
    
	/**
	 * 有APP被卸载后
	 * @param e
	 */
	public void onEventMainThread(UnistallAppEvent e){
		Iterator<AppInfo> it = mData.iterator();
		while(it.hasNext()){
			if(it.next().getPackageName().equals(e.getPackageName())){
				it.remove();
				break;
			}
		}
		mAlphaAdapter.notifyDataSetChanged();
	}
	
	public void onEventMainThread(ADBean bean){
		if(bean.isShowAD){
			mADContainerLL.setVisibility(View.VISIBLE);
		}else{
			mADContainerLL.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onLoadSlidingMenuData(List<SlidingMenuItem> list) {
		this.mSlidingMenuLV.setAdapter(new SlidingMenuAdapter(this, list));
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
	}
}
