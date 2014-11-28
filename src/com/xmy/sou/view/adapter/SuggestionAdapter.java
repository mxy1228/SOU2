package com.xmy.sou.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.umeng.fb.model.DevReply;
import com.umeng.fb.model.Reply;
import com.xmy.sou.R;
import com.xmy.sou.viewcache.SuggestionViewCache;

public class SuggestionAdapter extends BaseAdapter {

	private List<Reply> mData;
	private Context mCtx;
	private LayoutInflater mInfalter;
	
	public SuggestionAdapter(Context ctx,List<Reply> data){
		this.mCtx = ctx;
		this.mData = data;
		this.mInfalter = (LayoutInflater)mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		SuggestionViewCache cache = null;
		if(convertView == null){
			convertView = mInfalter.inflate(R.layout.suggestion_item, null);
			cache = new SuggestionViewCache(convertView);
			convertView.setTag(cache);
		}else{
			cache = (SuggestionViewCache)convertView.getTag();
		}
		Reply reply = mData.get(position);
		if(reply instanceof DevReply){
			cache.getmDevTV().setVisibility(View.VISIBLE);
			cache.getmUserTV().setVisibility(View.GONE);
			cache.getmDevTV().setText(reply.getContent());
		}else{
			cache.getmDevTV().setVisibility(View.GONE);
			cache.getmUserTV().setVisibility(View.VISIBLE);
			cache.getmUserTV().setText(reply.getContent());
		}
		return convertView;
	}

}
