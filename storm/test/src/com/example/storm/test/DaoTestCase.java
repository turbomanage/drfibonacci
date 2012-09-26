package com.example.storm.test;

import java.util.List;

import android.content.Context;
import android.test.AndroidTestCase;

import com.example.storm.TestEntity;
import com.example.storm.dao.TestEntityDao;

public class DaoTestCase extends AndroidTestCase {
	private Context ctx;
	private TestEntityDao testEntityDao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ctx = getContext();
		testEntityDao = new TestEntityDao(ctx);
	}
	
	public void testPut() {
		TestEntity te1 = new TestEntity(13, "thirteen");
		long id1 = testEntityDao.put(te1);
		assertEquals(1, id1);
		assertEquals(id1, te1.getId());
		TestEntity te2 = new TestEntity(21, "twenty-one");
		long id2 = testEntityDao.put(te2);
		assertEquals(2, id2);
		assertEquals(id2, te2.getId());
		List<TestEntity> all = testEntityDao.listAll();
		assertEquals(2, all.size());
	}
	
}
