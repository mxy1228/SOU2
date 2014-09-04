package com.xmy.sou.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.xmy.event.SaveAppEvent;
import com.xmy.sou.entity.AppInfo;
import com.xmy.sou.log.SLog;
import com.xmy.sou.pinyin.HanziToPinyin;
import com.xmy.sou.pinyin.HanziToPinyin.Token;

import de.greenrobot.event.EventBus;

public class AppDao {

	public static final String TABLE_NAME = "app";
	
	public static class PROPERTIES{
		public static final String APP_NAME = "appname";
		public static final String PACKAGE_NAME = "packagename";
		public static final String VERSION_NAME = "versionname";
		public static final String VERSION_CODE = "versioncode";
		public static final String SORT_KEY = "sort_key";
		public static final String KEY = "key";
		public static final String ALL = APP_NAME+","+VERSION_NAME+","+VERSION_CODE+","+SORT_KEY+","+KEY+","+PACKAGE_NAME;
	}
	
	private DBHelper mDBHelper;
	private Context mContext;
	
	public AppDao(Context context){
		this.mDBHelper = new DBHelper(context);
		this.mContext = context;
	}
	
	/**
	 * 判断DB里面是否为空
	 * @return true - 空 ；false - 非空
	 */
	public boolean isEmpty(){
		SQLiteDatabase database = null;
		SQLiteStatement stm = null;
		try {
			database  = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+TABLE_NAME;
			stm = database.compileStatement(sql);
			long count = stm.simpleQueryForLong();
			return  count == 0;
		} catch (Exception e) {
			SLog.e(e);
		}finally{
			closeSavely(database, stm);
		}
		return false;
	}
	
	/**
	 * 保存应用信息到DB
	 * 功能描述：首先根据pakcageName查询DB中是否已经存在该APP的信息，若存在则进行Update操作，否则进行Insert操作
	 *
	 * @author 徐萌阳(xumengyang)
	 * @param @param info
	 * @return void
	 * @date 2014年7月22日 上午11:52:19
	 *
	 */
	public void saveOrUpdate(PackageInfo info){
		SQLiteDatabase database = null;
		SQLiteStatement stm = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String selectSql = "SELECT COUNT(*) FROM "+TABLE_NAME+" WHERE "+PROPERTIES.PACKAGE_NAME+" = ?";
			stm = database.compileStatement(selectSql);
			stm.bindString(1, info.packageName);
			long result = stm.simpleQueryForLong();
			String name = info.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
			StringBuilder pinyin = new StringBuilder();
			StringBuilder sortkey = new StringBuilder();
			ArrayList<Token> tokens = HanziToPinyin.getInstance().get(name);
			if(tokens != null && tokens.size() > 0){
				for(Token token : tokens){
					if(Token.PINYIN == token.type){
						pinyin.append(token.target.toLowerCase());
						sortkey.append(token.target.toLowerCase().subSequence(0, 1));
					}else{
						pinyin.append(token.source);
						sortkey.append(token.source.subSequence(0, 1));
					}
					
				}
			}
			String insertSql = "INSERT INTO "+TABLE_NAME+" ("+PROPERTIES.ALL+") VALUES (?,?,?,?,?,?)";
			String updateSql = "UPDATE "+TABLE_NAME+" SET "
			+PROPERTIES.APP_NAME+" = ? AND "
			+PROPERTIES.VERSION_NAME+" = ? AND "
			+PROPERTIES.VERSION_CODE+" = ? AND "
			+PROPERTIES.SORT_KEY+" = ? AND "
			+PROPERTIES.KEY+" = ? "
			+" WHERE "+PROPERTIES.PACKAGE_NAME+" = ?";
			stm = result == 0 ? database.compileStatement(insertSql) : database.compileStatement(updateSql);
			SLog.d("name="+name);
			stm.bindString(1, name);
			stm.bindString(2, info.versionName);
			stm.bindString(3, info.versionCode+"");
			stm.bindString(4, sortkey.toString());
			stm.bindString(5, pinyin.toString());
			stm.bindString(6, info.packageName);
			stm.execute();
		} catch (Exception e) {
			SLog.e(e);
		}finally{
			closeSavely(database, null);
		}
	}
	
	/**
	 * 
	 * 功能描述：将应用列表保存到DB
	 *
	 * @author 徐萌阳(xumengyang)
	 * @param @param list
	 * @return void
	 * @date 2014年7月22日 上午11:53:08
	 *
	 */
	public void saveList(List<PackageInfo> list){
		SQLiteDatabase database = null;
		SQLiteStatement stm = null;
		try {
			JSONArray array = new JSONArray();
			for(PackageInfo info : list){
				Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(info.packageName);
				//屏蔽系统应用
				if(intent != null && !info.packageName.equals("com.xmy.sou")){
					saveOrUpdate(info);
					JSONObject obj = new JSONObject();
					obj.put("packageName", info.packageName);
					obj.put("versionName", info.versionName);
					obj.put("versionCode", info.versionCode);
					array.put(obj);
				}
				SaveAppEvent e = new SaveAppEvent();
				double rate = (double)list.indexOf(info) / (list.size()-1);
				e.setRate(rate);
				//如果所有的app已经存储完毕，则把Array列表返回给LanchActivity
				if(rate == 1){
					e.setApps(array.toString());
				}
				EventBus.getDefault().post(e);
			}
		} catch (Exception e) {
			SLog.e(e);
		}finally{
			closeSavely(database, stm);
		}
	}
	
	/**
	 * 
	 * 功能描述：根据包名删除数据库中APP信息
	 *
	 * @param @param packageName
	 * @return void
	 *
	 */
	public boolean delelte(String packageName){
		SQLiteDatabase database = null;
		SQLiteStatement stm = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+TABLE_NAME+" WHERE "+PROPERTIES.PACKAGE_NAME+" = ?";
			stm = database.compileStatement(sql);
			stm.bindString(1, packageName);
			int result = stm.executeUpdateDelete();
			return result != 0;
		} catch (Exception e) {
			SLog.e(e);
		}finally{
			closeSavely(database, stm);
		}
		return false;
	}
	
	/**
	 * 
	 * 功能描述：根据关键字在DB中搜索应用
	 *
	 * @author 徐萌阳(xumengyang)
	 * @param @param key
	 * @param @return
	 * @return List<AppInfo>
	 * @date 2014年7月22日 上午11:53:40
	 *
	 */
	public List<AppInfo> search(String key){
		SQLiteDatabase database = null;
		try {
			List<AppInfo> list = new ArrayList<AppInfo>();
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT appname,packagename,versionname,versioncode FROM "+TABLE_NAME+" WHERE key LIKE '%"+key+"%' OR appname LIKE '%"+key+"%' OR sort_key LIKE '%"+key+"%'";
			Cursor c = database.rawQuery(sql, null);
			while(c.moveToNext()){
				AppInfo info = new AppInfo();
				info.setName(c.getString(0));
				info.setPackageName(c.getString(1));
				info.setVersionName(c.getString(2));
				info.setVersionCode(c.getInt(3));
				list.add(info);
			}
			return list;
		} catch (Exception e) {
			SLog.e(e);
		}finally{
			closeSavely(database, null);
		}
		return null;
	}
	
//	/**
//	 * 根据包名更新DB中对应APP的启动时间及启动次数
//	 * @param packageName
//	 * @return
//	 */
//	public boolean updateAppLanchInfo(String packageName){
//		
//	}
	
	
	private void closeSavely(SQLiteDatabase database,SQLiteStatement stm){
		if(database != null){
			database.close();
		}
		if(stm != null){
			stm.close();
		}
	}
}
