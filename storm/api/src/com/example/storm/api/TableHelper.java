package com.example.storm.api;

import android.database.sqlite.SQLiteDatabase;

public interface TableHelper {

	String getTableName();
	void onCreate(SQLiteDatabase db);
	void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion);
	
}
