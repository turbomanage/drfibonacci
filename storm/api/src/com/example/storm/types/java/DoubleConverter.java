package com.example.storm.types.java;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;

@Converter(forTypes = { double.class, Double.class })
public class DoubleConverter extends TypeConverter<Double,Double> {

	@Override
	public SqlType getSqlType() {
		return SqlType.REAL;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_DOUBLE;
	}

	@Override
	public Double toSql(Double javaValue) {
		return javaValue;
	}

	@Override
	public Double fromSql(Double sqlValue) {
		return sqlValue;
	}

}
