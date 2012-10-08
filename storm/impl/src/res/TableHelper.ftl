package ${package};

import com.example.storm.TableHelper;

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
public class SimpleEntityTable extends TableHelper {

	@Override
	public String getTableName() {
		return "${tableName}";
	}

	@Override
	public String createSql() {
		return
			"CREATE TABLE IF NOT EXISTS ${tableName}(" +
				<#list fields as field>
				"${field.colName} ${field.sqlType}<#if !field.nullable> NOT NULL</#if><#if field_has_next>,</#if>" +
				</#list>
			")";
	}

	@Override
	public String upgradeSql(int oldVersion, int newVersion) {
		return null;
	}

}