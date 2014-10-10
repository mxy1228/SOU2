package com.xmy.sou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xmy.sou.Config;

public class DBHelper extends SQLiteOpenHelper{

	public DBHelper(Context context){
		super(context, Config.DB_ABOUT.DB_NAME, null, Config.DB_ABOUT.DB_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+AppDao.TABLE_NAME+" (id integer PRIMARY KEY AUTOINCREMENT," +
				"appname char," +
				"packagename char," +
				"versionname char," +
				"versioncode integer," +
				"sort_key char," +
				"key char,"+
				"last_open_time long," +
				"open_count integer )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
}
