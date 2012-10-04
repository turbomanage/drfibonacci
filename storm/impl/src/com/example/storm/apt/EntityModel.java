package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;

import com.example.storm.SQLiteDao;

/**
 * Representation of an entity POJO. The public methods are referenced in a
 * FreeMarker template to generate a DAO class.
 * 
 * @author drfibonacci
 */
public class EntityModel extends ClassModel {

	protected String entityPackageName;
	protected String entityName;
	private List<String> sqlTypes = new ArrayList<String>();

	private Class<SQLiteDao> baseDaoClass;

	public List<String> getSqlTypes() {
		return sqlTypes;
	}

	public String getEntityPackageName() {
		return this.entityPackageName;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getTableName() {
		return entityName;
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
}
