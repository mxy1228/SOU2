package com.xmy.sou.view.cts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.xmy.sou.R;
import com.xmy.sou.log.SLog;

public class CYMGFlowWindow extends LinearLayout implements OnClickListener,OnTouchListener{

	//悬浮窗为合拢状态
	private static final int CLOSE = 0;
	//悬浮窗为展开状态
	private static final int EXPAND = 1;
	//悬浮窗磁性吸附到屏幕最上和最下判断的默认距离
	private static final int DEFAULT_MAGNETIC = 300;
	//磁性吸附时每频率移动距离
	private static final int PER_MAGNETIC_DISTANCE = 10;
	//磁性移动速率
	private static final int PER_MAGNETIC_TIME = 5;
	
	private Context mContext;
	private WindowManager mWM;
	private WindowManager.LayoutParams mParams;
	private int mScreenWidth;
	private int mScreenHeight;
	private int mXInView;
	private int mYInView;
	private float mPressDownX;
	private float mPressDownY;
	private int mState;
	
//	private View mChildView;
	private ImageButton mIBtn;
	private ImageButton mPersonalLeftIBtn;
	private ImageButton mPersonalRightIBtn;
	private ImageButton mForumLeftIBtn;
	private ImageButton mForumRightIBtn;
	private ImageButton mCtsLeftIBtn;
	private ImageButton mCtsRightIBtn;
	private LinearLayout mLeftLL;
	private LinearLayout mRightLL;
	private FrameLayout mContainer;
	private View mRightView;
	private View mLeftView;
	
	public CYMGFlowWindow(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	public CYMGFlowWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	private void init(){
		SLog.d("init");
		
		this.mWM = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.mIBtn = new ImageButton(mContext);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.cymg_flow_win_size)
				, mContext.getResources().getDimensionPixelSize(R.dimen.cymg_flow_win_size));
		this.mIBtn.setLayoutParams(params);
		this.mIBtn.setBackgroundResource(R.drawable.cymg_flow_win_btn);
//		this.mIBtn.setBackgroundColor(Color.parseColor("#66000000"));
		this.addView(mIBtn);
		this.mIBtn.setOnTouchListener(this);
		
		mRightView = inflate(mContext, R.layout.cymg_flow_win_right, null);
		mLeftView = inflate(mContext, R.layout.cymg_flow_win_left, null);
//		this.mChildView = inflate(mContext, AccResource.getInstance(mContext).cymg_flow_win, null);
//		this.mContainer = (FrameLayout)mChildView.findViewById(res.cymg_flow_win_container);
		
//		this.mLeftLL = (LinearLayout)inflate(mContext, res.cymg_flow_win_left, null);
//		this.addView(mLeftLL);
		
//		this.mIBtn = (ImageButton)inflate(mContext, res.cymg_flow_win, null);
//		this.mIBtn.setOnTouchListener(this);
//		this.addView(mIBtn);
//		this.mIBtn.setTag(CLOSE);
		
//		this.mRightLL = (LinearLayout)inflate(mContext, res.cymg_flow_win_right, null);
//		this.mRightLL.setOrientation(LinearLayout.HORIZONTAL);
//		LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
//				, LinearLayout.LayoutParams.WRAP_CONTENT);
//		this.mLeftLL.setLayoutParams(rightParams);
//		this.addView(mRightLL);
		
//		this.mIBtn = (ImageButton)mChildView.findViewById(res.cymg_flow_win_ibtn);
		//默认设置悬浮窗为收拢状态
		
//		this.mPersonalLeftIBtn = (ImageButton)mChildView.findViewById(res.cymg_flow_win_personal_left_ibtn);
//		this.mPersonalLeftIBtn.setOnClickListener(this);
//		this.mPersonalRightIBtn = (ImageButton)mChildView.findViewById(res.cymg_flow_win_personal_right_ibtn);
//		this.mPersonalRightIBtn.setOnClickListener(this);
//		this.mForumLeftIBtn = (ImageButton)mChildView.findViewById(res.cymg_flow_win_forum_left_ibtn);
//		this.mForumLeftIBtn.setOnClickListener(this);
//		this.mForumRightIBtn = (ImageButton)mChildView.findViewById(res.cymg_flow_win_forum_right_ibtn);
//		this.mForumRightIBtn.setOnClickListener(this);
//		this.mCtsLeftIBtn = (ImageButton)mChildView.findViewById(res.cymg_flow_win_cts_left_ibtn);
//		this.mCtsLeftIBtn.setOnClickListener(this);
//		this.mCtsRightIBtn = (ImageButton)mChildView.findViewById(res.cymg_flow_win_cts_right_ibtn);
//		this.mCtsRightIBtn.setOnClickListener(this);
		
//		this.mLeftLL = (LinearLayout)mChildView.findViewById(res.cymg_flow_win_left_ll);
//		this.mRightLL = (LinearLayout)mChildView.findViewById(res.cymg_flow_win_right_ll);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
//				, LinearLayout.LayoutParams.WRAP_CONTENT);
//		this.mChildView.setLayoutParams(params);
//		this.addView(mChildView);
		//计算屏幕的尺寸
		DisplayMetrics metrics = new DisplayMetrics();
		mWM.getDefaultDisplay().getMetrics(metrics);
		this.mScreenWidth = metrics.widthPixels;
		this.mScreenHeight = metrics.heightPixels;
	}
	
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		// TODO Auto-generated method stub
		return super.drawChild(canvas, child, drawingTime);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		SLog.d("onLayout:l="+l+",t="+t+",r="+r+",b="+b);
		if(mState == CLOSE){
			mIBtn.layout(l, t, r, b);
		}else{
			mIBtn.layout(r-mContext.getResources().getDimensionPixelSize(R.dimen.cymg_flow_win_size), t, r, b);
		}
