package com.example.storm.types;

import com.example.storm.api.Converter;

@Converter(forTypes = { double.class, Double.class })
public class DoubleConverter extends TypeConverter<Double,Double> {

	public static final DoubleConverter GET = new DoubleConverter();
	
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
		// use long bits as hex to preserve exact value
		return Double.longBitsToDouble(Long.parseLong(strValue, 16));
	}

	@Override
	public String toString(Double sqlValue) {
		// use long bits as hex to preserve exact value
		// Don't use Long.toHexString! Long.parseLong doesn't understand 2's complement
		return (sqlValue == null) ? null : Long.toString(Double.doubleToLongBits(sqlValue), 16);
	}
}
