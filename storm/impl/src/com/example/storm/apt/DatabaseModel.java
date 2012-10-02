package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;

public class DatabaseModel extends ClassModel {

	private String dbName;
	int dbVersion;
	private List<String> daoClasses = new ArrayList<String>();
	
	public DatabaseModel(String name, int version) {
		this.dbName = name;
		this.dbVersion = version;
	}

	public String getDbName() {
		return dbName;
	}

	public int getDbVersion() {
		return dbVersion;
	}
	
	public List<String> getDaoClasses() {
		return daoClasses;
	}

	protected void addDaoClass(String qualifiedClassName) {
		daoClasses.add(qualifiedClassName);
	}
}
