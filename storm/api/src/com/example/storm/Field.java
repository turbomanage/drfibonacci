package com.example.storm;

/**
 * Model of a persisted field
 * 
 * @author drfibonacci
 */
public class Field {
	private String fieldName, colName, javaType;
	
	public Field(String fieldName, String javaType) {
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
}
