package com.example.storm.apt;

import com.example.storm.exception.TypeNotSupportedException;
import com.example.storm.types.java.TypeConverter;


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
	
	private TypeConverter getConverter() {
		return TypeMapper.getConverter(javaType);
	}
	
	/**
	 * Convenience method for more compact Dao templates.
	 * Returns the name of this field's converter class 
	 * sans package name.
	 * 
	 * @return
	 */
	public String getConverterName() {
		return getConverter().getClass().getSimpleName();
	}
	
	/**
	 * Fully-qualified name of the converter class
	 * for this field.
	 * 
	 * @return String classname
	 */
	public String getQualifiedConverterClass() {
		return getConverter().getClass().getName();
	}
	
	public String getCursorMethod() {
		return getConverter().getCursorMethod().getMethodName();
	}

	public String getSqlType() {
		// TODO Hack. Add @Id annotation instead.
		// TODO Make TypeNotSupportedException and swallow it here to prevent 
		// template from failing, which will cause err to report elsewhere.
		if ("id".equals(fieldName))
			return "INTEGER PRIMARY KEY AUTOINCREMENT";
		try {
			return TypeMapper.getSqlType(javaType);
		} catch (TypeNotSupportedException e) {
			// swallow as its already been reported
			return null;
		}
	}
	
	public String getSetter() {
		// TODO handle little-known case of 2nd cap rule
		return "set" + capFirst(fieldName);
	}
	
	public String getGetter() {
		if ("boolean".equals(javaType))
			return "is" + capFirst(fieldName);
		else
			return "get" + capFirst(fieldName);
	}

	/**
	 * Capitalizes the first letter to create a valid getter/setter name.
	 * 
	 * @param String
	 * @return String
	 */
	private String capFirst(String anyName) {
		// obscure Java convention: if second letter capitalized, leave it alone
		if (anyName.length() > 1)
			if (anyName.charAt(1) >='A' && anyName.charAt(1) <= 'Z')
				return anyName;
		String capFirstLetter = anyName.substring(0, 1).toUpperCase();
		return capFirstLetter + anyName.substring(1); 
	}
	
	public boolean isNullable() {
		return javaType.contains(".") || javaType.contains("[]");
	}
	
//	/**
//	 * Returns the default value of a primitive type as a String to be used
//	 * by a codegen template.
//	 * 
//	 * @return String Default value
//	 */
//	public String getDefaultValue() {
//		if ("boolean".equals(javaType)) {
//			return "false";
//		} else if ("float".equals(javaType) || "double".equals(javaType)) {
//			return "0.";
//		} else if (isPrimitive()) {
//			return "0";
//		} else {
//			return "null";
//		}
//	}
//	
}
