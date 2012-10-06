package com.example.storm.api;

import android.database.sqlite.SQLiteDatabase;

public interface DatabaseFactory {
	
	String getName();
	int getVersion();
	void onCreate(SQLiteDatabase db);
	void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	
}
