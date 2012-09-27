package com.example.storm;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	public abstract Cursor queryByExample(T exampleObj);

	protected String getTableName() {
		return getEntityName();
	}

	public T getById(Long id) {
		Map<String, String> queryMap = newQueryMap();
		queryMap.put("_id", id.toString());
		Cursor c = queryByMap(queryMap);
		return asObject(c);
	}
	
	public T getByExample(T exampleObj) {
		return asObject(queryByExample(exampleObj));
	}
	
	public List<T> listAll() {
		return asList(queryAll());
	}
	
	public List<T> listByExample(T exampleObj) {
		return asList(queryByExample(exampleObj));
	}
	
	public List<T> listByMap(Map<String,String> queryMap) {
		return asList(queryByMap(queryMap));
	}
	
	public long put(T obj) {
		// Remove id from CV in case of insert
		ContentValues cv = getEditableValues(obj);
		long id = db.insertOrThrow(this.getTableName(), null, cv);
		obj.setId(id);
		return id;
	}

	public Cursor queryAll() {
		return db.query(getTableName(), null, null, null, null, null, null);
	}

	public Cursor queryByMap(Map<String,String> queryMap) {
		String where =""; 
		String[] args = new String[queryMap.size()];
		int i=0;
		for (String key : queryMap.keySet()) {
			String colName = key;
			String value = queryMap.get(key);
			where += " AND " + colName + "=?";
			args[i++] = value;
		}
		where = where.substring(5); // chop leading AND
		return db.query(getTableName(), null, where, args, null, null, null);
	}
	
	/**
	 * @param c
	 * @return
	 */
	public List<T> asList(Cursor c) {
		ArrayList<T> resultList = new ArrayList<T>();
		for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c.moveToNext()) {
			T obj = newInstance(c);
			resultList.add(obj);
		}
		return resultList;
	}
	
	/**
	 * Converts a {@link Cursor} to an object. Throws an exception if there
	 * was more than one row in the cursor.
	 * 
	 * @param c Cursor
	 * @return Object
	 */
	public T asObject(Cursor c) {
		if (c.getCount() > 1)
			throw new TooManyResultsException("Cursor returned " + c.getCount() + " rows");
		c.moveToFirst();
		return newInstance(c);
	}
	
	public Map<String,String> newQueryMap() {
		return new HashMap<String,String>();
	}
	
}