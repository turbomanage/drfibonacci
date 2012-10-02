package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an entity POJO. The public methods are referenced in a
 * FreeMarker template to generate a DAO class.
 * 
 * @author drfibonacci
 */
public class EntityModel extends ClassModel {

	private static final String BASE_DAO = "com.example.storm.SQLiteDao";

	protected String entityPackageName;
	protected String entityName;
	private List<String> sqlTypes = new ArrayList<String>();

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

	public String getBaseDao() {
		return BASE_DAO;
	}
}
