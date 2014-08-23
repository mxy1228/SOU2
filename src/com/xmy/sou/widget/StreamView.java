package com.xmy.sou.widget;

import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.xmy.sou.log.SLog;

public class StreamView extends FrameLayout {

	private static final int DURATION = 10 * 1000;
	private static final int FADE_DURATION = 3 * 1000;
	
	private int[] mRes;
	private ImageView[] mIVs;
	private Context mContext;
	private Handler mHandler;
	
	private float mMinFloat = 1.2f;
	private float mMaxFloat = 1.9f;
	private int mCurFloatIndex = -1;
	
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
		mIVs = new ImageView[2];
		ImageView iv1 = new ImageView(mContext);
		FrameLayout.LayoutParams param1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
				, FrameLayout.LayoutParams.MATCH_PARENT);
		iv1.setLayoutParams(param1);
		iv1.setScaleType(ScaleType.CENTER_CROP);
		mIVs[0] = iv1;
		this.addView(iv1);
		ImageView iv2 = new ImageView(mContext);
		FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
				, FrameLayout.LayoutParams.MATCH_PARENT);
		iv2.setLayoutParams(param2);
		iv2.setScaleType(ScaleType.CENTER_CROP);
		mIVs[1] = iv2;
		this.addView(iv2);
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
		if(mCurFloatIndex == -1){
			mCurFloatIndex = 1;
			anim(mIVs[mCurFloatIndex]);
			return;
		}
		int index = mCurFloatIndex;
		mCurFloatIndex = (1 + mCurFloatIndex) % mIVs.length;
		
		ImageView curIV = mIVs[index];
		ImageView nextIV = mIVs[mCurFloatIndex];
		nextIV.setAlpha(0.0f);
		anim(nextIV);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(FADE_DURATION);
		animatorSet.playTogether(ObjectAnimator.ofFloat(curIV, "alpha", 1.0f , 0.0f)
				, ObjectAnimator.ofFloat(nextIV, "alpha", 0.0f,1.0f));
		animatorSet.start();
	}
	
	private float getScale(){
		return this.mMinFloat + (new Random().nextFloat() * (this.mMaxFloat - this.mMinFloat));
	}
	
	private float getTranslation(int value,float ratio){
		return value * (ratio - 1.0f) * (new Random().nextFloat() - 0.5f);
	}
	
	@SuppressLint("NewApi")
	private void anim(ImageView iv){
		float fromFloat = getScale();
		float toFloat = getScale();
		float fromFloatX = getTranslation(iv.getWidth(), fromFloat);
		float fromFloatY = getTranslation(iv.getHeight(), fromFloat);
		float toFloatX = getTranslation(iv.getWidth(), toFloat);
		float toFloatY = getTranslation(iv.getHeight(), toFloat);
		iv.setScaleX(fromFloat);
		iv.setScaleY(fromFloat);
		iv.setTranslationX(fromFloatX);
		iv.setTranslationY(fromFloatY);
		ViewPropertyAnimator propertyAnim = iv.animate().translationX(toFloatX).translationY(toFloatY).scaleX(toFloat).scaleY(toFloat).setDuration(DURATION);
		propertyAnim.start();
		propertyAnim.setListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				mHandler.post(myRunnalble);
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	/**
	 * 设置图片资源
	 * @param res
	 */
	public void setResource(int[] res){
		mRes = res;
		for(int i=0;i<res.length;i++){
			mIVs[i].setImageResource(mRes[i]);
		}
	}
	
	private Runnable myRunnalble = new Runnable() {
		
		@Override
		public void run() {
			startStreamAnimation();
		}
	};

}
