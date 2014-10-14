package com.xmy.handler;

import java.util.List;

import com.umeng.fb.model.Reply;

public interface ISuggestionHandler {

	/**
	 * 发送建议
	 * @param content
	 */
	public void sendSuggestion(String content);
	
	/**
	 * 用户发送建议成功
	 * @param list
	 */
	public void onSendUserReply(List<Reply> list);
	
	/**
	 * 接收到开发者回复
	 * @param list
	 */
	public void onReceiveDevReply(List<Reply> list);
	
	/**
	 * 初始化建议列表数据
	 * @param list
	 */
	public void onInitReplyList(List<Reply> list);
}
