package com.example.storm;

import java.util.HashMap;
import java.util.Map;

public class TypeMapper {

		private static Map<String, String> map = new HashMap<String,String>();
		
		static {
			// TODO Auto-generated constructor stub
			map.put("int", SqlType.INTEGER);
			map.put("long", SqlType.INTEGER);
			map.put("java.lang.Integer", SqlType.INTEGER);
			map.put("java.lang.Long", SqlType.INTEGER);
			map.put("java.lang.String", SqlType.TEXT);
		}
		
		static String getSqlType(String javaType) {
			return map.get(javaType);
		}
}
