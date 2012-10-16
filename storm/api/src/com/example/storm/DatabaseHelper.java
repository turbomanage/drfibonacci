package com.example.storm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.storm.api.Database;
import com.example.storm.api.DatabaseFactory;

/**
 * Default implementation of the SQLiteOpenHelper. Projects should extend this
 * class and annotate it with {@link Database}. Subclasses may override the
 * default onCreate and onUpgrade methods.
 * 
 * @author drfibonacci
 */
public abstract class DatabaseHelper extends SQLiteOpenHelper {

	public enum UpgradeStrategy {
		DROP_CREATE, BACKUP_RESTORE, UPGRADE
	}

	private DatabaseFactory dbFactory;
	protected Context mContext;

	public DatabaseHelper(Context ctx, DatabaseFactory dbFactory) {
		this(ctx, dbFactory.getName(), null, dbFactory.getVersion());
		this.mContext = ctx;
		this.setDbFactory(dbFactory);
	}

	/**
	 * Don't extend this one. You need dbFactory from the other constructor.
	 * 
	 * @param ctx
	 * @param dbName
	 * @param cursorFactory
	 * @param dbVersion
	 */
	private DatabaseHelper(Context ctx, String dbName,
			CursorFactory cursorFactory, int dbVersion) {
		super(ctx, dbName, null, dbVersion);
	}

	protected abstract UpgradeStrategy getUpgradeStrategy();

	public Context getContext() {
		return this.mContext;
	}

	/**
	 * Hook to replace any of the generated TableHelpers with your own.
	 * 
	 * @return
	 */
	protected TableHelper[] getTableHelpers() {
		return getDbFactory().getTableHelpers();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (TableHelper th : getTableHelpers()) {
			th.onCreate(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (getUpgradeStrategy()) {
		case DROP_CREATE:
			this.dropAndCreate();
			break;
		case BACKUP_RESTORE:
			this.backupAndRestore();
			break;
		case UPGRADE:
			this.upgrade(db, oldVersion, newVersion);
			break;
		}
	}

	public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (TableHelper th : getTableHelpers()) {
			th.onUpgrade(db, oldVersion, newVersion);
		}
	}

	public void backupAndRestore() {
		for (TableHelper th : getTableHelpers()) {
			th.backupAndRestore(this);
		}
	}

	public void dropAndCreate() {
		for (TableHelper th : getTableHelpers()) {
			th.dropAndCreate(this);
		}
	}

	/**
	 * Convenience method for testing or app backups
	 */
	public void backupAllTablesToCsv() {
		for (TableHelper table : getTableHelpers()) {
			table.backup(this);
		}
	}

	/**
	 * Convenience method for testing or app restores
	 */
	public void restoreAllTablesFromCsv() {
		for (TableHelper table : getTableHelpers()) {
			table.restore(this);
		}
	}

	public DatabaseFactory getDbFactory() {
		return dbFactory;
	}

	private void setDbFactory(DatabaseFactory dbFactory) {
		this.dbFactory = dbFactory;
	}

}
