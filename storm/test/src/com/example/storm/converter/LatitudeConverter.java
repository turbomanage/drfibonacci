package com.example.storm.converter;

import com.example.storm.api.Converter;
import com.example.storm.types.DoubleConverter;
import com.example.storm.types.TypeConverter;

@Converter(forTypes = { Latitude.class })
public class LatitudeConverter extends TypeConverter<Latitude, Double> {

	@Override
	public Double toSql(Latitude javaValue) {
		return javaValue.getDegLat();
	}

	@Override
	public Latitude fromSql(Double sqlValue) {
		return new Latitude(sqlValue);
	}

	@Override
	public Double fromString(String strValue) {
		return DoubleConverter.GET.fromString(strValue);
	}

	@Override
	public String toString(Double sqlValue) {
		return DoubleConverter.GET.toString(sqlValue);
	}

	@Override
	public com.example.storm.types.TypeConverter.SqlType getSqlType() {
		return SqlType.REAL;
	}

	@Override
	public com.example.storm.types.TypeConverter.BindType getBindType() {
		return BindType.DOUBLE;
	}

}
