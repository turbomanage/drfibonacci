package com.example.storm.csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.database.DatabaseUtils;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;

import com.example.storm.DatabaseHelper;
import com.example.storm.TableHelper;


public class CsvTableReader {

	@SuppressWarnings("rawtypes")
	protected TableHelper th;
	private int[] colMap;
	private String[] defaultValues;
	private InsertHelper insertHelper;

	@SuppressWarnings("rawtypes")
	public CsvTableReader(TableHelper tableHelper) {
		this.th = tableHelper;
	}
	
	protected String getCsvFilename(DatabaseHelper dbHelper) {
		String dbName = dbHelper.getDbFactory().getName();
		String tableName = th.getTableName();
		int version = dbHelper.getDbFactory().getVersion();
		return String.format("%s.v%d.%s", dbName, version, tableName);
	}

	public int importFromCsv(DatabaseHelper dbHelper) {
		String filename = getCsvFilename(dbHelper);
		FileInputStream fileInputStream;
		try {
			fileInputStream = dbHelper.getContext().openFileInput(filename);
			return importFromCsv(dbHelper, fileInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int importFromCsv(DatabaseHelper dbHelper, InputStream is) {
		int numInserts = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		insertHelper = new DatabaseUtils.InsertHelper(db,
				th.getTableName());
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			String headerRow = reader.readLine();
			colMap = parseCsvHeader(headerRow);
			defaultValues = th.getDefaultValues();
			String csvRow = reader.readLine();
			while (csvRow != null) {
				long rowId = parseAndInsert(csvRow);
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

	private String[] mapValuesToTable(List<String> textValues) {
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
	 * @return An array containing the table column # for each csv col
	 */
	private int[] parseCsvHeader(String headerRow) {
		String[] csvCols = headerRow.split(",");
		int[] colMap = new int[csvCols.length];
		for (int i = 0; i < csvCols.length; i++) {
			String colName = csvCols[i];
			if (th.getColumns().get(colName) != null) {
				int columnIndex = insertHelper.getColumnIndex(colName);
				colMap[i] = columnIndex - 1; // columnIndex is 1-based
			}
		}
		return colMap;
	}

	private long parseAndInsert(String csvRow) {
		List<String> textValues = CsvUtils.getValues(csvRow);
		insertHelper.prepareForInsert();
		String[] rowValues = mapValuesToTable(textValues);
		th.bindRowValues(insertHelper, rowValues);
		return insertHelper.execute();
	}

}
