package ${packageName};

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.storm.DatabaseHelper;
import com.example.storm.api.DatabaseFactory;

/**
 * GENERATED CLASS
 * 
 * Singleton provides an instance of the {@link DatabaseHelper}.
 * 
 * @author drfibonacci
 */
public class ${factoryName} implements DatabaseFactory {

	private static DatabaseHelper mInstance;
	private static String DB_NAME = "${dbName}";
	private static int DB_VERSION = ${dbVersion}; 

	/**
	 * Provides an instance of the DatabaseHelper.
	 * 
	 * @param ctx Application context
	 * @return {@link SQLiteOpenHelper} instance
	 */
	public static DatabaseHelper getDatabaseHelper(Context ctx) {
		if (mInstance==null) {
			newInstance(ctx);
		}
		return mInstance;
	}
	
	/**
	 * Create a new instance of the user's {$link DatabaseHelper} class.
	 * 
	 * @param ctx Application context
	 * @return DatabaseHelper instance
	 */
	private static void newInstance(Context ctx) {
		mInstance = new ${dbHelperClass}(ctx, new ${factoryName}());
	}
	
	public String getName() {
		return DB_NAME;
	}
	
	public int getVersion() {
		return DB_VERSION;
	}

	public void onCreate(SQLiteDatabase db) {
	<#list daoClasses as daoClass>
		${daoClass}.onCreate(db);	
	</#list>
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	<#list daoClasses as daoClass>
		${daoClass}.onUpgrade(db, oldVersion, newVersion);	
	</#list>
	}
		
	private ${factoryName}() {
		// non-instantiable
	}
	
}
