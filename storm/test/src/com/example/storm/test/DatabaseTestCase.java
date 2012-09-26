package com.example.storm.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;

import com.example.storm.DatabaseFactory;
import com.example.storm.TestActivity;
import com.example.storm.dao.DatabaseHelper;

public class DatabaseTestCase extends AndroidTestCase {
	private Context ctx;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ctx = getContext();
	}
	
	public void testOpenDatabase() {
		SQLiteOpenHelper dbHelper = DatabaseFactory.getDatabaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		assertEquals(TestActivity.DB_VERSION, db.getVersion());
		// wipe database
		dbHelper.onUpgrade(db, TestActivity.DB_VERSION, TestActivity.DB_VERSION);
	}
}
