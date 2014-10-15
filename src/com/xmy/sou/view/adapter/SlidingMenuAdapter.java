package com.xmy.sou.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xmy.sou.R;
import com.xmy.sou.entity.SlidingMenuItem;
import com.xmy.sou.viewcache.SlidingMenuViewCache;

public class SlidingMenuAdapter extends BaseAdapter {

	private Context mCtx;
	private List<SlidingMenuItem> mData;
	private LayoutInflater mInflater;
	
	public SlidingMenuAdapter(Context ctx,List<SlidingMenuItem> data){
		this.mCtx = ctx;
		this.mData = data;
		this.mInflater = (LayoutInflater)mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SlidingMenuViewCache cache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.slidingmenu_item, null);
			cache = new SlidingMenuViewCache(convertView);
			convertView.setTag(cache);
		}else{
			cache = (SlidingMenuViewCache)convertView.getTag();
		}
		SlidingMenuItem item = mData.get(position);
		cache.getmTV().setText(item.getmTitle());
		return convertView;
	}

	
}
