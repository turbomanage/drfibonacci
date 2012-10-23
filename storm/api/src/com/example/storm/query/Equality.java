package com.example.storm.query;

/**
 * SQL =
 * 
 * @author drfibonacci
 */
public class Equality extends Predicate {
	
	public Equality(String colName, String param) {
		super(colName, param);
	}

	@Override
	public String getSqlOp() {
		return colName + "=";
	}

}
