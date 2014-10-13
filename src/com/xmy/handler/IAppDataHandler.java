package com.xmy.handler;

import java.util.List;

import com.xmy.sou.entity.AppInfo;

public interface IAppDataHandler {

	public void isEmpty(boolean empty);

	public void onSearchResult(List<AppInfo> result);
}
