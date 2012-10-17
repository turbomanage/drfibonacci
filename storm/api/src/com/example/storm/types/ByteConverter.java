package com.example.storm.types;

import com.example.storm.api.Converter;

@Converter(forTypes = { byte.class, Byte.class })
public class ByteConverter extends TypeConverter<Byte,Short> {

	public static final ByteConverter GET = new ByteConverter();
	
	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public BindType getBindType() {
		return BindType.SHORT;
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

	@Override
	public Short fromString(String strValue) {
		if (strValue == null)
			return null;
		return Short.valueOf(strValue);
	}

}
