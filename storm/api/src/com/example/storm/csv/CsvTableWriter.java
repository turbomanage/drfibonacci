package com.example.storm.csv;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.storm.DatabaseHelper;
import com.example.storm.TableHelper;

public class CsvTableWriter extends CsvTableReader {

	@SuppressWarnings("rawtypes")
	public CsvTableWriter(TableHelper tableHelper) {
		super(tableHelper);
	}

	private String buildCsvRow(Cursor c) {
		StringBuilder sb = new StringBuilder();
		String[] values = th.getRowValues(c);
		for (int i = 0; i < c.getColumnCount(); i++) {
			String value = values[i];
			sb.append(',');
			sb.append(CsvUtils.escapeCsv(value));
		}
		return sb.toString().substring(1);
	}

	private String buildHeaderRow(Cursor c) {
		// Write column names in first row
		StringBuilder sb = new StringBuilder();
		String[] cols = c.getColumnNames();
		for (String colName : cols) {
			sb.append(',');
			sb.append(colName);
		}
		return sb.toString().substring(1);
	}

	public int dumpToCsv(DatabaseHelper dbHelper) {
		int numRowsWritten = 0;
		String filename = getCsvFilename(dbHelper);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor c = db.query(th.getTableName(), null, null, null, null, null, null);
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

}
