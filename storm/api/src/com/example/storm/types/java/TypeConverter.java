package com.example.storm.types.java;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.storm.CursorMethod;
import com.example.storm.types.sql.SqlType;


public abstract class TypeConverter<J extends Object, S extends Object> {
	
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
	 * SQLite column type that represents this Java type. 
	 * 
	 * @return SqlType
	 */
	public abstract SqlType getSqlType();

	/**
	 * The name of the cursor method used to retrieve this type from the db.
	 * Used by code generation templates only.
	 * 
	 * @return CursorMethod Name of the cursor getX method
	 */
	public abstract CursorMethod getCursorMethod();
	
}
