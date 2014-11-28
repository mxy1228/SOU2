package com.xmy.sou.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.fb.model.Reply;
import com.xmy.handler.ISuggestionHandler;
import com.xmy.presenter.SuggestionPresenter;
import com.xmy.sou.R;
import com.xmy.sou.view.adapter.SuggestionAdapter;
import com.xmy.sou.widget.WaitingDialog;

public class SuggesttionActivity extends BaseActivity implements OnClickListener,ISuggestionHandler{

	private ListView mLV;
	private EditText mET;
	private Button mSendBtn;
	private WaitingDialog mWaitingDialog;
	
	private List<Reply> mData;
	private SuggestionPresenter mPresenter;
	private SuggestionAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.suggestion);
		initView();
		initData();
		initEvent();
	}
	
	@Override
	protected void initView() {
		this.mLV = (ListView)findViewById(R.id.suggestion_lv);
		this.mET = (EditText)findViewById(R.id.suggestion_et);
		this.mSendBtn = (Button)findViewById(R.id.suggestion_btn);
		this.mWaitingDialog = new WaitingDialog(this);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void initData() {
		this.mData = new ArrayList<Reply>();
		this.mPresenter = new SuggestionPresenter(this, SuggesttionActivity.this);
		this.mAdapter = new SuggestionAdapter(SuggesttionActivity.this, mData);
		this.mLV.setAdapter(mAdapter);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			SuggesttionActivity.this.finish();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void initEvent() {
		this.mSendBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.suggestion_btn:
			String content = mET.getText().toString();
			if(!TextUtils.isEmpty(content)){
				this.mPresenter.sendSuggestion(mET.getText().toString());
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void sendSuggestion(String content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendUserReply(List<Reply> list) {
		this.mData.clear();
		this.mData.addAll(list);
		this.mAdapter.notifyDataSetChanged();
		this.mET.setText("");
		showShortTaost(R.string.thx_feedback);
		//隐藏软键盘
		hideSoftInput();
	}

	@Override
	public void onReceiveDevReply(List<Reply> list) {
		this.mData.clear();
		this.mData.addAll(list);
		this.mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onInitReplyList(List<Reply> list) {
		mData.clear();
		mData.addAll(list);
		this.mAdapter.notifyDataSetChanged();
	}

	@Override
	public void showWaitingDialog() {
		if(!this.isFinishing()){
			this.mWaitingDialog.show();
		}
	}

	@Override
	public void dissmisWaitingDialog() {
		if(!this.isFinishing()){
			this.mWaitingDialog.dismiss();
		}
	}

}
