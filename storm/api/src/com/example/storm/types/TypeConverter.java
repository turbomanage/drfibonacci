package com.example.storm.types;

import android.content.ContentValues;
import android.database.Cursor;



public abstract class TypeConverter<J extends Object, S extends Object> {
	
	public enum SqlType {INTEGER, REAL, BLOB, TEXT};
	
	/**
	 * Enum representing the types for which there are corresponding methods
	 * {@link Cursor} and bind methods. Never called at runtime.
	 * 
	 * @author drfibonacci
	 */
	public enum BindType {BLOB, DOUBLE, FLOAT, INT, LONG, SHORT, STRING}
	
	/**
	 * Convert a Java value to a representation that can
	 * be put into a {@link ContentValues} map.
	 *  
	 * @param javaValue
	 * @return
	 */
	public abstract S toSql(J javaValue);
	
	/**
	 * Convert a value obtained from the {@link Cursor} getS method
	 * to its Java type.
	 * 
	 * @param sqlValue
	 * @return
	 */
	public abstract J fromSql(S sqlValue);

	/**
	 * Convert a value from a String to its {@link SqlType}. 
	 * This method is used by the CSV importer.
	 * 
	 * @param strValue
	 * @return 
	 */
	public abstract S fromString(String strValue);

	/**
	 * Convert a value from its SQL type to a String. This
	 * method is used by the CSV exporter.
	 * 
	 * @param sqlValue
	 * @return
	 */
	public String toString(S sqlValue) {
		return (sqlValue == null) ? null : sqlValue.toString();
	}
	
	/**
	 * SQLite column type that represents this Java type. 
	 * 
	 * @return SqlType
	 */
	public abstract SqlType getSqlType();

	/**
	 * Used to generate the name of the appropriate cursor method.
	 * Used by code generation templates only.
	 * 
	 * @return BindType
	 */
	public abstract BindType getBindType();
	
}
