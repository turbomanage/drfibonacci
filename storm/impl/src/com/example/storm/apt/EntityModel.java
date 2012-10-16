package com.example.storm.apt;

import com.example.storm.SQLiteDao;


public class EntityModel extends ClassModel {

	private static final String TABLE_SUFFIX = "Table";
	private Class<SQLiteDao> baseDaoClass;
	private DatabaseModel dbModel;

	public String getEntityName() {
		return this.getClassName();
	}
	
	public String getDaoName() {
		return this.getEntityName() + "Dao";
	}
	
	public String getDaoPackage() {
		return this.getPackage() + ".dao";
	}

	public String getDbFactory() {
		return dbModel.getFactoryClass();
	}
	
	/**
	 * Provides the simple name of the base DAO class to templates.
	 * 
	 * @return String Simple name of the base DAO
	 */
	public String getBaseDaoName() {
		return this.baseDaoClass.getSimpleName();
	}

	protected Class<SQLiteDao> getBaseDaoClass() {
		return baseDaoClass;
	}

	protected void setBaseDaoClass(Class<SQLiteDao> daoClass) {
		this.baseDaoClass = daoClass;
		// add corresponding import
		this.addImport(daoClass.getCanonicalName());
	}

	void setDatabase(DatabaseModel dbModel) {
		this.dbModel = dbModel;
		dbModel.addEntity(this);
	}
	
	public String getTableHelperClass() {
		return getDaoPackage() + "." + getTableHelperName();
	}
	
	public String getTableHelperName() {
		return getTableName() + TABLE_SUFFIX;
	}

	public String getTableName() {
		// TODO Make configurable in @Entity
		return getEntityName();
	}

}