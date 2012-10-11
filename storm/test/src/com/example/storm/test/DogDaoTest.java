package com.example.storm.test;

import java.util.Date;

import com.example.storm.DatabaseHelper;
import com.example.storm.TestDatabaseHelper;
import com.example.storm.TestDbFactory;
import com.example.storm.entity.Dog;
import com.example.storm.entity.dao.DogDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class DogDaoTest extends AndroidTestCase {

	private DogDao dao;
	private Context ctx;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ctx = getContext();
		openDatabase();
		dao = new DogDao(getContext());
	}
	
	public void testInsertAndRetrieve() {
		Dog fido = new Dog("Fido", new Date());
		long id = dao.insert(fido);
		Dog retrievedFido = dao.get(id);
		assertAllFieldsMatch(fido, retrievedFido);
	}

	private void assertAllFieldsMatch(Dog expected, Dog actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getBirthday(), actual.getBirthday());
	}
	
	private void openDatabase() {
		DatabaseHelper dbHelper = TestDbFactory.getDatabaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		assertEquals(TestDatabaseHelper.DB_VERSION, db.getVersion());
		// wipe database
		// TODO why is this explicit call required for unit tests?
		dbHelper.onUpgrade(db, 1, 2);
	}

}
