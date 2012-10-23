package ${package};

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.storm.DatabaseHelper;
import com.example.storm.api.DatabaseFactory;
import com.example.storm.TableHelper;

/**
 * GENERATED CODE
 *
 * Singleton provides an instance of the {@link DatabaseHelper}.
 *
 * @author David M. Chandler
 */
public class ${factoryName} implements DatabaseFactory {

	private static final String DB_NAME = "${dbName}";
	private static final int DB_VERSION = ${dbVersion};
	private static final TableHelper[] TABLE_HELPERS = new TableHelper[] {
	<#list tableHelpers as th>
		new ${th}()<#if th_has_next>,</#if>
	</#list>
	};
	private static DatabaseHelper mInstance;

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

	public TableHelper[] getTableHelpers() {
		return TABLE_HELPERS;
	}

	private ${factoryName}() {
		// non-instantiable
	}

}
