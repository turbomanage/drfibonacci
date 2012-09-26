package com.example.storm;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class SQLiteDao<T extends ModelBase> {

	private static final String TAG = SQLiteDao.class.getName();

	protected SQLiteDatabase db;
	protected Class<T> clazz;

	public SQLiteDao(Context ctx) {
		// Reflection voodoo to get the type parameter from the subclass
		Type genericSuperclass = getClass().getGenericSuperclass();
		// Allow this class to be safely instantiated with or without a
		// parameterized type
		if (genericSuperclass instanceof ParameterizedType)
			clazz = (Class<T>) ((ParameterizedType) genericSuperclass)
					.getActualTypeArguments()[0];

		this.db = DatabaseFactory.getDatabaseHelper(ctx).getWritableDatabase();
	}

	public abstract String getEntityName();
	public abstract T newInstance(Cursor c);
	public abstract ContentValues getEditableValues(T obj);

	protected String getTableName() {
		return getEntityName();
	}

	public long put(T obj) {
		// Remove id from CV in case of insert
		ContentValues cv = getEditableValues(obj);
		long id = db.insertOrThrow(this.getTableName(), null, cv);
		obj.setId(id);
		return id;
	}

	public List<T> listAll() {
		ArrayList<T> list = new ArrayList<T>();
		Cursor cursor = db.query(getTableName(), null, null, null, null, null,
				null);
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor
				.moveToNext()) {
			T obj = newInstance(cursor);
			list.add(obj);
		}
		return list;
	}

}