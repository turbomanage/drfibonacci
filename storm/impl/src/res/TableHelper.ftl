package ${daoPackage};

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import com.example.storm.query.FilterBuilder;
import com.example.storm.TableHelper;
import java.util.Map;
import java.util.HashMap;
<#list imports as import>
import ${import};
</#list>

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
public class ${tableHelperName} extends TableHelper<${entityName}> {

	public enum Columns implements TableHelper.Column {
		<#list fields as field>
		${field.colName}<#if field_has_next>,</#if>
		</#list>
	}
	
	@Override
	public String getTableName() {
		return "${tableName}";
	}
	
	@Override
	public Column[] getColumns() {
		return Columns.values();
	}

	@Override	
	public Column getIdCol() {
		return Columns._ID;
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
	public String dropSql() {
		return "DROP TABLE IF EXISTS ${tableName}";
	}
	
	@Override
	public String upgradeSql(int oldVersion, int newVersion) {
		return null;
	}

	@Override
	public String[] getRowValues(Cursor c) {
		String[] values = new String[c.getColumnCount()];
		<#list fields as field>
		values[${field_index}] = ${field.converterName}.GET.toString(get${field.bindType}OrNull(c, ${field_index}));
		</#list>
		return values;
	}

	@Override
	public void bindRowValues(InsertHelper insHelper, String[] rowValues) {
		<#list fields as field>
		if (rowValues[${field_index}] == null) insHelper.bindNull(${field_index+1}); else insHelper.bind(${field_index+1}, ${field.converterName}.GET.fromString(rowValues[${field_index}]));
		</#list>
	}

	@Override
	public String[] getDefaultValues() {
		String[] values = new String[getColumns().length];
		${entityName} defaultObj = new ${entityName}();
		<#list fields as field>
		values[${field_index}] = ${field.converterName}.GET.toString(${field.converterName}.GET.toSql(defaultObj.${field.getter}()));
		</#list>
		return values;
	}

	@Override
	public ${entityName} newInstance(Cursor c) {
		${entityName} obj = new ${entityName}();
		<#list fields as field>
		obj.${field.setter}(${field.converterName}.GET.fromSql(get${field.bindType}OrNull(c, ${field_index})));
		</#list>
		return obj;
	}
	
	@Override
	public ContentValues getEditableValues(${entityName} obj) {
		ContentValues cv = new ContentValues();
		<#list fields as field>
		cv.put("${field.colName}", ${field.converterName}.GET.toSql(obj.${field.getter}()));
		</#list>	
		return cv;
	}
	
	@Override
	public FilterBuilder buildFilter(FilterBuilder filter, ${entityName} obj) {
		${entityName} defaultObj = new ${entityName}();
		// Include fields in query if they differ from the default object
		<#list fields as field>
		if (obj.${field.getter}() != defaultObj.${field.getter}())
			filter = filter.eq(Columns.${field.colName}, ${field.converterName}.GET.toSql(obj.${field.getter}()));
		</#list>
		return filter;	
	}

}