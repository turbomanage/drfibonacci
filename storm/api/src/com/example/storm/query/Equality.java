package com.example.storm.query;

import com.example.storm.TableHelper.Column;

/**
 * SQL =
 * 
 * @author drfibonacci
 */
public class Equality extends Predicate {
	
	public Equality(Column colName, String param) {
		super(colName, param);
	}

	@Override
	public String getSqlOp() {
		return colName + "=";
	}

}
