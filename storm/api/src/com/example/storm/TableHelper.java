package com.example.storm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;

import com.example.storm.api.Persistable;

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
	protected abstract Map<String, String> getColumns();

	protected abstract String getTableName();

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
	protected abstract String[] getRowValues(Cursor c);

	protected abstract void bindRowValues(InsertHelper insHelper, String[] rowValues);
	
	/**
	 * Populate an array with the entity's default values for each field obtained
	 * by creating a new instance of the entity. These values are used to fill in
	 * any missing columns when importing from CSV.
	 *  
	 * @return
	 */
	protected abstract String[] getDefaultValues();

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

	public void backupAndRestore(DatabaseHelper dbHelper) {
		this.dumpToCsv(dbHelper);
		this.dropAndCreate(dbHelper);
		this.importFromCsv(dbHelper);
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

	// TODO move CSV methods to CsvWriter, CsvReader
	protected int dumpToCsv(DatabaseHelper dbHelper) {
		int numRowsWritten = 0;
		String filename = getCsvFilename(dbHelper);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor c = db.query(getTableName(), null, null, null, null, null, null);
		FileOutputStream fos;
		try {
			Context ctx = dbHelper.getContext();
			fos = ctx.openFileOutput(filename, 0);
			PrintWriter printWriter = new PrintWriter(fos);
			String headerRow = buildHeaderRow(c);
			printWriter.println(headerRow);
			for (boolean hasItem = c.moveToFirst(); hasItem; hasItem = c
					.moveToNext()) {
				String csv = buildCsvRow(c);
				printWriter.println(csv);
				numRowsWritten++;
			}
			printWriter.flush();
			printWriter.close();
			return numRowsWritten;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}

	protected String getCsvFilename(DatabaseHelper dbHelper) {
		String dbName = dbHelper.dbFactory.getName();
		String tableName = getTableName();
		int version = dbHelper.dbFactory.getVersion();
		return String.format("%s.v%d.%s", dbName, version, tableName);
	}

	protected String buildHeaderRow(Cursor c) {
		// Write column names in first row
		StringBuilder sb = new StringBuilder();
		String[] cols = c.getColumnNames();
		for (String colName : cols) {
			sb.append(',');
			sb.append(colName);
		}
		return sb.toString().substring(1);
	}

	protected String buildCsvRow(Cursor c) {
		StringBuilder sb = new StringBuilder();
		String[] values = this.getRowValues(c);
		for (int i = 0; i < c.getColumnCount(); i++) {
			String value = values[i];
			sb.append(',');
			sb.append(CsvUtils.escapeCsv(value));
		}
		return sb.toString().substring(1);
	}

	public int importFromCsv(DatabaseHelper dbHelper) {
		int numInserts = 0;
		Context ctx = dbHelper.getContext();
		String csvFilename = getCsvFilename(dbHelper);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		InsertHelper insertHelper = new DatabaseUtils.InsertHelper(db,
				getTableName());
		try {
			FileInputStream ois = ctx.openFileInput(csvFilename);
			InputStreamReader isr = new InputStreamReader(ois);
			BufferedReader reader = new BufferedReader(isr);
			String headerRow = reader.readLine();
			int[] cols = parseCsvHeader(headerRow, insertHelper);
			String[] defaultValues = getDefaultValues();
			String csvRow = reader.readLine();
			while (csvRow != null) {
				long rowId = parseAndInsert(csvRow, cols, defaultValues,
						insertHelper);
				if (rowId < 0) {
					throw new RuntimeException("Error after row " + numInserts);
				}
				numInserts++;
				csvRow = reader.readLine();
			}
			db.setTransactionSuccessful();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return numInserts;
	}

	private long parseAndInsert(String csvRow, int[] cols, String[] defaultValues,
			InsertHelper insertHelper) {
		List<String> textValues = CsvUtils.getValues(csvRow);
		insertHelper.prepareForInsert();
		String[] rowValues = mapValuesToTable(cols, textValues, defaultValues);
		this.bindRowValues(insertHelper, rowValues);
		return insertHelper.execute();
	}

	private String[] mapValuesToTable(int[] colMap, List<String> textValues, String[] defaultValues) {
		String[] rowValues = defaultValues.clone();
		for (int i=0; i < textValues.size(); i++) {
			 rowValues[colMap[i]] = textValues.get(i);
		}
		return rowValues;
	}

	/**
	 * Map each column in the CSV to a column in the new table.
	 * 
	 * @param headerRow
	 * @param insertHelper
	 * @return An array containing the table column # for each csv col
	 */
	private int[] parseCsvHeader(String headerRow, InsertHelper insertHelper) {
		String[] csvCols = headerRow.split(",");
		int[] colMap = new int[csvCols.length];
		for (int i = 0; i < csvCols.length; i++) {
			String colName = csvCols[i];
			int columnIndex = insertHelper.getColumnIndex(colName);
			colMap[i] = columnIndex - 1; // columnIndex is 1-based
		}
		return colMap;
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
	
}