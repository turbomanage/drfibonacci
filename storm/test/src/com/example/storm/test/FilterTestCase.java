package com.example.storm.test;

import android.content.Context;
import android.test.AndroidTestCase;

import com.example.storm.entity.SimpleEntity;
import com.example.storm.entity.dao.SimpleEntityDao;
import com.example.storm.entity.dao.SimpleEntityTable.Columns;
import com.example.storm.query.FilterBuilder;

public class FilterTestCase extends AndroidTestCase {
	private static final byte BYTE_VALUE = (byte) 13;
	private static final byte[] BLOB_VALUE = "blobField".getBytes();
	private static final boolean BOOLEAN_VALUE = true;
	private static final char CHAR_VALUE = 'q';
	private static final double DOUBLE_VALUE = Math.PI;
	private static final float FLOAT_VALUE = (float) Math.PI;
	private static final int INT_VALUE = 121393;
	private static final long LONG_VALUE = 2971215073L;
	private static final int SHORT_VALUE = 21;
	private Context ctx;
	private SimpleEntityDao dao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ctx = getContext();
		dao = new SimpleEntityDao(ctx);
		insertTestEntity();
	}

	@Override
	protected void tearDown() throws Exception {
		dao.deleteAll();
		super.tearDown();
	}

	private long insertTestEntity() {
		SimpleEntity e = new SimpleEntity();
		e.setBlobField(BLOB_VALUE);
		e.setBooleanField(true);
		e.setByteField(BYTE_VALUE);
		e.setCharField(CHAR_VALUE);
		e.setDoubleField(DOUBLE_VALUE);
		e.setFloatField((float) FLOAT_VALUE);
		e.setIntField(INT_VALUE);
		e.setLongField(LONG_VALUE);
		e.setShortField((short) SHORT_VALUE);
		return dao.insert(e);
	}

	public void testQueryByBlob() {
		try {
			dao.filter().eq(Columns.BLOBFIELD, BLOB_VALUE);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}

	public void testQueryByBooleanValue() {
		FilterBuilder filter = dao.filter().eq(Columns.BOOLEANFIELD, BOOLEAN_VALUE);
		SimpleEntity result = dao.asObject(filter.exec());
		assertEquals(BOOLEAN_VALUE, result.isBooleanField());
	}

	public void testQueryByByte() {
		FilterBuilder filter = dao.filter().eq(Columns.BYTEFIELD, BYTE_VALUE);
		SimpleEntity result = dao.asObject(filter.exec());
		assertEquals(BYTE_VALUE, result.getByteField());
	}
	
	public void testQueryByChar() {
		FilterBuilder filter = dao.filter().eq(Columns.CHARFIELD, CHAR_VALUE);
		SimpleEntity result = dao.asObject(filter.exec());
		assertEquals(CHAR_VALUE, result.getCharField());
	}

	public void testQueryByDouble() {
		try {
			dao.filter().eq(Columns.DOUBLEFIELD, Math.PI);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}

	public void testQueryByFloat() {
		try {
			dao.filter().eq(Columns.FLOATFIELD, FLOAT_VALUE);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}

	public void testQueryByInt() {
		FilterBuilder filter = dao.filter().eq(Columns.INTFIELD, INT_VALUE);
		SimpleEntity result = dao.asObject(filter.exec());
		assertEquals(INT_VALUE, result.getIntField());
	}

	public void testQueryByLong() {
		FilterBuilder filter = dao.filter().eq(Columns.LONGFIELD, LONG_VALUE);
		SimpleEntity result = dao.asObject(filter.exec());
		assertEquals(LONG_VALUE, result.getLongField());
	}

	public void testQueryByShort() {
		FilterBuilder filter = dao.filter().eq(Columns.SHORTFIELD, (short) SHORT_VALUE);
		SimpleEntity result = dao.asObject(filter.exec());
		assertEquals(SHORT_VALUE, result.getShortField());
	}

}