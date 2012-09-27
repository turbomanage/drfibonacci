package com.example.storm.test;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;

import com.example.storm.DatabaseFactory;
import com.example.storm.TestActivity;
import com.example.storm.TestEntity;
import com.example.storm.TooManyResultsException;
import com.example.storm.dao.TestEntityDao;

public class DaoTestCase extends AndroidTestCase {
	private Context ctx;
	private TestEntityDao dao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ctx = getContext();
		openDatabase();

		dao = new TestEntityDao(ctx);
		// Insert some sample data
		TestEntity te1 = new TestEntity(13, "abc");
		long id1 = dao.put(te1);
		// Verify that we started clean
		assertEquals(1, id1);
		// Verify obj id field is updated with the auto id
		assertEquals(id1, te1.getId().longValue());
		TestEntity te2 = new TestEntity(21, "abc");
		long id2 = dao.put(te2);
		assertEquals(2, id2);
	}
	
	private void openDatabase() {
		SQLiteOpenHelper dbHelper = DatabaseFactory.getDatabaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		assertEquals(TestActivity.DB_VERSION, db.getVersion());
		// wipe database
		dbHelper.onUpgrade(db, TestActivity.DB_VERSION, TestActivity.DB_VERSION);
	}

	public void testGetById() {
		TestEntity obj = dao.getById(1l);
		assertEquals(13, obj.getIntField().intValue());
		assertEquals("abc", obj.getStringField());
	}
	
	public void testGetByExample() {
		TestEntity exampleObj = new TestEntity();
		exampleObj.setIntField(21);
		TestEntity resultObj = dao.getByExample(exampleObj);
		assertEquals("abc", resultObj.getStringField());
	}
	
	public void testGetByExampleWithTooManyResults() {
		TestEntity exampleObj = new TestEntity();
		exampleObj.setStringField("abc");
		try {
			TestEntity resultObj = dao.getByExample(exampleObj);
			fail();
		} catch (TooManyResultsException e) {
			// passed
		}
	}
	
	public void testListAll() {
		List<TestEntity> all = dao.listAll();
		assertEquals(2, all.size());
	}

	public void testListByExample() {
		TestEntity exampleObj = new TestEntity();
		exampleObj.setStringField("abc");
		List<TestEntity> resultList = dao.listByExample(exampleObj);
		assertEquals(2, resultList.size());
	}

}