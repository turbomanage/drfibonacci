package com.example.storm.apt.test;

import com.example.storm.apt.TypeMapper;
import com.example.storm.types.java.BooleanConverter;
import com.example.storm.types.java.IntegerConverter;

import junit.framework.TestCase;

public class TypeMapperTest extends TestCase {
	
	public void testBooleanConversions() {
		BooleanConverter conv = new BooleanConverter();
		assertEquals(0, conv.toSql(false).intValue());
		assertEquals(1, conv.toSql(true).intValue());
		assertFalse(conv.fromSql(0));
		assertTrue(conv.fromSql(1));
	}
	
	public void testSupportedTypes() {
		assertTrue(TypeMapper.getConverter("int") instanceof IntegerConverter);
	}
}
