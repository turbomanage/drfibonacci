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
		newEntity.setBlobField(new byte[]{0x0C,0x15,0x22});
		newEntity.setBlobField("CAFEBABE".getBytes());
		newEntity.setBooleanField(true);
		newEntity.setCharField('z');
		newEntity.setDoubleField((1+Math.sqrt(5))/2);
		newEntity.setFloatField((float) ((1+Math.sqrt(5))/2));
		newEntity.setIntField(75025);
		newEntity.setLastMod(new Date().getTime());
		newEntity.setLastSync(new Date().getTime());
		newEntity.setLongField(12586269025L);
		newEntity.setShortField((short) 28657);
		newEntity.setwBooleanField(Boolean.TRUE);
		newEntity.setwByteField(new Byte((byte) 89));
		newEntity.setwCharacterField('X');
		newEntity.setwDoubleField((1-Math.sqrt(5))/2);
		newEntity.setwFloatField((float) ((1-Math.sqrt(5))/2));
		newEntity.setwIntegerField(1836311903);
		newEntity.setwLongField(86267571272L);
		newEntity.setwShortField((short) 17711);
		newEntity.setwStringField("Hello, world!");
		long id = dao.put(newEntity);
		assertTrue(id > 0);
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
			assertEquals(a.getwDoubleField(), b.getwDoubleField());
			assertEquals(a.getwFloatField(), b.getwFloatField());
			assertEquals(a.getwIntegerField(), b.getwIntegerField());
			assertEquals(a.getwLongField(), b.getwLongField());
			assertEquals(a.getwShortField(), b.getwShortField());
		}

}