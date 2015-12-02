package com.vs.vslauncher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dataBase extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "User_App_Info.db";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "info_table";
	public final static String CARD_ID = "IconId";
	public final static String CARD_PARK_INFO = "packageName";
	public final static String CARD_ETC_INFO = "activityname";
	public final static String CARD_ACCOUNT_INFO = "name";
	public final static String CARD_MSG = "MsgInfo";

	public dataBase(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 创建table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + CARD_PARK_INFO
				+ " text, " + CARD_ETC_INFO + " text, " + CARD_ACCOUNT_INFO
				+ " text);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	// 增加操作
	public long insert(int source_id, String activityname,
			String packagename, String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		/* ContentValues */
		ContentValues cv = new ContentValues();
		//cv.put(CARD_ID, source_id);
		cv.put(CARD_ETC_INFO, activityname);
		cv.put(CARD_PARK_INFO, packagename);
		cv.put(CARD_ACCOUNT_INFO, name);

		long row = db.insert(TABLE_NAME, null, cv);
		return row;
	}

	// 删除操作
	public void delete(String packageName) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "packageName = ?";
		String[] whereValue = { packageName };
		db.delete(TABLE_NAME, where, whereValue);
	}

	// 修改操作
	public void update(int source_id, String activityname,
			String packagename, String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = CARD_PARK_INFO + " = ?";
		String[] whereValue = { packagename };

		ContentValues cv = new ContentValues();
		//cv.put(CARD_ID, source_id);
		cv.put(CARD_ETC_INFO, activityname);
		cv.put(CARD_PARK_INFO, packagename);
		cv.put(CARD_ACCOUNT_INFO, name);

		db.update(TABLE_NAME, cv, where, whereValue);
	}
}