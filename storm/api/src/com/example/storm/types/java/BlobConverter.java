package com.example.storm.types.java;

import android.util.Base64;

import com.example.storm.CursorMethod;
import com.example.storm.api.Converter;
import com.example.storm.types.sql.SqlType;

@Converter(forTypes = { byte[].class })
public class BlobConverter extends TypeConverter<byte[], byte[]> {

	@Override
	public SqlType getSqlType() {
		return SqlType.BLOB;
	}

	@Override
	public CursorMethod getCursorMethod() {
		return CursorMethod.GET_BLOB;
	}

	@Override
	public byte[] toSql(byte[] javaValue) {
		return javaValue;
	}

	@Override
	public byte[] fromSql(byte[] sqlValue) {
		return sqlValue;
	}

	@Override
	public byte[] fromString(String strValue) {
		return Base64.decode(strValue, Base64.DEFAULT);
	}

}
