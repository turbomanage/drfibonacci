package com.example.storm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.storm.api.Database;
import com.example.storm.api.DatabaseFactory;

/**
 * Default implementation of the SQLiteOpenHelper.
 * Projects should extend this class and annotate it with
 * {@link Database}. Subclasses may override the default
 * onCreate and onUpgrade methods. 
 * 
 * @author drfibonacci
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	protected DatabaseFactory dbFactory;

	public DatabaseHelper(Context ctx, DatabaseFactory dbFactory) {
		this(ctx, dbFactory.getName(), null, dbFactory.getVersion());
		this.dbFactory = dbFactory;
	}
	
	/**
	 * Don't extend this one. You need dbFactory from the other constructor.
	 * 
	 * @param ctx
	 * @param dbName
	 * @param cursorFactory
	 * @param dbVersion
	 */
	private DatabaseHelper(Context ctx, String dbName, CursorFactory cursorFactory, int dbVersion) {
		super(ctx, dbName, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		dbFactory.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dbFactory.onUpgrade(db, oldVersion, newVersion);
	}

}
