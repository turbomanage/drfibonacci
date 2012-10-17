package com.example.storm.types;

import java.util.Date;

import com.example.storm.api.Converter;

/**
 * User-supplied TypeConverter.
 * 
 * @author drfibonacci
 */
@Converter(forTypes = { Date.class })
public class DateConverter extends TypeConverter<Date, Long> {

	public static final DateConverter GET = new DateConverter();
	
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
	public BindType getBindType() {
		return BindType.LONG;
	}

	@Override
	public Long fromString(String strValue) {
		return Long.valueOf(strValue);
	}

}
