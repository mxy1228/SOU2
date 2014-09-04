package com.xmy.sou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.xmy.sou.R;

public class LanchProgressView extends View {

	private static final int WHITE = 0;
	private static final int BLACK = 1;
	private static final int PADDING = 80;
	
	private Paint mGreenPaint;
	private Paint mLightBluePaint;
	private Paint mBluePaint;
	private Paint mPurplePaint;
	private Paint mRoseRedPaint;
	private Paint mYellowPaint;
	private Paint mOrangePaint;
	private Paint mRedPaint;
	private Paint mWhitePaint;
	
	private int mTextSize;
	private Context mContext;
	private RectF mRectF;
	private int mProgress;
	private int mDrawColor;
	private int mScreenWidth;
	
	public LanchProgressView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	
	public LanchProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	public LanchProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	private void init(){
		this.mTextSize = mContext.getResources().getDimensionPixelSize(R.dimen.progress_text_size);
		mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		float left = PADDING;
		float right = mScreenWidth - (PADDING);
		this.mRectF = new RectF(left, left, right, right);
		this.mGreenPaint = new Paint();
		initPaint(mGreenPaint, "#73bb0a");
		this.mLightBluePaint = new Paint();
		initPaint(mLightBluePaint, "#00a6ad");
		this.mBluePaint = new Paint();
		initPaint(mBluePaint, "#1983b6");
		this.mPurplePaint = new Paint();
		initPaint(mPurplePaint, "#8a3b85");
		this.mRoseRedPaint = new Paint();
		initPaint(mRoseRedPaint, "#cb3e73");
		this.mYellowPaint = new Paint();
		initPaint(mYellowPaint, "#f4ad00");
		this.mOrangePaint = new Paint();
		initPaint(mOrangePaint, "#fe770f");
		this.mRedPaint = new Paint();
		initPaint(mRedPaint, "#eb5345");
		
		this.mWhitePaint = new Paint();
		this.mWhitePaint.setAntiAlias(true);
		this.mWhitePaint.setColor(Color.parseColor("#FFFFFF"));
		this.mWhitePaint.setStyle(Style.STROKE);
		this.mWhitePaint.setStrokeWidth(70);
//		new Thread(new MyThread()).start();
		
	}
	
	private void initPaint(Paint paint,String color){
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor(color));
		paint.setTextSize(80);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(100);
		paint.setTextAlign(Align.CENTER);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		int width = getWidth();
//		SLog.d("progressView width = "+width+" and rectf width = "+mRectF.width());
//		int height = getHeight();
//		SLog.d("progressView height = "+height+" and rectf height = "+mRectF.height());
//		canvas.drawRect(mRectF, mGreenPaint);
//		Paint p1 = mDrawColor == WHITE ? mBlackPaint : mWhitePaint;
		canvas.drawArc(mRectF, 0, 45, false, mGreenPaint);
		canvas.drawArc(mRectF, 45, 45, false, mLightBluePaint);
		canvas.drawArc(mRectF, 90, 45, false, mBluePaint);
		canvas.drawArc(mRectF, 135, 45, false, mPurplePaint);
		canvas.drawArc(mRectF, 180, 45, false, mRoseRedPaint);
		canvas.drawArc(mRectF, 225, 45, false, mYellowPaint);
		canvas.drawArc(mRectF, 270, 45, false, mOrangePaint);
		canvas.drawArc(mRectF, 315, 45, false, mRedPaint);
		
//		Paint p2 = mDrawColor == WHITE ? mWhitePaint : mBlackPaint;
//		canvas.drawArc(mRectF, -90, mProgress * 3.6f, false, p2);
//		TextPaint textP = new TextPaint();
//		float textWidth = textP.measureText(""+mProgress);
//		float x = (mRectF.right - textWidth) / 2;
//		FontMetrics fm = mWhitePaint.getFontMetrics();
//		float y = mRectF.top + (mRectF.bottom - mRectF.top) / 2 - (fm.bottom - fm.top) / 2 + Math.abs(fm.top);
//		canvas.drawText(""+mProgress, x, y, mWhitePaint);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(mScreenWidth, mScreenWidth);
	}
	
	private class MyThread implements Runnable{

		@Override
		public void run() {
			try {
				while(true){
					mProgress++;
					if(mProgress > 100){
						mDrawColor = mDrawColor == WHITE ? BLACK : WHITE;
						mProgress = 0;
					}
					postInvalidate();
					Thread.sleep(30);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public int getMyWidth(){
		return (int)(mRectF.width()+mTextSize);
	}
	
	public int getMyHeight(){
		return (int)(mRectF.width()+mTextSize);
	}
}
