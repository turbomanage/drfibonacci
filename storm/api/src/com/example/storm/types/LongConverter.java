package com.example.storm.types;

import com.example.storm.api.Converter;

@Converter(forTypes = { long.class, Long.class })
public class LongConverter extends TypeConverter<Long,Long> {

	public static final LongConverter GET = new LongConverter();
	
	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public BindType getBindType() {
		return BindType.LONG;
	}

	@Override
	public Long toSql(Long javaValue) {
		return javaValue;
	}

	@Override
	public Long fromSql(Long sqlValue) {
		return sqlValue;
	}

	@Override
	public Long fromString(String strValue) {
		return Long.valueOf(strValue);
	}

}
