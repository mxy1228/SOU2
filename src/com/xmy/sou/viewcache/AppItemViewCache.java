package com.xmy.sou.viewcache;

import com.xmy.sou.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AppItemViewCache {

	private View mView;
	private ImageView mIconIV;
	private TextView mNameTV;
	public AppItemViewCache(View view){
		this.mView = view;
	}
	public ImageView getmIconIV() {
		if(mIconIV == null){
			mIconIV = (ImageView)mView.findViewById(R.id.app_item_icon_iv);
		}
		return mIconIV;
	}
	public TextView getmNameTV() {
		if(mNameTV == null){
			mNameTV = (TextView)mView.findViewById(R.id.app_item_name_tv);
		}
		return mNameTV;
	}
	
	
	
}
