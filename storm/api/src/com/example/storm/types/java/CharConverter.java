package com.example.storm.types.java;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;

@Converter(forTypes = { char.class, Character.class })
public class CharConverter extends TypeConverter<Character,Integer> {

	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_INT;
	}

	@Override
	public Integer toSql(Character javaValue) {
		if (javaValue == null)
			return null;
		return (int) javaValue.charValue();
	}

	@Override
	public Character fromSql(Integer sqlValue) {
		if (sqlValue == null)
			return null;
		return (char) sqlValue.intValue();
	}

	@Override
	public Character fromString(String strValue) {
		return strValue.charAt(0);
	}

}
