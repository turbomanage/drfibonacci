//package com.example.storm;
//
//import com.example.storm.api.Converter;
//import com.example.storm.types.java.TypeConverter;
//import com.example.storm.types.sql.SqlType;
//
//@Converter(forTypes = { Integer.class, int.class })
//public class TestConverter extends TypeConverter<Integer, Integer> {
//
//	@Override
//	public SqlType getSqlType() {
//		return SqlType.INTEGER;
//	}
//
//	@Override
//	public CursorMethod getCursorMethod() {
//		return CursorMethod.GET_INT;
//	}
//
//}
