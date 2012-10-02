package com.example.storm;

/**
 * Represents all the available getters on a Cursor.
 * Used only for type safety at code generation time.
 *  
 * @author drfibonacci
 */
public enum CursorMethod {
	
	GET_BLOB("getBlob"),
	GET_DOUBLE("getDouble"),
	GET_FLOAT("getFloat"),
	GET_INT("getInt"),
	GET_LONG("getLong"),
	GET_SHORT("getShort"), 
	GET_STRING("getString");
	
	private String methodName;

	CursorMethod(String methodName) {
		this.methodName = methodName;
	}
	
	public String getMethodName() {
		return this.methodName;
	}
	
}
