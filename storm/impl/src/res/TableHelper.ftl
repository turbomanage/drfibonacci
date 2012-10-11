package ${package};

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
public class ${className} extends TableHelper<${entityName}> {

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
	public ${entityName} newInstance(Map<String,String> values) {
		${entityName} obj = new ${entityName}();
		<#list fields as field>
		obj.${field.setter}(new ${field.converterName}().fromString(values.get("${field.colName}")));
		</#list>
		return obj;
	}

}