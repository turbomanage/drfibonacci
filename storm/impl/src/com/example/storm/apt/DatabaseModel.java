package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;

public class DatabaseModel extends ClassModel {

	private String dbName;
	int dbVersion;
	private List<EntityModel> entities = new ArrayList<EntityModel>();

	public DatabaseModel(String dbName, int dbVersion) {
		super();
		this.dbName = dbName;
		this.dbVersion = dbVersion;
	}

	public String getDbName() {
		return dbName;
	}

	public int getDbVersion() {
		return dbVersion;
	}
	
	public String getDbHelperClass() {
		return getQualifiedClassName();
	}
	
	public String getFactoryName() {
		return capFirst(this.getDbName()) + "Factory";
	}
	
	public String getFactoryClass() {
		return this.getPackage() + "." + getFactoryName();
	}

	public List<String> getDaoClasses() {
		List<String> daoClasses = new ArrayList<String>();
		for (EntityModel em : entities) {
			daoClasses.add(em.getDaoPackage() + "." + em.getDaoName());
		}
		return daoClasses;
	}

	void addEntity(EntityModel daoModel) {
		this.entities.add(daoModel);
	}
	
	public String[] getTableHelpers() {
		ArrayList<String> tableHelpers = new ArrayList<String>();
		for (EntityModel em : entities) {
			tableHelpers.add(em.getTableHelperClass());
		}
		return tableHelpers.toArray(new String[]{});
	}

}
