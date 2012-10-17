package com.example.storm.types;

import com.example.storm.api.Converter;

@Converter(forTypes = { short.class, Short.class })
public class ShortConverter extends TypeConverter<Short, Short> {

	public static final ShortConverter GET = new ShortConverter();
	
	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public BindType getBindType() {
		return BindType.SHORT;
	}

	@Override
	public Short toSql(Short javaValue) {
		return javaValue;
	}

	@Override
	public Short fromSql(Short sqlValue) {
		return sqlValue;
	}

	@Override
	public Short fromString(String strValue) {
		return Short.valueOf(strValue);
	}

}
