package com.example.storm.types;

import com.example.storm.api.Converter;


@Converter(forTypes = { boolean.class, Boolean.class })
public class BooleanConverter extends TypeConverter<Boolean,Integer> {

	public static final BooleanConverter GET = new BooleanConverter();
	
	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public BindType getBindType() {
		return BindType.INT;
	}

	@Override
	public Integer toSql(Boolean javaValue) {
		if (javaValue == null)
			return null;
		return (javaValue==Boolean.TRUE) ? 1 : 0;
	}

	@Override
	public Boolean fromSql(Integer sqlValue) {
		if (sqlValue == null)
			return null;
		return sqlValue==0 ? Boolean.FALSE : Boolean.TRUE;
	}

	@Override
	public Integer fromString(String strValue) {
		if (strValue == null)
			return null;
		return Integer.valueOf(strValue);
	}

}
