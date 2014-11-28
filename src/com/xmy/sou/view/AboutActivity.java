package com.xmy.sou.view;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.xmy.sou.R;

public class AboutActivity extends BaseActivity {

	private TextView mVersionTV;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.about);
		initView();
		initData();
		initEvent();
	}
	
	@Override
	protected void initView() {
		getSupportActionBar().setHomeButtonEnabled(true);
		this.mVersionTV = (TextView)findViewById(R.id.about_version_tv);
	}

	@Override
	protected void initData() {
		try {
			PackageManager pm = getPackageManager();
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			this.mVersionTV.setText(getString(R.string.version, info.versionName));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			AboutActivity.this.finish();
			break;

		default:
			break;
		}
		return true;
	}
}
