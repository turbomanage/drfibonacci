
package com.example.storm;


public abstract class ModelBase {

    public Long id;
    public Integer version;
    public Long lastMod; // ms since epoch
    public Long lastSync; // ms since epoch

    public ModelBase() {
        // Empty ctor for Jackson
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getLastMod() {
		return lastMod;
	}

	public void setLastMod(Long lastMod) {
		this.lastMod = lastMod;
	}

	public Long getLastSync() {
		return lastSync;
	}

	public void setLastSync(Long lastSync) {
		this.lastSync = lastSync;
	}

}
