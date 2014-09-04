package com.xmy.sou.widget;

import java.io.InputStream;

import com.xmy.sou.log.SLog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class StreamView extends FrameLayout {

	private static final int DURATION = 15 * 1000;
	private static final int FADE_DURATION = 3 * 1000;
	
	private int[] mRes;
//	private ImageView[] mIVs;
	private Context mContext;
	private Handler mHandler;
	private ImageView mIV;
	
	private float mMinFloat = 1.1F;
	private float mMaxFloat = 1.9F;
//	private int mCurFloatIndex = -1;
	
	public StreamView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	public StreamView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	public StreamView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}
	
	private void init(){
//		mIVs = new ImageView[2];
		mIV = new ImageView(mContext);
		FrameLayout.LayoutParams param1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
				, FrameLayout.LayoutParams.MATCH_PARENT);
		mIV.setLayoutParams(param1);
		mIV.setScaleType(ScaleType.FIT_XY);
//		mIVs[0] = iv1;
		this.addView(mIV);
//		ImageView iv2 = new ImageView(mContext);
//		FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
//				, FrameLayout.LayoutParams.MATCH_PARENT);
//		iv2.setLayoutParams(param2);
//		iv2.setScaleType(ScaleType.CENTER_CROP);
//		mIVs[1] = iv2;
//		this.addView(iv2);
		this.mHandler = new Handler();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		start();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mHandler.removeCallbacks(myRunnalble);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
	
	private void start(){
		mHandler.post(myRunnalble);
	}
	
	/**
	 * 开始流动画
	 */
	private void startStreamAnimation(){
//		if(mCurFloatIndex == -1){
//			mCurFloatIndex = 1;
//			anim(mIVs[mCurFloatIndex]);
//			return;
//		}
//		int index = mCurFloatIndex;
//		mCurFloatIndex = (1 + mCurFloatIndex) % mIVs.length;
//		
//		ImageView curIV = mIVs[index];
//		ImageView nextIV = mIVs[mCurFloatIndex];
//		nextIV.setAlpha(0.0f);
		anim(mIV);
//		AnimatorSet animatorSet = new AnimatorSet();
//		animatorSet.setDuration(FADE_DURATION);
//		animatorSet.playTogether(ObjectAnimator.ofFloat(curIV, "alpha", 1.0f , 0.0f)
//				, ObjectAnimator.ofFloat(nextIV, "alpha", 0.0f,1.0f));
//		animatorSet.start();
	}
	
//	private float getScale(){
//		return this.mMinFloat + (new Random().nextFloat() * (this.mMaxFloat - this.mMinFloat));
//	}
	
//	private float getTranslation(int value,float ratio){
//		return value * (ratio - 1.0f) * (new Random().nextFloat() - 0.5f);
//	}
	
	@SuppressLint("NewApi")
	private void anim(ImageView iv){
		WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
//		float fromScale = getScale();
//		float toScale = getScale();
		float fromFloatX = 0;
//				getTranslation(iv.getWidth(), fromScale);
		float fromFloatY = 0;
//				getTranslation(iv.getHeight(), fromScale);
		float toFloatX = 0;
//				getTranslation(iv.getWidth(), toFloat);
		float toFloatY = 0 - mIV.getHeight() + wm.getDefaultDisplay().getHeight();
//				getTranslation(iv.getHeight(), toFloat);
//		iv.setScaleX(fromScale);
//		iv.setScaleY(fromScale);
		iv.setTranslationX(fromFloatX);
		iv.setTranslationY(fromFloatY);
		ViewPropertyAnimator propertyAnim = iv.animate()
				.translationX(toFloatX)
				.translationY(toFloatY)
//				.scaleX(toScale)
//				.scaleY(toScale)
				.setDuration(DURATION);
		propertyAnim.start();
//		propertyAnim.setListener(new AnimatorListener() {
//			
//			@Override
//			public void onAnimationStart(Animator animation) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animator animation) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animator animation) {
//				mHandler.post(myRunnalble);
//			}
//			
//			@Override
//			public void onAnimationCancel(Animator animation) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}
	
	
	/**
	 * 设置图片资源
	 * @param res
	 */
	public void setResource(int res){
//		mRes = res;
//		for(int i=0;i<res.length;i++){
//			mIVs[i].setImageResource(mRes[i]);
//		}
		try {
//			InputStream is = mContext.getAssets().open("sky.jpg");
//			Bitmap d = new BitmapDrawable(is).getBitmap();
//			Bitmap scaled = Bitmap.createScaledBitmap(d, 512, d.getHeight()/2, true);
			mIV.setImageResource(res);
		} catch (Exception e) {
			SLog.e(e);
		}
		
	}
	
	private Runnable myRunnalble = new Runnable() {
		
		@Override
		public void run() {
			startStreamAnimation();
		}
	};

}
