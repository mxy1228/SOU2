package com.xmy.sou.viewcache;

import com.xmy.sou.R;

import android.view.View;
import android.widget.TextView;

public class SuggestionViewCache {

	private TextView mDevTV;
	private TextView mUserTV;
	private View mView;
	
	public SuggestionViewCache(View view){
		this.mView = view;
	}

	public TextView getmDevTV() {
		if(mDevTV == null){
			mDevTV = (TextView)mView.findViewById(R.id.suggestion_item_dev_tv);
		}
		return mDevTV;
	}

	public TextView getmUserTV() {
		if(mUserTV == null){
			mUserTV = (TextView)mView.findViewById(R.id.suggestion_item_user_tv);
		}
		return mUserTV;
	}
	
	
}
