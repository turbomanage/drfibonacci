
package com.listapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.listapp.model.NamedList;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This database class extends the SQLiteOpenHelper A database file is created:
 * listapp.db It is possible to implement an own mechanism to store data on
 * database updates: Write your code inside the defined block inside the
 * "onUpgrade" method! More details about sqlite databases in android:
 * 
 * @see <a
 *      href="http://developer.android.com/guide/topics/data/data-storage.html#db">Tutorial</a>
 * @see <a
 *      href="http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html">Reference</a>
 * @author drfibonacci
 */

public class Database extends SQLiteOpenHelper {
    
    /* Change these for your database */
    private static final String DATABASE_NAME = "listapp.db";
    private static final int DATABASE_VERSION = 1;
    private static final Class<?>[] tables = {
            NamedList.class
    };
    /* End changes */

    private static final String TAG = Database.class.getCanonicalName();

    public static final int SQLTYPE_INTEGER = 0;
    public static final int SQLTYPE_REAL = 1;
    public static final int SQLTYPE_TEXT = 2;
    public static final int SQLTYPE_BLOB = 3;
    public static final int SQLTYPE_ID = 4;

    private static final String ID_COL = "_id";

    private final Map<String, Map<String, String>> metaModel;

    public Database(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        metaModel = inspectModel();
    }

    @Override
    public final void onCreate(final SQLiteDatabase db) {
        for (Class<?> table : tables) {
            String sqlCreateStmt = getSqlCreate(table);
            db.execSQL(sqlCreateStmt);
        }
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
        /* PROTECTED REGION ID(DatabaseUpdate) ENABLED START */

        // TODO Implement your database update functionality here and remove the
        // following method call!
        onUpgradeDropTablesAndCreate(db);

        /* PROTECTED REGION END */
    }

    /**
     * This basic upgrade functionality will destroy all old data on upgrade
     */
    public final void onUpgradeDropTablesAndCreate(final SQLiteDatabase db) {
        for (String tableName : metaModel.keySet()) {
            String sqlDropStmt = getSqlDrop(tableName);
            db.execSQL(sqlDropStmt);
        }
        onCreate(db);
    }

    private Map<String, Map<String, String>> inspectModel() {
        Map<String, Map<String, String>> classMap = new HashMap<String, Map<String, String>>();
        // For each class
        for (Class<?> table : tables) {
            // Get field names and types
            String tableName = getTableName(table);
            // Get field map
            Map<String, String> typeMap = new HashMap<String, String>();
            Field[] fields = table.getFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                String sqlType = getSqlType(field);
                typeMap.put(fieldName, sqlType);
            }
            classMap.put(tableName, typeMap);
        }
        return classMap;
    }

    private String getSqlCreate(Class clazz) {
        String tableName = getTableName(clazz);
        Map<String, String> fields = metaModel.get(tableName);
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(tableName);
        sb.append("(");
        sb.append(ID_COL + " ");
        sb.append(fields.get("id"));
        for (String fieldName : fields.keySet()) {
            if (!"id".equals(fieldName)) {
                sb.append(",");
                String colName = fieldName;
                String sqlType = fields.get(fieldName);
                sb.append(colName + " ");
                sb.append(sqlType);
            }
        }
        sb.append(")");
        Log.i(TAG, "sqlCreateStmt=" + sb.toString());
        return sb.toString();
    }

    private String getSqlDrop(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    private String getSqlType(Field f) {
        if ("id".equals(f.getName())) {
            return "INTEGER PRIMARY KEY AUTOINCREMENT";
        }
        return "TEXT";
    }
    
    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
