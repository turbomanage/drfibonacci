package com.example.storm.test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;

import com.example.storm.DatabaseFactory;
import com.example.storm.TestActivity;
import com.example.storm.TestEntity;
import com.example.storm.dao.TestEntityDao;
import com.example.storm.exception.TooManyResultsException;

public class DaoTestCase extends AndroidTestCase {
	private Context ctx;
	private TestEntityDao dao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ctx = getContext();
		openDatabase();
		dao = new TestEntityDao(ctx);
		long id = dao.put(new TestEntity());
		// Verify that we started clean
		assertEquals(1, id);
	}
	
	public void testDelete() {
		long id = dao.put(new TestEntity());
		int numRowsDeleted = dao.delete(id);
		assertEquals(1, numRowsDeleted);
		assertNull(dao.get(id));
	}
	
	public void testDeleteAll() {
		persistRandomEntities(5);
		int numRowsDeleted = dao.deleteAll();
		assertTrue(numRowsDeleted > 5);
		List<TestEntity> listAll = dao.listAll();
		assertEquals(0, listAll.size());
	}

	public void testGetByExample() {
		TestEntity newEntity = new TestEntity();
		newEntity.setIntField(21);
		long id = dao.put(newEntity);
		TestEntity exampleObj = new TestEntity();
		exampleObj.setIntField(21);
		TestEntity resultEntity = dao.getByExample(exampleObj);
		assertEquals(id, resultEntity.getId());
	}

	public void testGetByExampleWithTooManyResults() {
		String testName = "testGetByExampleWithTooManyResults";
		persistTwoEntitiesHavingAnIdenticalStringField(testName);
		TestEntity exampleObj = new TestEntity();
		exampleObj.setwStringField(testName);
		try {
			TestEntity resultObj = dao.getByExample(exampleObj);
			fail();
		} catch (TooManyResultsException e) {
			// passed
		}
	}

	public void testInsert() {
		TestEntity newEntity = new TestEntity();
		long id = dao.put(newEntity);
		assertTrue(id > 0);
		assertEquals(id, newEntity.getId());
		TestEntity retrievedEntity = dao.get(id);
		assertAllFieldsMatch(newEntity, retrievedEntity);
	}

	public void testInsertWithNonDefaultValues() {
		TestEntity newEntity = new TestEntity();
		populateTestEntity(newEntity);
		long id = dao.put(newEntity);
		assertTrue(id > 0);
		TestEntity retrievedEntity = dao.get(id);
		assertAllFieldsMatch(newEntity, retrievedEntity);
	}
	
	public void testUpdate() {
		TestEntity newEntity = new TestEntity();
		long id = dao.put(newEntity);
		populateTestEntity(newEntity);
		long numRowsUpdated = dao.put(newEntity);
		assertEquals(1, numRowsUpdated);
		TestEntity retrievedEntity = dao.get(id);
		assertAllFieldsMatch(newEntity, retrievedEntity);
	}

	public void testListAll() {
		List<TestEntity> before = dao.listAll();
		persistRandomEntities(5);
		List<TestEntity> after = dao.listAll();
		assertEquals(5, after.size() - before.size());
	}

	public void testListByExample() {
		String testName = "testListByExample";
		persistTwoEntitiesHavingAnIdenticalStringField(testName);
		TestEntity exampleObj = new TestEntity();
		exampleObj.setwStringField(testName);
		List<TestEntity> resultList = dao.listByExample(exampleObj);
		assertEquals(2, resultList.size());
	}

	private void assertAllFieldsMatch(TestEntity a, TestEntity b) {
		assertEquals(a.getId(), b.getId());
		assertEquals(a.getByteField(), b.getByteField());
		assertTrue(Arrays.equals(a.getBlobField(), b.getBlobField()));
		assertEquals(a.getCharField(), b.getCharField());
		assertEquals(a.getDoubleField(), b.getDoubleField());
		assertEquals(a.getFloatField(), b.getFloatField());
		assertEquals(a.getIntField(), b.getIntField());
		assertEquals(a.getLastMod(), b.getLastMod());
		assertEquals(a.getLastSync(), b.getLastSync());
		assertEquals(a.getLongField(), b.getLongField());
		assertEquals(a.getShortField(), b.getShortField());
		assertEquals(a.getVersion(), b.getVersion());
		assertEquals(a.getwStringField(), b.getwStringField());
		assertEquals(a.getwBooleanField(), b.getwBooleanField());
		assertEquals(a.getwByteField(), b.getwByteField());
		assertEquals(a.getwCharacterField(), b.getwCharacterField());
		assertEquals(a.getwDateField(), b.getwDateField());
		assertEquals(a.getwDoubleField(), b.getwDoubleField());
		assertEquals(a.getwFloatField(), b.getwFloatField());
		assertEquals(a.getwIntegerField(), b.getwIntegerField());
		assertEquals(a.getwLongField(), b.getwLongField());
		assertEquals(a.getwShortField(), b.getwShortField());
	}

	private void openDatabase() {
		SQLiteOpenHelper dbHelper = DatabaseFactory.getDatabaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		assertEquals(TestActivity.DB_VERSION, db.getVersion());
		// wipe database
		dbHelper.onUpgrade(db, TestActivity.DB_VERSION, TestActivity.DB_VERSION);
	}

	private void persistRandomEntities(int n) {
		for (int i = 0; i < n; i++) {
			TestEntity randomEntity = new TestEntity();
			randomEntity.setLongField(new Random().nextLong());
			dao.put(randomEntity);
		}
	}

	private void persistTwoEntitiesHavingAnIdenticalStringField(String strInCommon) {
		TestEntity e1 = new TestEntity();
		e1.setwStringField(strInCommon);
		dao.put(e1);
		TestEntity e2 = new TestEntity();
		e2.setwStringField(strInCommon);
		dao.put(e2);
	}

	private void populateTestEntity(TestEntity e) {
		e.setBlobField("CAFEBABE".getBytes());
		e.setBooleanField(true);
		e.setCharField('z');
		e.setDoubleField((1 + Math.sqrt(5)) / 2);
		e.setFloatField((float) ((1 + Math.sqrt(5)) / 2));
		e.setIntField(75025);
		e.setLastMod(new Date().getTime());
		e.setLastSync(new Date().getTime());
		e.setLongField(12586269025L);
		e.setShortField((short) 28657);
		e.setwBooleanField(Boolean.TRUE);
		e.setwByteField(new Byte((byte) 89));
		e.setwCharacterField('X');
		e.setwDateField(new Date());
		e.setwDoubleField((1 - Math.sqrt(5)) / 2);
		e.setwFloatField((float) ((1 - Math.sqrt(5)) / 2));
		e.setwIntegerField(1836311903);
		e.setwLongField(86267571272L);
		e.setwShortField((short) 17711);
		e.setwStringField("Hello, world!");
	}

}