package com.example.storm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseFactory {

	private static String LOG_TAG = DatabaseFactory.class.getName();
	private static String ERR_MSG = "Failed to obtain instance of DatabaseHelper class.";
	private static SQLiteOpenHelper mInstance;

	/**
	 * Provides an instance of the DatabaseHelper.
	 * 
	 * @param ctx Application context
	 * @return {@link SQLiteOpenHelper} instance
	 */
	public static SQLiteOpenHelper getDatabaseHelper(Context ctx) {
		if (mInstance==null) {
			newInstance(ctx);
		}
		return mInstance;
	}
	
	/**
	 * Create a new instance of the generated DatabaseHelper class by calling
	 * the {@link Context} constructor via reflection. This enables test code
	 * and the {@link SQLiteDao} class to refer to the {@link SQLiteOpenHelper}
	 * class independent of the generated impl.
	 * 
	 * @param ctx
	 *            Application context
	 * @return 
	 * @return SQLiteOpenHelper instance
	 */
	private static void newInstance(Context ctx) {
		Class<?> dbHelperClass;
		try {
			dbHelperClass = Class.forName("com.example.storm.dao.DatabaseHelper");
			Constructor<?> ctxConstructor = dbHelperClass
					.getConstructor(Context.class);
			mInstance = (SQLiteOpenHelper) ctxConstructor.newInstance(ctx);
		} catch (ClassNotFoundException e) {
			Log.e(LOG_TAG, ERR_MSG, e);
		} catch (NoSuchMethodException e) {
			Log.e(LOG_TAG, ERR_MSG, e);
		} catch (IllegalArgumentException e) {
			Log.e(LOG_TAG, ERR_MSG, e);
		} catch (InstantiationException e) {
			Log.e(LOG_TAG, ERR_MSG, e);
		} catch (IllegalAccessException e) {
			Log.e(LOG_TAG, ERR_MSG, e);
		} catch (InvocationTargetException e) {
			Log.e(LOG_TAG, ERR_MSG, e);
		}
	}
	
	private DatabaseFactory() {
		// Factory itself is non-instantiable
	}

}
