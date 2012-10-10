package com.example.storm.types.java;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;

@Converter(forTypes = { int.class, java.lang.Integer.class })
public class IntegerConverter extends TypeConverter<Integer, Integer> {

	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_INT;
	}

	@Override
	public Integer toSql(Integer javaValue) {
		return javaValue;
	}

	@Override
	public Integer fromSql(Integer sqlValue) {
		return sqlValue;
	}

	@Override
	public Integer fromString(String strValue) {
		return Integer.valueOf(strValue);
	}

}
