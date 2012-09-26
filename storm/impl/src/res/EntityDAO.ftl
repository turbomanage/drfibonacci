package ${packageName};

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
<#list imports as import>
import ${import};
</#list>

public class ${className} extends ${baseDaoClass}<${entityName}>{

	public static void onCreate(SQLiteDatabase db) {
		String sqlStmt = 
			"CREATE TABLE ${tableName}(" +
				<#list fields as field>
				"${field.colName} ${field.sqlType}<#if field_has_next>,</#if>" +
				</#list>
			")";
		db.execSQL(sqlStmt);
	}
	
	public static void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
		String sqlStmt = "DROP TABLE IF EXISTS ${tableName}";
		db.execSQL(sqlStmt);
		onCreate(db);
	}
	
	public ${className}(Context ctx) {
		super(ctx);
	}
	
	public String getEntityName() {
		return "${entityName}";
	}
	
	public ${entityName} newInstance(Cursor c) {
		${entityName} obj = new ${entityName}();
		<#list fields as field>
		<#if field.javaType = "int" || field.javaType = "java.lang.Integer">
		obj.${field.setter}(c.getInt(c.getColumnIndexOrThrow("${field.colName}")));
		<#elseif field.javaType = "long" || field.javaType = "java.lang.Long">
		obj.${field.setter}(c.getLong(c.getColumnIndexOrThrow("${field.colName}")));
		<#elseif field.javaType = "java.lang.String">
		obj.${field.setter}(c.getString(c.getColumnIndexOrThrow("${field.colName}")));
		</#if>
		</#list>
		return obj;
	}
	
	public ContentValues getEditableValues(${entityName} obj) {
		ContentValues cv = new ContentValues();
		<#list fields as field>
		<#if "_id" != field.colName>
		cv.put("${field.colName}", obj.${field.getter}());
		</#if>
		</#list>	
		return cv;
	}
	
}