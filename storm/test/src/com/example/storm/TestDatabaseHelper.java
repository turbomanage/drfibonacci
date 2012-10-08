package com.example.storm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.storm.api.Database;
import com.example.storm.api.DatabaseFactory;

@Database(name = TestDatabaseHelper.DB_NAME, version = TestDatabaseHelper.DB_VERSION)
public class TestDatabaseHelper extends DatabaseHelper {

	public TestDatabaseHelper(Context ctx, DatabaseFactory dbFactory) {
		super(ctx, dbFactory);
	}
	
	public static final String DB_NAME = "testDb";
	public static final int DB_VERSION = 1;

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// test implementation -- wipe all tables
		TableHelper[] tables = dbFactory.getTableHelpers();
		for (TableHelper table : tables) {
			String dropSql = "DROP TABLE IF EXISTS " + table.getTableName();
			db.execSQL(dropSql);
		}
		super.onUpgrade(db, oldVersion, newVersion);
	}
	
}
