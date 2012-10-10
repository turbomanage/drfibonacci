package ${daoPackage};

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.storm.DatabaseHelper;
import java.util.Map;
import java.util.HashMap;
<#list imports as import>
import ${import};
</#list>

/**
 * GENERATED CLASS
 *
 * @author drfibonacci
 */
public class ${daoName} extends ${baseDaoName}<${entityName}>{

	public DatabaseHelper getDbHelper(Context ctx) {
		return ${dbFactory}.getDatabaseHelper(ctx);
	}
	
	public ${daoName}(Context ctx) {
		super(ctx);
	}
	
	public String getEntityName() {
		return "${entityName}";
	}
	
	public String getIdCol() {
		return "_id";
	}
	
	public ${entityName} newInstance(Cursor c) {
		${entityName} obj = new ${entityName}();
		<#list fields as field>
		obj.${field.setter}(new ${field.converterName}().fromSql(${field.cursorMethod}OrNull(c, "${field.colName}")));
		</#list>
		return obj;
	}

	public ${entityName} newInstance(Map<String,String> values) {
		${entityName} obj = new ${entityName}();
		<#list fields as field>
		obj.${field.setter}(new ${field.converterName}().fromString(values.get("${field.colName}")));
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
	
	public Cursor queryByExample(${entityName} obj) {
		Map<String,String> queryMap = new HashMap<String,String>(); 
		${entityName} defaultObj = new ${entityName}();
		// Include fields in query if they differ from the default object
		<#list fields as field>
		if (obj.${field.getter}() != defaultObj.${field.getter}())
			queryMap.put("${field.colName}", "" + new ${field.converterName}().toSql(obj.${field.getter}()));
		</#list>
		return queryByMap(queryMap);	
	}
	
}