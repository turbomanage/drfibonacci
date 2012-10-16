package ${daoPackage};

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
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

	protected static Map<String,String> COLUMNS = new HashMap<String,String>();
	static {
		<#list fields as field>
		COLUMNS.put("${field.colName}","${field.javaType}");
		</#list>
	}
	
	@Override
	public String getTableName() {
		return "${tableName}";
	}
	
	@Override
	public Map<String,String> getColumns() {
		return COLUMNS;
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
		values[${field_index}] = new ${field.converterName}().toString(get${field.bindType}OrNull(c, ${field_index}));
		</#list>
		return values;
	}

	@Override
	public void bindRowValues(InsertHelper insHelper, String[] rowValues) {
		<#list fields as field>
		if (rowValues[${field_index}] == null) insHelper.bindNull(${field_index+1}); else insHelper.bind(${field_index+1}, new ${field.converterName}().fromString(rowValues[${field_index}]));
		</#list>
	}

	@Override
	public String[] getDefaultValues() {
		String[] values = new String[getColumns().size()];
		${entityName} defaultObj = new ${entityName}();
		<#list fields as field>
		values[${field_index}] = new ${field.converterName}().toString(new ${field.converterName}().toSql(defaultObj.${field.getter}()));
		</#list>
		return values;
	}

	public String getIdCol() {
		return "_id";
	}
	
	public ${entityName} newInstance(Cursor c) {
		${entityName} obj = new ${entityName}();
		<#list fields as field>
		obj.${field.setter}(new ${field.converterName}().fromSql(get${field.bindType}OrNull(c, "${field.colName}")));
		</#list>
		return obj;
	}

	public ContentValues getEditableValues(${entityName} obj) {
		ContentValues cv = new ContentValues();
		<#list fields as field>
		cv.put("${field.colName}", new ${field.converterName}().toSql(obj.${field.getter}()));
		</#list>	
		return cv;
	}
	
	public Map<String,String> getQueryValuesMap(${entityName} obj) {
		Map<String,String> queryMap = new HashMap<String,String>(); 
		${entityName} defaultObj = new ${entityName}();
		// Include fields in query if they differ from the default object
		<#list fields as field>
		if (obj.${field.getter}() != defaultObj.${field.getter}())
			queryMap.put("${field.colName}", "" + new ${field.converterName}().toSql(obj.${field.getter}()));
		</#list>
		return queryMap;	
	}

}