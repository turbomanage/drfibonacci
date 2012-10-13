package com.example.storm.types;

import com.example.storm.api.Converter;

@Converter(forTypes = { double.class, Double.class })
public class DoubleConverter extends TypeConverter<Double,Double> {

	@Override
	public SqlType getSqlType() {
		return SqlType.REAL;
	}

	@Override
	public BindType getBindType() {
		return BindType.DOUBLE;
	}

	@Override
	public Double toSql(Double javaValue) {
		return javaValue;
	}

	@Override
	public Double fromSql(Double sqlValue) {
		return sqlValue;
	}

	@Override
	public Double fromString(String strValue) {
		// use long bits to preserve exact value
		return Double.longBitsToDouble(Long.valueOf(strValue));
	}

	@Override
	public String toString(Double sqlValue) {
		// use long bits to preserve exact value
		return (sqlValue == null) ? null : String.valueOf(Double.doubleToLongBits(sqlValue));
	}
}
