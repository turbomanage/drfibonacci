
package com.example.storm;

import android.database.Cursor;

public abstract class ModelBase {

    public interface Columns {
        String ID = "_id";
        String VERSION = "version";
        String LAST_MOD = "lastMod";
        String LAST_SYNC = "lastSync";
        String[] BASE_COLUMNS = {
                ID, VERSION, LAST_MOD, LAST_SYNC
        };
    }

    public long id;
    public int version=0;
    public long lastMod=0; // ms since epoch
    public long lastSync=0; // ms since epoch

    public ModelBase (Cursor c) {
        this.id = c.getLong(c.getColumnIndexOrThrow(Columns.ID));
        this.version = c.getInt(c.getColumnIndexOrThrow(Columns.VERSION));
        this.lastMod = c.getLong(c.getColumnIndexOrThrow(Columns.LAST_MOD));
        this.lastSync = c.getLong(c.getColumnIndexOrThrow(Columns.LAST_SYNC));
    }

    public ModelBase() {
        // Empty ctor for Jackson
    }

    protected String[] allColumns(String... colName) {
        int baseLen = Columns.BASE_COLUMNS.length;
        String[] allColumns = new String[baseLen + colName.length];
        for (int i = 0; i < colName.length; i++) {
            allColumns[baseLen + i] = colName[i];
        }
        return allColumns;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getLastMod() {
		return lastMod;
	}

	public void setLastMod(long lastMod) {
		this.lastMod = lastMod;
	}

	public long getLastSync() {
		return lastSync;
	}

	public void setLastSync(long lastSync) {
		this.lastSync = lastSync;
	}

}
