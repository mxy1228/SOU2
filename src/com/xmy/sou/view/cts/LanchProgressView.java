package com.xmy.sou.view.cts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xmy.sou.R;
import com.xmy.sou.log.SLog;

public class LanchProgressView extends View {

	private static final int WHITE = 0;
	private static final int BLACK = 1;
	
	private Paint mWhitePaint;
	private Paint mBlackPaint;
	private int mTextSize;
	private Context mContext;
	private RectF mRectF;
	private int mProgress;
	private int mDrawColor;
	
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
		float left = mTextSize * 0.5f;
		float right = mTextSize * 4.5f;
		this.mRectF = new RectF(left, left, right, right);
		this.mWhitePaint = new Paint();
		this.mWhitePaint.setAntiAlias(true);
		this.mWhitePaint.setColor(Color.parseColor("#FFFFFF"));
		this.mWhitePaint.setTextSize(160);
		this.mWhitePaint.setStyle(Style.STROKE);
		this.mWhitePaint.setStrokeWidth(mTextSize * 0.5f);
		this.mWhitePaint.setTextAlign(Align.CENTER);
		
		this.mBlackPaint = new Paint();
		this.mBlackPaint.setAntiAlias(true);
		this.mBlackPaint.setColor(Color.parseColor("#0D91F7"));
		this.mBlackPaint.setTextSize(mTextSize * 0.5f);
		this.mBlackPaint.setStyle(Style.STROKE);
		this.mBlackPaint.setStrokeWidth(mTextSize * 0.5f);
		new Thread(new MyThread()).start();
		
//		setBackgroundColor(Color.BLACK);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		int width = getWidth();
//		SLog.d("progressView width = "+width+" and rectf width = "+mRectF.width());
//		int height = getHeight();
//		SLog.d("progressView height = "+height+" and rectf height = "+mRectF.height());
//		canvas.drawRect(mRectF, mWhitePaint);
		Paint p1 = mDrawColor == WHITE ? mBlackPaint : mWhitePaint;
		canvas.drawArc(mRectF, 0, 360, false, p1);
		Paint p2 = mDrawColor == WHITE ? mWhitePaint : mBlackPaint;
		canvas.drawArc(mRectF, -90, mProgress * 3.6f, false, p2);
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
		setMeasuredDimension((int)(mRectF.width()+mTextSize), (int)(mRectF.width()+mTextSize));
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
