package com.example.storm.test;

import junit.framework.TestCase;

import com.example.storm.CsvUtils;

public class CsvUtilsTest extends TestCase {

	private static final char QUOTE = '"';

	public void testEscapePlainValue() {
		String val = "this is as a regular string with _!@#$%^&*() and 0123456789";
		assertEquals(val, CsvUtils.escapeCsv(val));
	}

	public void testEscapeValueContainingComma() {
		String val = "ab,cd";
		assertEquals(QUOTE + val + QUOTE, CsvUtils.escapeCsv(val));
	}
	
	public void testEscapeValueContainingQuote() {
		String val = "abcd" + QUOTE + "efgh";
		assertEquals(QUOTE + "abcd" + QUOTE + QUOTE + "efgh" + QUOTE, CsvUtils.escapeCsv(val));
	}
	
	public void testEscapeValueContainingCR() {
		String val = "abcd\r";
		assertEquals(QUOTE + val + QUOTE, CsvUtils.escapeCsv(val));
	}
	
	public void testEscapeValueContainingLF() {
		String val = "abcd\n";
		assertEquals(QUOTE + val + QUOTE, CsvUtils.escapeCsv(val));
	}
	
	public void testUnescapeValue() {
		String val = QUOTE + "this is a test" + QUOTE;
		assertEquals("this is a test", CsvUtils.unescapeCsv(val));
	}
	
	public void testUnescapeValueContainingQuotes() {
		String orig = "a " + QUOTE + "nice" + QUOTE + " day";
		assertEquals(orig, CsvUtils.unescapeCsv(CsvUtils.escapeCsv(orig)));  
	}
	
}
