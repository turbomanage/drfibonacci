package com.example.storm.types.java;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;

@Converter(forTypes = { long.class, Long.class })
public class LongConverter extends TypeConverter<Long,Long> {

	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_LONG;
	}

	@Override
	public Long toSql(Long javaValue) {
		return javaValue;
	}

	@Override
	public Long fromSql(Long sqlValue) {
		return sqlValue;
	}

}
