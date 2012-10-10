package com.example.storm.types.java;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;


@Converter(forTypes = { boolean.class, Boolean.class })
public class BooleanConverter extends TypeConverter<Boolean,Integer> {

	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_INT;
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
	public Boolean fromString(String strValue) {
		return Boolean.valueOf(strValue);
	}

}
