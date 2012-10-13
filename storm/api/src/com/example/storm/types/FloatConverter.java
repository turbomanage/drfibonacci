package com.example.storm.types;

import com.example.storm.api.Converter;


@Converter(forTypes = { float.class, Float.class })
public class FloatConverter extends TypeConverter<Float,Float> {

	@Override
	public SqlType getSqlType() {
		return SqlType.REAL;
	}

	@Override
	public BindType getBindType() {
		return BindType.FLOAT;
	}

	@Override
	public Float toSql(Float javaValue) {
		return javaValue;
	}

	@Override
	public Float fromSql(Float sqlValue) {
		return sqlValue;
	}

	@Override
	public Float fromString(String strValue) {
		return Float.valueOf(strValue);
	}

}
