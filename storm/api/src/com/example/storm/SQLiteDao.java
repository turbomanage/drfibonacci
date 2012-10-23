package com.example.storm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.storm.api.Persistable;
import com.example.storm.exception.TooManyResultsException;
import com.example.storm.query.FilterBuilder;

public abstract class SQLiteDao<T extends Persistable> {

	private static final String TAG = SQLiteDao.class.getName();

	protected SQLiteDatabase db;
	protected TableHelper<T> th;

	@SuppressWarnings("unchecked")
	public SQLiteDao(Context ctx) {
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
			return db.delete(th.getTableName(), th.getIdCol() + "=?", new String[]{id.toString()});
		}
		return 0;
	}
	
	public int deleteAll() {
		return db.delete(th.getTableName(), null, null);
	}
	
	/**
	 * Return an object to construct a filter with AND conditions.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FilterBuilder filter() {
		return new FilterBuilder((SQLiteDao<Persistable>) this);
	}
	
	public T get(Long id) {
		return asObject(filter().eq(th.getIdCol(), id).exec());
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
	
	/**
	 * Insert a row in the database. If the object's id is the
	 * default long (0), the db will generate an id.
	 * 
	 * @param obj
	 * @return row ID of newly inserted or -1 if err
	 */
	public long insert(T obj) {
		ContentValues cv = th.getEditableValues(obj);
		if (obj.getId() == 0) {
			// the default, remove from ContentValues to allow autoincrement
			cv.remove(th.getIdCol().toString());
		}
		long id = db.insertOrThrow(th.getTableName(), null, cv);
		obj.setId(id);
		return id;
	}

	/**
	 * Efficiently insert a collection of objects using {@link InsertHelper}.
	 *  
	 * @param many Collection of objects
	 * @return count of inserted objects or -1 immediately if any errors
	 */
	public long insertMany(Iterable<T> many) {
		long numInserted = 0;
		InsertHelper insertHelper = new DatabaseUtils.InsertHelper(db, th.getTableName());
		db.beginTransaction();
		try {
			for (T obj : many) {
				ContentValues cv = th.getEditableValues(obj);
				if (obj.getId() == 0) {
					// the default, remove from ContentValues to allow autoincrement
					cv.remove(th.getIdCol().toString());
				}
				long id = insertHelper.insert(cv);
				if (id == -1)
					return -1;
				numInserted++;
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return numInserted;
	}
	
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
		int numRowsUpdated = db.update(th.getTableName(), cv, th.getIdCol()
				+ "=?", new String[] { id.toString() });
		return numRowsUpdated;
	}
	
	public Cursor query(String where, String[] params) {
		return db.query(th.getTableName(), null, where, params, null, null, null);
	}
	
	public Cursor queryAll() {
		return query(null, null);
	}

	public Cursor queryByExample(T obj) {
		return th.buildFilter(this.filter(), obj).exec();
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