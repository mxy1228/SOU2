package com.xmy.sou.view.cts;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xmy.sou.R;
import com.xmy.sou.log.SLog;

public class FloatView extends FrameLayout{

	private Context mContext;
	private View mChildView;
//	private LinearLayout mContainerLL;
	private LinearLayout mButtonLLRight;
	private ImageView mIV;
	private LinearLayout mButtonLLLeft;
	private WindowManager mWM;
	private WindowManager.LayoutParams mParams;
	
	private int mXInView;
	private int mYInView;
	private int mXInScreen;
	private int mYInScreen;
	
	private int mScreenHeight;
	private int mScreenWidth;
	private int mDownX;
	private int mDownY;
	private int mPerX;
	private int mPerY;
	
	private float mPressDownX;
	private float mPressDownY;
	private float mPressUpX;
	private float mPressUpY;
	
	public FloatView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	
	public FloatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	public FloatView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}
	

	private void init(){
		mWM = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mChildView = inflater.inflate(R.layout.flow_window, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
				, LinearLayout.LayoutParams.WRAP_CONTENT);
		mChildView.setLayoutParams(params);
		addView(mChildView);
		mButtonLLRight = (LinearLayout)mChildView.findViewById(R.id.flow_window_btn_containr_right);
		mButtonLLLeft = (LinearLayout)mChildView.findViewById(R.id.flow_window_btn_containr_left);
		mIV = (ImageView)mChildView.findViewById(R.id.flow_window_iv);
		mIV.setTag(1);
		mIV.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mPressDownX = event.getX();
					mPressDownY = event.getY();
					return true;
				case MotionEvent.ACTION_UP:
					mPressUpX = event.getX();
					mPressUpY = event.getY();
					if(mPressUpX == mPressDownX && mPressUpY == mPressDownY){
						final int state = Integer.valueOf(v.getTag().toString());
						SLog.d("OnTouchListener:state="+state);
//						if(state == 1){
//							mButtonLLLeft.setVisibility(View.VISIBLE);
							int left = (int)event.getRawX();
							int right = (int)(mScreenWidth - event.getRawX());
//							if(right > left){
//								mButtonLLRight.setVisibility(View.VISIBLE);
//								mButtonLLLeft.setVisibility(View.GONE);
//							}else{
//								mButtonLLRight.setVisibility(View.GONE);
//								mButtonLLLeft.setVisibility(View.VISIBLE);
//							}
//						}else{
//							mButtonLLLeft.setVisibility(View.GONE);
//							mButtonLLRight.setVisibility(View.GONE);
//							mButtonLLLeft.setVisibility(View.GONE);
//						}
						ExpandAnimation anim = new ExpandAnimation(left,right);
						anim.setDuration(300);
						anim.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								mIV.setTag(state==1?2:1);
							}
						});
						mChildView.startAnimation(anim);
						return true;
					}
				default:
					break;
				}
				return false;
			}
		});
