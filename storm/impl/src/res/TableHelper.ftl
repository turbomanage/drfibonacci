package ${package};

import android.database.sqlite.SQLiteDatabase;

import com.example.storm.api.TableHelper;

/**
 * GENERATED CODE
 * 
 * This class contains the SQL DDL for the named entity / table.
 * These methods are not included in the EntityDao class because
 * they must be executed before the Dao can be instantiated, and
 * they are instance methods vs. static so that they can be 
 * overridden in a typesafe manner.
 * 
 * @author drfibonacci
 */
public class SimpleEntityTable implements TableHelper {

	@Override
	public String getTableName() {
		return "${tableName}";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlStmt = 
			"CREATE TABLE ${tableName}(" +
				<#list fields as field>
				"${field.colName} ${field.sqlType}<#if !field.nullable> NOT NULL</#if><#if field_has_next>,</#if>" +
				</#list>
			")";
		db.execSQL(sqlStmt);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
		String sqlStmt = "DROP TABLE IF EXISTS ${tableName}";
		db.execSQL(sqlStmt);
		onCreate(db);
	}

}