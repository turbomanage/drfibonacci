package com.example.storm.api;

/**
 * Interface that entities can extend to enable synchronization
 * (not yet supported).
 * 
 * @author drfibonacci
 */
public interface Syncable {
	
	int getVersion();
	long getLastMod();
	long getLastSync();
	void setVersion(int version);
	void setLastMod(long lastMod);
	void setLastSync(long lastSync);
	
}
