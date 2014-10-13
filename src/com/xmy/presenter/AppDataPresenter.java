package com.xmy.presenter;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.xmy.handler.IAppDataHandler;
import com.xmy.sou.db.AppDao;
import com.xmy.sou.log.SLog;

public class AppDataPresenter {

	private AppDao mDao;
	private ReadWriteLock mLock = new ReentrantReadWriteLock();
	private Lock mWriteLock = mLock.writeLock();
	private Lock mReadLock = mLock.readLock();
	private IAppDataHandler mHandler;
	
	public AppDataPresenter(IAppDataHandler handler,Context ctx){
		this.mHandler = handler;
		this.mDao = new AppDao(ctx);
	}
	
	/**
	 * 增加或更新APPINFO
	 * @param info
	 */
	public void saveOrUpdate(PackageInfo info){
		mWriteLock.lock();
		try {
			mDao.saveOrUpdate(info);
		} catch (Exception e) {
			SLog.e(e);
		}finally{
			mWriteLock.unlock();
		}
	}
	
	/**
	 * 判断数据库是否为空
	 */
	public void isEmpty(){
		mReadLock.lock();
		try {
			mHandler.isEmpty(mDao.isEmpty());
		} catch (Exception e) {
			SLog.e(e);
			mHandler.isEmpty(false);
		}finally{
			mReadLock.unlock();
		}
	}
	
	/**
	 * 将一组APP数据存储到DB中
	 * @param list
	 */
	public void saveList(List<PackageInfo> list){
		mWriteLock.lock();
		try {
			mDao.saveList(list);
		} catch (Exception e) {
			SLog.e(e);
		}finally{
			mWriteLock.unlock();
		}
	}
	
	/**
	 * 根据包名删除APPINGO
	 * @param packageName
	 */
	public void delelte(String packageName){
		mWriteLock.lock();
		try {
			mDao.delelte(packageName);
		} catch (Exception e) {
			SLog.e(e);
		}finally{
			mWriteLock.unlock();
		}
	}
	
	/**
	 * 根据关键字搜索APPINFO
	 * @param key
	 * @return
	 */
	public void search(String key){
		mReadLock.lock();
		try {
			mHandler.onSearchResult(mDao.search(key));
		} catch (Exception e) {
			SLog.e(e);
			mHandler.onSearchResult(null);
		}finally{
			mReadLock.unlock();
		}
	}
}
