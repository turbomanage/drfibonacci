package com.example.storm.test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
		// Insert some sample data
		TestEntity te1 = new TestEntity();
		te1.setIntField(13);
		te1.setwStringField("abc");
		long id1 = dao.put(te1);
		// Verify that we started clean
		assertEquals(1, id1);
		// Verify obj id field is updated with the auto id
		assertEquals(id1, te1.getId());
		TestEntity te2 = new TestEntity();
		te2.setIntField(21);
		te2.setwStringField("abc");
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
		assertEquals(13, obj.getIntField());
		assertEquals("abc", obj.getwStringField());
	}

	public void testGetByExample() {
		TestEntity exampleObj = new TestEntity();
		exampleObj.setIntField(21);
		TestEntity resultObj = dao.getByExample(exampleObj);
		assertEquals("abc", resultObj.getwStringField());
	}

	public void testGetByExampleWithTooManyResults() {
		TestEntity exampleObj = new TestEntity();
		exampleObj.setwStringField("abc");
		try {
			TestEntity resultObj = dao.getByExample(exampleObj);
			fail();
		} catch (TooManyResultsException e) {
			// passed
		}
	}

	public void testInsertAndRetrieveAllFields() {
		TestEntity newEntity = new TestEntity();
		long id = dao.put(newEntity);
		assertTrue(id > 0);
		TestEntity retrievedEntity = dao.getById(id);
		assertAllFieldsMatch(newEntity, retrievedEntity);
	}

	public void testInsertAndRetrieveAllFieldsWithValues() {
		TestEntity newEntity = new TestEntity();
		populateEntity(newEntity);
		long id = dao.put(newEntity);
		assertTrue(id > 0);
		TestEntity retrievedEntity = dao.getById(id);
		assertAllFieldsMatch(newEntity, retrievedEntity);
	}
	
	public void testUpdateAndRetrieveAllFields() {
		TestEntity newEntity = new TestEntity();
		long id = dao.put(newEntity);
		populateEntity(newEntity);
		long numRowsUpdated = dao.put(newEntity);
		assertEquals(1, numRowsUpdated);
		TestEntity retrievedEntity = dao.getById(id);
		assertAllFieldsMatch(newEntity, retrievedEntity);
	}

	public void testListAll() {
		List<TestEntity> all = dao.listAll();
		assertEquals(2, all.size());
	}

	public void testListByExample() {
		TestEntity exampleObj = new TestEntity();
		exampleObj.setwStringField("abc");
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

	private void populateEntity(TestEntity e) {
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