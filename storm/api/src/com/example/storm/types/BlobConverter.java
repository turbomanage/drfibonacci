package com.example.storm.types;

import android.util.Base64;

import com.example.storm.api.Converter;

@Converter(forTypes = { byte[].class })
public class BlobConverter extends TypeConverter<byte[], byte[]> {

	@Override
	public SqlType getSqlType() {
		return SqlType.BLOB;
	}

	@Override
	public BindType getBindType() {
		return BindType.BLOB;
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
	
	@Override
	public String toString(byte[] sqlValue) {
		return (sqlValue == null) ? null : Base64.encodeToString(sqlValue, Base64.NO_WRAP);
	}

}