//		mContainerLL = (LinearLayout)mChildView.findViewById(R.id.flow_window_btn_containr);
		
		DisplayMetrics metrics = new DisplayMetrics();
		mWM.getDefaultDisplay().getMetrics(metrics);
		this.mScreenHeight = metrics.heightPixels;
		this.mScreenWidth = metrics.widthPixels;
		this.mPerX = mScreenWidth / 200;
		this.mPerY = mPerX * mScreenHeight / mScreenWidth;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mChildView.layout(l, t, r, b);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			
			return true;
		case MotionEvent.ACTION_UP:
			mPressUpX = ev.getX();
			mPressUpY = ev.getY();
			if(mPressUpX == mPressDownX && mPressUpY == mPressDownY){
				return false;
			}else{
				return true;
			}
		case MotionEvent.ACTION_DOWN:
			mPressDownX = ev.getX();
			mPressDownY = ev.getY();
			mXInView = (int)ev.getX();
			mYInView = (int)ev.getY();
			mDownX = (int)ev.getRawX();
			mDownY = (int)ev.getRawY();
			return false;
		default:
			break;
		}
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		String state = mIV.getTag().toString();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			
			return false;
		case MotionEvent.ACTION_UP:
			if(mDownX == (int)event.getRawX() && mDownY == (int)event.getRawY()){
				return false;
			}
			if(!state.equals("1")){
				return false;
			}
			int top = (int)(event.getRawY() - event.getY());
			int left = (int)(event.getRawX() - event.getX());
			int right = (int)(mScreenWidth - left);
			int bottom = (int)(mScreenHeight - top);
			if(top < mScreenHeight/2){
				if(top > 240){
					if(left < right){
						new MoveTask(0, mParams.y).execute();
					}else{
						new MoveTask(mScreenWidth, mParams.y).execute();
					}
				}else{
					new MoveTask(mParams.x, 0).execute();
				}
			}else{
				if(bottom > 240){
					if(left < right){
						new MoveTask(0, mParams.y).execute();
					}else{
						new MoveTask(mScreenWidth, mParams.y).execute();
					}
				}else{
					new MoveTask(mParams.x, mScreenHeight).execute();
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			SLog.d("OnTouchListener:state="+state);
			if(state.equals("1")){
				mXInScreen = (int)event.getRawX();
				mYInScreen = (int)event.getRawY();
				updatePosition(mXInScreen,mYInScreen);
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	public void setLayoutParams(WindowManager.LayoutParams params){
		this.mParams = params;
	}

	private void updatePosition(int x,int y){
		this.mParams.x = (int)(x - mXInView);
		this.mParams.y = (int)(y - mYInView);
		mWM.updateViewLayout(this, mParams);
	}
	
	private class MoveTask extends AsyncTask<Void, Void, Void>{
		
		private int pX;
		private int pY;
		
		private int deltaX;
		private int deltaY;
		
		private int time = 5;
		
		public MoveTask(int pX,int pY){
			this.pX = pX;
			this.pY = pY;
		}
		
		@Override
		protected void onPreExecute() {
			this.deltaX = pX - Math.abs(mParams.x);
			SLog.i("pX="+pX+",mParams.x="+mParams.x+",deltaX="+deltaX);
			this.deltaY = pY - Math.abs(mParams.y);
			SLog.i("pY="+pY+",mParams.y="+mParams.y+",deltaY="+deltaY);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			if(deltaX == 0){
				if(deltaY > 0){
					while(mParams.y <= mScreenHeight){
						mParams.y = mParams.y + 10;
						publishProgress();
						try {
							Thread.sleep(time);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else{
					while(mParams.y >= 0 ){
						mParams.y = mParams.y - 10;
						publishProgress();
						try {
							Thread.sleep(time);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				if(deltaX < 0){
					while(mParams.x >= 0){
						mParams.x = mParams.x - 10;
						publishProgress();
						try {
							Thread.sleep(time);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else{
					while(mParams.x <= mScreenWidth){
						mParams.x = mParams.x + 10;
						publishProgress();
						try {
							Thread.sleep(time);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			return null;
		}
		
		
		@Override
		protected void onProgressUpdate(Void... values) {
			mWM.updateViewLayout(FloatView.this, mParams);
		}
	}
	
	private Handler mMyHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			SLog.d("x = "+msg.arg1);
			SLog.d("y = "+msg.arg2);
			mParams.x = msg.arg1;
			mParams.y = msg.arg2;
			mWM.updateViewLayout(FloatView.this, mParams);
		};
	};
	
	public void show(){
	    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	    params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	    params.format = PixelFormat.TRANSPARENT;
	    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	    params.gravity = Gravity.LEFT | Gravity.TOP;
	    params.type = WindowManager.LayoutParams.TYPE_PHONE;
	    this.setLayoutParams(params);
	    mWM.addView(this, params);
	}
	
	public void gone(){
		mWM.removeView(this);
	}
	
	private class ExpandAnimation extends Animation{
		private int mStart;
		private int mEnd;
		private FrameLayout.LayoutParams params;
		
		public ExpandAnimation(int left,int right){
//			params = (FrameLayout.LayoutParams)mButtonLL.getLayoutParams();
			if(left > right){
				params = (FrameLayout.LayoutParams)mIV.getLayoutParams();
//				mStart = params.
			}else{
				
			}
			mStart = params.leftMargin;
			String state = mIV.getTag().toString();
//			SLog.d("ExpandAnim:state="+state);
			mEnd = state.equals("1") ? mContext.getResources().getDimensionPixelSize(R.dimen.test_dimen1) : mContext.getResources().getDimensionPixelSize(R.dimen.test_dimen2);
		}
		
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if(interpolatedTime < 1.0f){
				params.leftMargin = mStart + (int)((mEnd - mStart) * interpolatedTime);
				mChildView.requestLayout();
			}
		}
	}
}
