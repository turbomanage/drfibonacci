package com.example.storm;

import android.database.sqlite.SQLiteDatabase;

public abstract class TableHelper {

	public abstract String getTableName();
	public abstract String createSql();
	public abstract String upgradeSql(int oldVersion, int newVersion);
	
	public void onCreate(SQLiteDatabase db) {
		String createSql = createSql();
		db.execSQL(createSql);
	}
	
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		String upgradeSql = upgradeSql(oldVersion, newVersion);
		if (upgradeSql != null)
			db.execSQL(upgradeSql);
		onCreate(db);
	}
	
}
