package com.example.storm;

import android.content.Context;

import com.example.storm.api.Database;
import com.example.storm.api.DatabaseFactory;

@Database(name = TestDatabaseHelper.DB_NAME, version = TestDatabaseHelper.DB_VERSION)
public class TestDatabaseHelper extends DatabaseHelper {

	public TestDatabaseHelper(Context ctx, DatabaseFactory dbFactory) {
		super(ctx, dbFactory);
	}
	
	public static final String DB_NAME = "testDb";
	public static final int DB_VERSION = 1;

}