//		if(mState == CLOSE){
//			mIBtn.layout(l, t, mContext.getResources().getDimensionPixelSize(R.dimen.cymg_flow_win_size), mContext.getResources().getDimensionPixelSize(R.dimen.cymg_flow_win_size));
//		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		SLog.d("onMeasure:width="+width+",height="+height);
		if(mState == CLOSE){
			setMeasuredDimension(mContext.getResources().getDimensionPixelSize(R.dimen.cymg_flow_win_size)
					, mContext.getResources().getDimensionPixelSize(R.dimen.cymg_flow_win_size));
		}else{
//			setBackgroundDrawable(null);
			setMeasuredDimension(500, mContext.getResources().getDimensionPixelSize(R.dimen.cymg_flow_win_size));
//			setBackgroundResource(R.drawable.cymg_flow_win_expand_right);
		}
	}
	
	/**
	 * 显示悬浮窗
	 */
	public void show(){
		SLog.d("show");
		mParams = new WindowManager.LayoutParams();
		mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mParams.format = PixelFormat.TRANSPARENT;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.gravity = Gravity.LEFT | Gravity.TOP;
		mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		this.setLayoutParams(mParams);
		mWM.addView(this, mParams);
	}
	
	/**
	 * 隐藏悬浮窗
	 */
	public void gone(){
		mWM.removeView(this);
	}

	@Override
	public void onClick(View v) {
		
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			//拦截ACTION_MOVE，交由Parent进行拖动
			return true;
		case MotionEvent.ACTION_UP:
			//不拦截ACTION_UP，由IBtn判断是否执行点击事件
			return false;
		case MotionEvent.ACTION_DOWN:
			//不拦截ACTION_DOWN，将ACTION_DOWN转交给IBtn，否则IBtn无法接收ACTION_UP事件
			mXInView = (int)ev.getX();
			mYInView = (int)ev.getY();
			return false;
		default:
			break;
		}
		//其他ACTION默认为不拦截
		return false;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//ACTION_DOWN会被parent拦截并传递给mIBtn，此时会记录下按下时的X/Y坐标，以备ACTION_UP时判断是否为点击
			//为了精确计算是否为点击，保持float精度不变
			mPressDownX = event.getRawX();
			mPressDownY = event.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			float pressUpX = event.getRawX();
			float pressUpY = event.getRawY();
			if(mPressDownX == pressUpX 
					&& mPressDownY == pressUpY){
				final int left = mParams.x;
				final int right = mScreenWidth - mParams.x;
				//如果ACTION_UP时的坐标和ACTION_DOWN的坐标一致，则表示此次交互为点击
				//下面处理点击事件的响应
//				ExpandAnimation anim = new ExpandAnimation();
//				anim.setDuration(300);
//				anim.setFillAfter(true);
//				anim.setAnimationListener(new AnimationListener() {
//					
//					@Override
//					public void onAnimationStart(Animation animation) {
//						FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIBtn.getLayoutParams();
//						if(left > right){
//							params.gravity = Gravity.RIGHT;
//							mLeftLL.setVisibility(View.VISIBLE);
//							mRightLL.setVisibility(View.GONE);
//						}else{
//							params.gravity = Gravity.LEFT;
//							mRightLL.setVisibility(View.VISIBLE);
//							mLeftLL.setVisibility(View.GONE);
//						}
//						mIBtn.setLayoutParams(params);
//					}
//					
//					@Override
//					public void onAnimationRepeat(Animation animation) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onAnimationEnd(Animation animation) {
//						if(mIBtn.getTag().toString().equals(CLOSE)){
//							mIBtn.setTag(EXPAND);
//						}else{
//							mIBtn.setTag(CLOSE);
//							mLeftLL.setVisibility(View.GONE);
//							mRightLL.setVisibility(View.GONE);
//						}
//					}
//				});
//				mContainer.startAnimation(anim);
				expand();
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//&ACTION_MASK是为了排除多点触控的干扰
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			
			return false;
		case MotionEvent.ACTION_UP:
			if(mState == EXPAND){
				//如果悬浮窗为展开状态，则不能拖动
				return false;
			}
			//下面计算悬浮窗目前位置距离屏幕上下左右的相对位置
			int top = (int)(event.getRawY() - event.getY());
			int left = (int)(event.getRawX() - event.getX());
			int right = (int)(mScreenWidth - left);
			int bottom = (int)(mScreenHeight - top);
			if(top < mScreenHeight / 2){
				//如果悬浮窗在屏幕上半部分
				if(top > DEFAULT_MAGNETIC){
					//如果距离屏幕顶部的距离大于默认磁性距离，则直接判断左右磁性
					if(left < right){
						//如果距离屏幕左边的距离小于距离屏幕右边的距离，则左边的磁性强，此时悬浮窗吸附到屏幕左侧
						//y轴坐标不变，X轴坐标变为0
						new MagneticMoveTask(0, mParams.y).execute();
					}else{
						//否则移动到屏幕右边，Y轴坐标不变，X轴坐标变为屏幕宽度
						new MagneticMoveTask(mScreenWidth, mParams.y).execute();
					}
				}else{
					//否则悬浮窗吸附到屏幕顶端,X轴坐标不变，Y轴坐标变为0
					new MagneticMoveTask(mParams.x, 0).execute();
				}
			}else{
				//如果悬浮窗在屏幕下半部分
				if(bottom > DEFAULT_MAGNETIC){
					//如果距离屏幕底部的距离大于默认磁性距离，则直接判断左右磁性
					if(left < right){
						//吸附到屏幕左侧，X轴变0，Y轴不变
						new MagneticMoveTask(0, mParams.y).execute();
					}else{
						//吸附到屏幕右侧，X轴变mScreenWidth，Y轴不变
						new MagneticMoveTask(mScreenWidth, mParams.y).execute();
					}
				}else{
					//吸附到屏幕底部，Y轴不变，X轴变为mScreenHeight
					new MagneticMoveTask(mParams.x, mScreenHeight).execute();
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(mState==CLOSE){
				//只有在收拢状态下才可以移动
				updatePostion((int)event.getRawX(), (int)event.getRawY());
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	/**
	 * 更新悬浮窗坐标
	 * @param x 目的坐标的X轴
	 * @param y 目的坐标的Y轴
	 */
	private void updatePostion(int x,int y){
		mParams.x = (int)(x - mXInView);
		mParams.y = (int)(y - mYInView);
		mWM.updateViewLayout(this, mParams);
	}
	
	/**
	 * 异步任务用于更新悬浮窗坐标
	 * @author xumengyang
	 *
	 */
	private class MagneticMoveTask extends AsyncTask<Void, Void, Void>{
		
		//目的X轴坐标
		private int pX;
		//目的Y轴坐标
		private int pY;
		//现X轴坐标与目的X轴坐标差值
		private int deltaX;
		//先Y轴坐标与目的Y轴坐标差值
		private int deltaY;
		
		public MagneticMoveTask(int x,int y){
			pX = x;
			pY = y;
		}
		
		@Override
		protected void onPreExecute() {
			//在磁性移动之前首先计算坐标差值
			//在拖动存在惯性（暂且这么叫吧）的情况下，会出现mParams.x为负数的情况，所以在这里取mParams.x的绝对值，下面的mParams.y也一样
			this.deltaX = pX - Math.abs(mParams.x);
			this.deltaY = pY - Math.abs(mParams.y);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			if(deltaX == 0){
				//若X轴的差值为0，则只需在Y轴移动即可
				if(deltaY > 0){
					//向下移动
					while(mParams.y <= mScreenHeight){
						mParams.y += PER_MAGNETIC_DISTANCE;
						publishProgress();
						try {
							Thread.sleep(PER_MAGNETIC_TIME);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else{
					//向上移动
					while(mParams.y >= 0){
						mParams.y -= PER_MAGNETIC_DISTANCE;
						publishProgress();
						try {
							Thread.sleep(PER_MAGNETIC_TIME);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				//若X轴差值不为0，则需要在X轴移动
				if(deltaX < 0){
					//向左移动
					while(mParams.x >= 0){
						mParams.x -= PER_MAGNETIC_DISTANCE;
						publishProgress();
						try {
							Thread.sleep(PER_MAGNETIC_TIME);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else{
					//向右移动
					while(mParams.x <= mScreenWidth){
						mParams.x += PER_MAGNETIC_DISTANCE;
						publishProgress();
						try {
							Thread.sleep(PER_MAGNETIC_TIME);
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
			//更新悬浮窗位置
			mWM.updateViewLayout(CYMGFlowWindow.this, mParams);
		}
	}
	
	/**
	 * 根据悬浮窗当前的位置判断功能条的展开与收起
	 */
	private void expand(){
		int left = mParams.x;
		int right = mScreenWidth - mParams.x;
//		String state = mIBtn.getTag().toString();
		if(mState == CLOSE){
			mState = EXPAND;
//			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT
//					, FrameLayout.LayoutParams.WRAP_CONTENT);
			if(left > right){
				ImageButton ibtn = (ImageButton)mLeftView.findViewById(R.id.cymg_flow_win_ibtn);
				ibtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mState = CLOSE;
						mWM.removeViewImmediate(mLeftView);
					}
				});
//				mWM.removeView(this);
				mWM.addView(mLeftView, mParams);
//				setBackgroundColor(Color.parseColor("#000000"));
//				this.setBackgroundResource(R.drawable.cymg_flow_win_expand_right);
//				this.removeView(mIBtn);
//				this.addView(mRightLL);
//				params.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
//				this.mIBtn.setLayoutParams(params);
//				this.addView(mIBtn);
				
				
				//如果左侧的空间大于右侧，则展开左侧功能条
				//此时需要将mIBtn设置成layout_gravity="right"
//				params.gravity = Gravity.RIGHT;
//				mIBtn.setLayoutParams(params);
//				mLeftLL.setBackgroundResource(mRes.cymg_flow_win_bg_left);
//				mLeftLL.setVisibility(View.VISIBLE);
//				mRightLL.setVisibility(View.GONE);
			}else{
//				mWM.removeView(this);
				mWM.addView(mLeftView, mParams);
//				this.setBackgroundResource(R.drawable.cymg_flow_win_expand_left);
				//否则的话就展开右侧功能条，需将mIBtn设置成layout_gravity="left"
//				params.gravity = Gravity.LEFT;
//				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//				mIBtn.setLayoutParams(params);
//				mRightLL.setBackgroundResource(mRes.cymg_flow_win_bg_right);
//				mRightLL.setVisibility(View.VISIBLE);
//				mLeftLL.setVisibility(View.GONE);
//				this.addView(mLeftLL);
//				this.removeView(mIBtn);
//				params.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
//				mIBtn.setLayoutParams(params);
//				this.addView(mIBtn);
			}
//			mIBtn.setTag(EXPAND);
		}else{
			
			
//			mWM.removeViewImmediate(mRightView);
//			mWM.addView(this, mParams);
//			setBackgroundColor(Color.parseColor("#000000"));
//			this.setBackgroundResource(R.drawable.cymg_flow_win_btn);
//			this.removeView(mLeftLL);
//			this.removeView(mRightLL);
//			mIBtn.setTag(CLOSE);
		}
	}
	
	private class ExpandAnimation extends Animation{
		
		private int mStart;
		private int mEnd;
		private int mSize;
		private LinearLayout.LayoutParams exParam;
		
		public ExpandAnimation(){
//			mSize = mContext.getResources().getDimensionPixelSize(AccResource.getInstance(mContext).cymg_flow_win_size);
			exParam = (LinearLayout.LayoutParams)mContainer.getLayoutParams();
			mStart = exParam.width;
//			String state = mIBtn.getTag().toString();
//			mEnd = state.equals(CLOSE) ? 500 : mSize;
		}
		
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			if(interpolatedTime < 1.0f){
				exParam.width = mStart + (int)((mEnd - mStart) * interpolatedTime);
				exParam.height = mSize;
				mContainer.requestLayout();
			}
		}
	}

}
