package com.example.storm.types;

import com.example.storm.api.Converter;

@Converter(forTypes = { String.class })
public class StringConverter extends TypeConverter<String,String> {

	@Override
	public SqlType getSqlType() {
		return SqlType.TEXT;
	}

	@Override
	public BindType getBindType() {
		return BindType.STRING;
	}

	@Override
	public String toSql(String javaValue) {
		return javaValue;
	}

	@Override
	public String fromSql(String sqlValue) {
		return sqlValue;
	}

	@Override
	public String fromString(String strValue) {
		return strValue;
	}

}
