package com.xmy.sou.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xmy.sou.R;
import com.xmy.sou.entity.AppInfo;
import com.xmy.sou.log.SLog;
import com.xmy.sou.viewcache.AppItemViewCache;

public class AppListAdapter extends BaseAdapter {

	private Context mContext;
	private List<AppInfo> mData;
	
	public AppListAdapter(Context context,List<AppInfo> data){
		this.mContext = context;
		this.mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		AppItemViewCache viewCache = null;
		if(arg1 == null){
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.app_item, null);
			viewCache = new AppItemViewCache(arg1);
			arg1.setTag(viewCache);
		}else{
			viewCache = (AppItemViewCache)arg1.getTag();
		}
		AppInfo info = mData.get(arg0);
		if(info != null){
			try {
				viewCache.getmIconIV().setImageDrawable(mContext.getPackageManager().getApplicationIcon(info.getPackageName()));
				viewCache.getmNameTV().setText(info.getName());
			} catch (Exception e) {
				SLog.e(e);
			}
		}
		return arg1;
	}
	
	public void changeData(List<AppInfo> data){
		this.mData = data;
		AppListAdapter.this.notifyDataSetChanged();
	}

}
