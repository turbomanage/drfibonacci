package com.example.storm;

import java.util.HashMap;
import java.util.Map;

public class TypeMapper {

	private static Map<String, SqlType> map = new HashMap<String, SqlType>();

	static {
		map.put("int", SqlType.INTEGER);
		map.put("long", SqlType.INTEGER);
		map.put("java.lang.Integer", SqlType.INTEGER);
		map.put("java.lang.Long", SqlType.INTEGER);
		map.put("java.lang.String", SqlType.TEXT);
	}

	static String getSqlType(String javaType) {
		
		SqlType sqlType = map.get(javaType);
		if (sqlType != null) {
			return sqlType.name();
		}
		throw new IllegalArgumentException("Fields of type " + javaType + " are not supported.");
	}
}
