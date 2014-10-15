package com.xmy.sou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xmy.sou.R;

public class SlidingMenuViewCache {

	private View mView;
	private TextView mTV;
	
	public SlidingMenuViewCache(View view){
		this.mView = view;
	}

	public TextView getmTV() {
		if(mTV == null){
			mTV = (TextView)mView.findViewById(R.id.menu_item_tv);
		}
		return mTV;
	}
	
	
}
