package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;

/**
 * Top-level container for the source code models populated by
 * {@link MainProcessor} and its subsidiaries.
 * 
 * @author drfibonacci
 */
public class StormEnvironment {

	private ProcessorLogger logger;
	private List<DatabaseProcessor> dbProcessors = new ArrayList<DatabaseProcessor>();

	StormEnvironment(ProcessorLogger logger) {
		this.logger = logger;
	}

	ProcessorLogger getLogger() {
		return this.logger;
	}

	void addDatabase(DatabaseProcessor dbProc) {
		dbProcessors.add(dbProc);
		DatabaseModel dbModel = dbProc.getModel();
		String dbName = dbModel.getDbName();
	}
	
	List<DatabaseProcessor> getDbProcessors() {
		return dbProcessors;
	}
	
	DatabaseModel getDbByName(String helperClass) {
		// iterate over all dbs and find matching
		for (DatabaseProcessor dbProc : dbProcessors) {
			DatabaseModel dbModel = dbProc.getModel();
			if (dbModel.getDbHelperClass().equals(helperClass)) {
				return dbModel;
			}
		}
		return null;
	}
	
	DatabaseModel getDefaultDb() {
		if (dbProcessors.size() > 0) {
			return dbProcessors.get(0).getModel();
		}
		return null;
	}
	
}