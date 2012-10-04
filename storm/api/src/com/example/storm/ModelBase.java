
package com.example.storm;

import com.example.storm.api.Persistable;


/**
 * Optional base class that entities can extend to
 * enable synchronization (not yet supported). 
 * 
 * @author drfibonacci
 */
public abstract class ModelBase implements Persistable {

    public long id;
    public int version;
    public long lastMod; // ms since epoch
    public long lastSync; // ms since epoch

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
