package com.example.storm.query;

/**
 * A condition in a SQL WHERE clause
 * 
 * @author drfibonacci
 */
public abstract class Predicate {

	protected String colName;
	protected String param;

	public Predicate(String colName, String param) {
		this.colName = colName;
		this.param = (String) param;
	}
	
	/**
	 * Left side of SQL condition. May wrap colName with a function.
	 * Example:
	 * "hex(blobCol) ="
	 * 
	 * @return String SQL
	 */
	public abstract String getSqlOp();
	
	/**
	 * @return String parameter
	 */
	String getParam() {
		return param;
	}
	
}