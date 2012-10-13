package com.example.storm.types;

import com.example.storm.api.Converter;

@Converter(forTypes = { char.class, Character.class })
public class CharConverter extends TypeConverter<Character,Integer> {

	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public BindType getBindType() {
		return BindType.INT;
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
	public Integer fromString(String strValue) {
		if (strValue == null)
			return null;
		return Integer.valueOf(strValue);
	}

}
