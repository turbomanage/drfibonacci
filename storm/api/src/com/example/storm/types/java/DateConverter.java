package com.example.storm.types.java;

import java.util.Date;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.java.TypeConverter;
import com.example.storm.types.sql.SqlType;

/**
 * User-supplied TypeConverter.
 * 
 * @author drfibonacci
 */
@Converter(forTypes = { Date.class })
public class DateConverter extends TypeConverter<Date, Long> {

	@Override
	public Long toSql(Date javaValue) {
		if (javaValue == null) 
			return null;
		return javaValue.getTime();
	}

	@Override
	public Date fromSql(Long sqlValue) {
		if (sqlValue == null) {
			return null;
		}
		return new Date(sqlValue);
	}

	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_LONG;
	}

	@Override
	public Date fromString(String strValue) {
		return fromSql(Long.valueOf(strValue));
	}

}
