package com.example.storm;


/**
 * Model of a persisted field
 * 
 * @author drfibonacci
 */
public class FieldModel {
	
	private String fieldName, colName, javaType;
	
	public FieldModel(String fieldName, String javaType) {
		this.fieldName = fieldName;
		this.javaType = javaType;
		// TODO Use @Id or @ColumnName annotation instead
		if ("id".equals(fieldName)) {
			this.colName = "_id";
		} else {
			this.colName = fieldName;
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getColName() {
		return colName;
	}

	public String getJavaType() {
		return javaType;
	}

	public String getSqlType() {
		// TODO Hack. Add @Id annotation instead.
		if ("id".equals(fieldName))
			return "INTEGER PRIMARY KEY AUTOINCREMENT";
		return TypeMapper.getSqlType(javaType);
	}
	
	public String getSetter() {
		return "set" + capFirst(fieldName);
	}
	
	public String getGetter() {
		return "get" + capFirst(fieldName);
	}

	/**
	 * Capitalizes the first letter.
	 * 
	 * @param String
	 * @return String
	 */
	private String capFirst(String anyName) {
		String capFirstLetter = anyName.substring(0, 1).toUpperCase();
		return capFirstLetter + anyName.substring(1); 
	}
	
	public boolean isPrimitive() {
		return !javaType.contains(".");
	}
	
	/**
	 * For primitive types, returns the wrapper type like java.lang.Integer.
	 * Used by codegen templates.
	 * 
	 * @return Fully-qualified wrapper type
	 */
	public String getWrapperType() {
		if ("int".equals(javaType)) {
			return "java.lang.Integer";
		} else if ("char".equals(javaType)) {
			return "java.lang.Character";
		} else if (isPrimitive()) {
			return "java.lang." + capFirst(javaType);
		}
		throw new IllegalArgumentException(javaType + " has no wrapper type.");
	}
	
	/**
	 * Returns the default value of a primitive type as a String to be used
	 * by a codegen template.
	 * 
	 * @return String Default value
	 */
	public String getDefaultValue() {
		if ("boolean".equals(javaType)) {
			return "false";
		} else if ("float".equals(javaType) || "double".equals(javaType)) {
			return "0.";
		} else if (isPrimitive()) {
			return "0";
		} else {
			return "null";
		}
	}
	
}
