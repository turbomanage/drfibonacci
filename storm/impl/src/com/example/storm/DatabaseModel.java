package com.example.storm;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

public class DatabaseModel extends ClassModel {

	private static final String TEMPLATE_PATH = "DatabaseHelper.ftl";
	private String dbName;
	int dbVersion;
	private List<String> daoClasses = new ArrayList<String>();
	
	public DatabaseModel(Element element, String name, int version) {
		super(element);
		this.dbName = name;
		this.dbVersion = version;
		this.className = "DatabaseHelper";
	}

	public String getDbName() {
		return dbName;
	}

	public int getDbVersion() {
		return dbVersion;
	}
	
	@Override
	public String getTemplatePath() {
		return TEMPLATE_PATH;
	}

	public List<String> getDaoClasses() {
		return daoClasses;
	}

	protected void addDaoClass(String qualifiedClassName) {
		daoClasses.add(qualifiedClassName);
	}
}
