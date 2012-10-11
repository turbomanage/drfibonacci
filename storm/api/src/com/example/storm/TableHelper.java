package com.example.storm;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import com.example.storm.api.Persistable;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Base64;

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

	protected abstract T newInstance(Map<String, String> values);

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
		for (int i = 0; i < c.getColumnCount(); i++) {
			String value;
			String colName = c.getColumnName(i);
			if (c.isNull(i)) {
				value = "";
			} else if (getColType(colName) == "byte[]") {
				value = Base64.encodeToString(c.getBlob(i), Base64.NO_WRAP);
			} else {
				value = CsvUtils.escapeCsv(c.getString(i));
			}
			if (i > 0)
				sb.append(',');
			sb.append(value);
		}
		return sb.toString();
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
			boolean[] isBlobCol = getTypes(headerRow);
			insertHelper.prepareForInsert();
			String csvRow = reader.readLine();
			while (csvRow != null) {
				long rowId = parseAndInsert(csvRow, cols, isBlobCol,
						insertHelper);
				if (rowId < 0) {
					throw new RuntimeException("Error after row " + numInserts);
				}
				numInserts++;
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

	private boolean[] getTypes(String headerRow) {
		String[] colNames = headerRow.split(",");
		boolean[] isBlob = new boolean[colNames.length];
		for (int i = 0; i < colNames.length; i++) {
			String colName = colNames[i];
			if ("byte[]".equals(getColType(colName))) {
				isBlob[i] = true;
			}
		}
		return isBlob;
	}

	private long parseAndInsert(String csvRow, int[] cols, boolean[] isBlobCol,
			InsertHelper insertHelper) {
		String[] values = CsvUtils.parseRow(csvRow);
		for (int i = 0; i < values.length; i++) {
			String str = values[i];
			int colIdx = cols[i];
			if (str == null) {
				insertHelper.bindNull(colIdx);
			} else if (isBlobCol[i]) {
				byte[] blob = Base64.decode(str, Base64.DEFAULT);
				insertHelper.bind(colIdx, blob);
			} else {
				String val = CsvUtils.unescapeCsv(str);
				insertHelper.bind(colIdx, val);
			}
		}
		return insertHelper.execute();
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
			colMap[i] = columnIndex;
		}
		return colMap;
	}

	protected String getColType(String colName) {
		return getColumns().get(colName);
	}

}