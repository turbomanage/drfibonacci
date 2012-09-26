package ${packageName};

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.storm.SQLiteDao;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private Context mContext;
	private static final String dbName = "${dbName}";
	private static final int dbVersion = ${dbVersion};

	public DatabaseHelper(Context ctx) {
		super(ctx, dbName, null, dbVersion);
		this.mContext = ctx;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		<#list daoClasses as daoClass>
		${daoClass}.onCreate(db);		
		</#list>
	}

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
		<#list daoClasses as daoClass>
		${daoClass}.onUpgrade(db, oldVersion, newVersion);		
		</#list>
	}
	
}
