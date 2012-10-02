package com.example.storm.types.java;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;

@Converter(forTypes = { String.class })
public class StringConverter extends TypeConverter<String,String> {

	@Override
	public SqlType getSqlType() {
		return SqlType.TEXT;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_STRING;
	}

	@Override
	public String toSql(String javaValue) {
		return javaValue;
	}

	@Override
	public String fromSql(String sqlValue) {
		return sqlValue;
	}

}
