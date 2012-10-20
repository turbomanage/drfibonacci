package com.example.storm.apt.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.storm.apt.ClassModel;
import com.example.storm.apt.StormEnvironment;
import com.example.storm.apt.entity.EntityModel;
import com.example.storm.csv.CsvUtils;

public class DatabaseModel extends ClassModel {

	private String dbName;
	int dbVersion;
	private List<EntityModel> entities = new ArrayList<EntityModel>();
	private List<String> tableHelpers = new ArrayList<String>();

	public DatabaseModel(String dbName, int dbVersion, String helperClass) {
		super();
		this.dbName = dbName;
		this.dbVersion = dbVersion;
		int lastDot = helperClass.lastIndexOf('.');
		String pkg = helperClass.substring(0, lastDot);
		this.setPackageName(pkg);
		this.setClassName(helperClass.substring(lastDot + 1));
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

	public void addEntity(EntityModel daoModel) {
		this.entities.add(daoModel);
		// Duplicate TableHelper info for use by index writer
		this.tableHelpers.add(daoModel.getTableHelperClass());
	}
	
	public String[] getTableHelpers() {
		return tableHelpers.toArray(new String[]{});
	}

	/**
	 * Populate the model of a database and its associated tables from
	 * a file in support of incremental compilation.
	 * 
	 * @param reader
	 * @return DatabaseModel
	 * @throws IOException
	 */
	public static DatabaseModel readFromIndex(BufferedReader reader) throws IOException {
		String dbInfo = reader.readLine();
		Map<String, String> props = CsvUtils.getAsMap(dbInfo);
		String dbName = props.get("dbName");
		int dbVersion = Integer.parseInt(props.get("dbVersion"));
		String helperClass = props.get("helperClass");
		DatabaseModel dbModel = new DatabaseModel(dbName, dbVersion, helperClass);
		// read TableHelpers
		List<String> tables = new ArrayList<String>();
		String th = reader.readLine();
		while (th != null && !th.equals(StormEnvironment.END_DATABASE)) {
			tables.add(th);
			th = reader.readLine();
		}
		dbModel.tableHelpers = tables;
		return dbModel;
	}
	
	/**
	 * Write the database info and associated tables to a file
	 * in support of incremental compilation.
	 * 
	 * @param out PrintWriter 
	 */
	public void writeToIndex(PrintWriter out) {
		out.println(StormEnvironment.BEGIN_DATABASE);
		Map<String,String> dbMap = new HashMap<String,String>();
		dbMap.put("dbName", this.dbName);
		dbMap.put("dbVersion", String.valueOf(this.dbVersion));
		dbMap.put("helperClass", this.getQualifiedClassName());
		String dbInfo = CsvUtils	.mapToCsv(dbMap);
		out.println(dbInfo);
		// write TableHelpers
		for (String th: this.tableHelpers) {
			out.println(th);
		}
		out.println(StormEnvironment.END_DATABASE);
	}

}
