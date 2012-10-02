package com.example.storm.types.java;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;


@Converter(forTypes = { float.class, Float.class })
public class FloatConverter extends TypeConverter<Float,Float> {

	@Override
	public SqlType getSqlType() {
		return SqlType.REAL;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_FLOAT;
	}

	@Override
	public Float toSql(Float javaValue) {
		return javaValue;
	}

	@Override
	public Float fromSql(Float sqlValue) {
		return sqlValue;
	}

}
