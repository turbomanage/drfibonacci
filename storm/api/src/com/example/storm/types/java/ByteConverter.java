package com.example.storm.types.java;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;

@Converter(forTypes = { byte.class, Byte.class })
public class ByteConverter extends TypeConverter<Byte,Short> {

	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_SHORT;
	}

	@Override
	public Short toSql(Byte javaValue) {
		if (javaValue == null) 
			return null;
		return javaValue.shortValue();
	}

	@Override
	public Byte fromSql(Short sqlValue) {
		if (sqlValue == null)
			return null;
		return sqlValue.byteValue();
	}

}
