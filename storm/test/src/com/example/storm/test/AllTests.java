package com.example.storm.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(DatabaseTestCase.class); // must run first
		suite.addTestSuite(DaoTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
