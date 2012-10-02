package com.example.storm.types.java;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;

@Converter(forTypes = { short.class, Short.class })
public class ShortConverter extends TypeConverter<Short, Short> {

	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_SHORT;
	}

	@Override
	public Short toSql(Short javaValue) {
		return javaValue;
	}

	@Override
	public Short fromSql(Short sqlValue) {
		return sqlValue;
	}

}
