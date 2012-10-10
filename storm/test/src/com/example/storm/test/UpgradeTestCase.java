package com.example.storm.test;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Base64;

import com.example.storm.CsvUtils;
import com.example.storm.DatabaseHelper;
import com.example.storm.TestDatabaseHelper;
import com.example.storm.TestDbFactory;
import com.example.storm.entity.SimpleEntity;
import com.example.storm.entity.dao.SimpleEntityDao;

public class UpgradeTestCase extends AndroidTestCase {

	private Context ctx;
	private SimpleEntityDao dao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ctx = getContext();
		openDatabase();
		dao = new SimpleEntityDao(ctx);
	}

	private void openDatabase() {
		DatabaseHelper dbHelper = TestDbFactory.getDatabaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		assertEquals(TestDatabaseHelper.DB_VERSION, db.getVersion());
		// wipe database
		dbHelper.onUpgrade(db, TestDatabaseHelper.DB_VERSION, TestDatabaseHelper.DB_VERSION);
	}

	private void persistRandomEntities(int n) {
		for (int i = 0; i < n; i++) {
			SimpleEntity randomEntity = new SimpleEntity();
			randomEntity.setId(i);
			randomEntity.setLongField(new Random().nextLong());
			long id = dao.insert(randomEntity);
			assertEquals(i, id);
		}
	}

//	public void testDumpToCsv() throws IOException {
//		persistRandomEntities(11);
//		SimpleEntity e = new SimpleEntity();
//		populateTestEntity(e);
//		dao.put(e);
//		FileOutputStream fos = ctx.openFileOutput("testDb.SimpleEntity.v1", 0);
//		PrintWriter printWriter = new PrintWriter(fos);
//		Cursor c = dao.queryAll();
//		// Write column names in first row
//		StringBuilder sb = new StringBuilder();
//		String[] cols = c.getColumnNames();
//		for (String colName : cols) {
//			sb.append(',');
//			sb.append(colName);
//		}
//		String headerRow = sb.toString().substring(1);
//		printWriter.println(headerRow);
//		for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c
//				.moveToNext()) {
//			String csv = getValues(c);
//			printWriter.println(csv);
//		}
//		printWriter.flush();
//		printWriter.close();
//	}
//
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	// TODO Cursor.getType requires API 11
//	private String getValues(Cursor c) {
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < c.getColumnCount(); i++) {
//			String value;
//			if (c.getType(i) == Cursor.FIELD_TYPE_BLOB) {
//				value = Base64.encodeToString(c.getBlob(i), Base64.NO_PADDING + Base64.NO_WRAP);
//			} else {
//				value = CsvUtils.escapeCsv(c.getString(i));
//			}
//			if (i > 0)
//				sb.append(',');
//			sb.append(value);
//		}
//		return sb.toString();
//	}
//	
//	private void populateTestEntity(SimpleEntity e) {
//		e.setBlobField("CAFEBABE".getBytes());
//		e.setBooleanField(true);
//		e.setCharField('z');
//		e.setDoubleField((1 + Math.sqrt(5)) / 2);
//		e.setFloatField((float) ((1 + Math.sqrt(5)) / 2));
//		e.setIntField(75025);
//		e.setLongField(12586269025L);
//		e.setShortField((short) 28657);
//		e.setwBooleanField(Boolean.TRUE);
//		e.setwByteField(new Byte((byte) 89));
//		e.setwCharacterField('X');
//		e.setwDateField(new Date());
//		e.setwDoubleField((1 - Math.sqrt(5)) / 2);
//		e.setwFloatField((float) ((1 - Math.sqrt(5)) / 2));
//		e.setwIntegerField(1836311903);
//		e.setwLongField(86267571272L);
//		e.setwShortField((short) 17711);
//		e.setwStringField("Hello, world!");
//	}

}