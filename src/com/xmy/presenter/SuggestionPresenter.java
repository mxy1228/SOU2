package com.xmy.presenter;

import java.util.List;

import android.content.Context;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.DevReply;
import com.umeng.fb.model.Reply;
import com.xmy.handler.ISuggestionHandler;

public class SuggestionPresenter {

	private ISuggestionHandler mHandler;
	private Conversation mConversation;
	private FeedbackAgent mAgent;
	private Context mCtx;
	
	public SuggestionPresenter(ISuggestionHandler handler,Context ctx){
		this.mHandler = handler;
		this.mAgent = new FeedbackAgent(ctx);
		this.mConversation = this.mAgent.getDefaultConversation();
		this.mCtx = ctx;
	}
	
	/**
	 * 发送建议
	 * @param content
	 */
	public void sendSuggestion(String content){
		mHandler.showWaitingDialog();
		mConversation.addUserReply(content);
		sync();
	}
	
	/**
	 * 初始化建议数据
	 */
	public void initConversation(){
		mHandler.onInitReplyList(mConversation.getReplyList());
	}
	
	public void sync(){
		Conversation.SyncListener listener = new Conversation.SyncListener() {
			
			@Override
			public void onSendUserReply(List<Reply> arg0) {
				mHandler.dissmisWaitingDialog();
				mHandler.onSendUserReply(mConversation.getReplyList());
			}
			
			@Override
			public void onReceiveDevReply(List<DevReply> arg0) {
				mHandler.onReceiveDevReply(mConversation.getReplyList());
			}
		};
		mConversation.sync(listener);
	}
}
