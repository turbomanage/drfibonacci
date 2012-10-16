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
import android.database.sqlite.SQLiteOpenHelper;

import com.example.storm.api.Persistable;
import com.example.storm.exception.TooManyResultsException;

public abstract class SQLiteDao<T extends Persistable> {

	private static final String TAG = SQLiteDao.class.getName();

	protected SQLiteDatabase db;
	protected Class<T> clazz;
	protected TableHelper<T> th;

	@SuppressWarnings("unchecked")
	public SQLiteDao(Context ctx) {
		// Reflection voodoo to get the type parameter from the subclass
		Type genericSuperclass = getClass().getGenericSuperclass();
		// Allow this class to be safely instantiated with or without a
		// parameterized type
		if (genericSuperclass instanceof ParameterizedType)
			clazz = (Class<T>) ((ParameterizedType) genericSuperclass)
					.getActualTypeArguments()[0];

		this.db = getDbHelper(ctx).getWritableDatabase();
		this.th = getTableHelper();
	}

	/**
	 * Generated subclasses implement to point to the correct 
	 * {@link SQLiteOpenHelper} for the entity.
	 * 
	 * @param Context ctx
	 * @return DatabaseHelper 
	 */
	public abstract DatabaseHelper getDbHelper(Context ctx);
	@SuppressWarnings("rawtypes")
	public abstract TableHelper getTableHelper();
	public int delete(Long id) {
		if (id != null) {
			return db.delete(getTableName(), th.getIdCol() + "=?", new String[]{id.toString()});
		}
		return 0;
	}
	
	public int deleteAll() {
		return db.delete(getTableName(), null, null);
	}
	
	protected String getTableName() {
		return th.getTableName();
	}

	public T get(Long id) {
		Map<String, String> queryMap = newQueryMap();
		queryMap.put("_id", id.toString());
		Cursor c = queryByMap(queryMap);
		return asObject(c);
	}
	
	public T getByExample(T exampleObj) {
		return asObject(queryByExample(exampleObj));
	}
	
	public SQLiteDatabase getDatabase() {
		return db;
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

	/**
	 * Insert a row in the database. If the object's id is the
	 * default long (0), the db will generate an id.
	 * 
	 * @param obj
	 * @return
	 */
	public long insert(T obj) {
		ContentValues cv = th.getEditableValues(obj);
		if (obj.getId() == 0) {
			// the default, remove from ContentValues to allow autoincrement
			cv.remove(th.getIdCol());
		}
		long id = db.insertOrThrow(this.getTableName(), null, cv);
		obj.setId(id);
		return id;
	}

	// TODO add insertMany(List<T>)
	
	/**
	 * Insert or update, depending on whether the ID column is set to
	 * a non-default value.
	 * 
	 * @param obj
	 * @return
	 */
	public long update(T obj) {
		ContentValues cv = th.getEditableValues(obj);
		Long id = obj.getId();
		int numRowsUpdated = db.update(this.getTableName(), cv, th.getIdCol()
				+ "=?", new String[] { id.toString() });
		return numRowsUpdated;
	}

	public Cursor queryAll() {
		return db.query(getTableName(), null, null, null, null, null, null);
	}

	public Cursor queryByExample(T obj) {
		Map<String, String> queryValuesMap = th.getQueryValuesMap(obj);
		return queryByMap(queryValuesMap);
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
			T obj = th.newInstance(c);
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
		if (c.getCount() == 1) {
			c.moveToFirst();
			return th.newInstance(c);
		} else if (c.getCount() > 1) {
			throw new TooManyResultsException("Cursor returned " + c.getCount() + " rows");
		}
		return null;
	}
	
	public Map<String,String> newQueryMap() {
		return new HashMap<String,String>();
	}
	
	/*
	 * Methods to wrap Cursor get methods
	 */
	
}