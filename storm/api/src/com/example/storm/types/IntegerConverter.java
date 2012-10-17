package com.example.storm.types;

import com.example.storm.api.Converter;

@Converter(forTypes = { int.class, java.lang.Integer.class })
public class IntegerConverter extends TypeConverter<Integer, Integer> {

	public static final IntegerConverter GET = new IntegerConverter();
	
	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public BindType getBindType() {
		return BindType.INT;
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
