package com.example.storm;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;

import com.example.storm.api.Persistable;
import com.example.storm.csv.CsvTableReader;
import com.example.storm.csv.CsvTableWriter;

/**
 * This class contains methods related to database creation and upgrades which
 * are executed before the database is fully ready for use by the DAOs. Its
 * methods are invoked by {@link DatabaseHelper#onCreate(SQLiteDatabase)}. To
 * customize the SQL for a particular table, create your own {@link TableHelper}
 * that overrides one or more of these methods and modify
 * {@link DatabaseHelper#getTableHelpers()} to return an instance of you custom
 * TableHelper.
 * 
 * @author drfibonacci
 */
public abstract class TableHelper<T extends Persistable> {

	private static final String TAG = TableHelper.class.getName();

	/**
	 * String representation of Java types associated with each column, like
	 * int, java.lang.Integer, byte[]
	 * 
	 * @return
	 */
	public abstract Map<String, String> getColumns();

	public abstract String getTableName();

	protected abstract String createSql();

	protected abstract String dropSql();

	protected abstract String upgradeSql(int oldVersion, int newVersion);

	/**
	 * Extract from a cursor a map containing each column name and value as a
	 * String. This is used by the CsvWriter and is necessary mainly because
	 * Cursor.getString truncates doubles and blobs need to be Base64 encoded.
	 * 
	 * @param c
	 *            Cursor
	 * @return Map<String colName, String colValue>
	 */
	public abstract String[] getRowValues(Cursor c);

	public abstract void bindRowValues(InsertHelper insHelper, String[] rowValues);
	
	/**
	 * Populate an array with the entity's default values for each field obtained
	 * by creating a new instance of the entity. These values are used to fill in
	 * any missing columns when importing from CSV.
	 *  
	 * @return
	 */
	public abstract String[] getDefaultValues();

	public abstract ContentValues getEditableValues(T obj);

	public abstract String getIdCol();

	public abstract T newInstance(Cursor c);

	// TODO make doubles (& blobs?) work right
	public abstract Map<String,String> getQueryValuesMap(T exampleObj);

	/**
	 * Create the table that represents the associated entity.
	 * 
	 * @param db
	 */
	protected void onCreate(SQLiteDatabase db) {
		db.execSQL(createSql());
	}

	/**
	 * Drop the table that represents the associated entity.
	 * 
	 * @param db
	 */
	protected void onDrop(SQLiteDatabase db) {
		db.execSQL(dropSql());
	}

	/**
	 * Upgrade the table that represents the associated entity. This will
	 * typically be an ALTER TABLE statement.
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	protected void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		db.execSQL(upgradeSql(oldVersion, newVersion));
	}

	/**
	 * Backup the current table and restore into the new schema.
	 *  
	 * @param dbHelper
	 */
	public void backupAndRestore(DatabaseHelper dbHelper) {
		this.backup(dbHelper);
		this.dropAndCreate(dbHelper);
		this.restore(dbHelper);
	}

	/**
	 * Back up the current table to a text file.
	 * 
	 * @param dbHelper
	 */
	public void backup(DatabaseHelper dbHelper) {
		new CsvTableWriter(this).dumpToCsv(dbHelper);
	}

	/**
	 * Restore table from a text file.
	 *  
	 * @param dbHelper
	 */
	public void restore(DatabaseHelper dbHelper) {
		new CsvTableReader(this).importFromCsv(dbHelper);
	}

	/**
	 * Drop table and recreate it.
	 * 
	 * @param dbHelper
	 */
	protected void dropAndCreate(DatabaseHelper dbHelper) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		this.onDrop(db);
		this.onCreate(db);
	}

	protected String getColType(String colName) {
		return getColumns().get(colName);
	}

	protected byte[] getBlobOrNull(Cursor c, int col) {
		return c.isNull(col) ? null : c.getBlob(col);
	}

	protected Double getDoubleOrNull(Cursor c, int col) {
		return c.isNull(col) ? null : c.getDouble(col);
	}

	protected Float getFloatOrNull(Cursor c, int col) {
		return c.isNull(col) ? null : c.getFloat(col);
	}

	protected Integer getIntOrNull(Cursor c, int col) {
		return c.isNull(col) ? null : c.getInt(col);
	}

	protected Long getLongOrNull(Cursor c, int col) {
		return c.isNull(col) ? null : c.getLong(col);
	}

	protected Short getShortOrNull(Cursor c, int col) {
		return c.isNull(col) ? null : c.getShort(col);
	}

	protected String getStringOrNull(Cursor c, int col) {
		return c.isNull(col) ? null : c.getString(col);
	}

	/*
	 * Methods to wrap Cursor get methods
	 */
	
	// TODO are these still necessary?
	protected byte[] getBlobOrNull(Cursor c, String colName) {
		int col = c.getColumnIndexOrThrow(colName);
		return c.isNull(col) ? null : c.getBlob(col);
	}

	protected Double getDoubleOrNull(Cursor c, String colName) {
		int col = c.getColumnIndexOrThrow(colName);
		return c.isNull(col) ? null : c.getDouble(col);
	}

	protected Float getFloatOrNull(Cursor c, String colName) {
		int col = c.getColumnIndexOrThrow(colName);
		return c.isNull(col) ? null : c.getFloat(col);
	}

	protected Integer getIntOrNull(Cursor c, String colName) {
		int col = c.getColumnIndexOrThrow(colName);
		return c.isNull(col) ? null : c.getInt(col);
	}

	protected Long getLongOrNull(Cursor c, String colName) {
		int col = c.getColumnIndexOrThrow(colName);
		return c.isNull(col) ? null : c.getLong(col);
	}

	protected Short getShortOrNull(Cursor c, String colName) {
		int col = c.getColumnIndexOrThrow(colName);
		return c.isNull(col) ? null : c.getShort(col);
	}

	protected String getStringOrNull(Cursor c, String colName) {
		int col = c.getColumnIndexOrThrow(colName);
		return c.isNull(col) ? null : c.getString(col);
	}
	
}