package com.example.storm.apt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.example.storm.apt.database.DatabaseModel;

/**
 * Top-level container for the source code models populated by
 * {@link MainProcessor} and its subsidiaries.
 * 
 * @author drfibonacci
 */
public class StormEnvironment {

	public static final String BEGIN_DATABASE = ":DB_START";
	public static final String END_DATABASE = ":DB_END";
	private static final String ENV_FILE = "stormEnv";
	private ProcessorLogger logger;
	private List<DatabaseModel> dbModels = new ArrayList<DatabaseModel>();

	StormEnvironment(ProcessorLogger logger) {
		this.logger = logger;
	}

	public ProcessorLogger getLogger() {
		return this.logger;
	}

	void addDatabase(DatabaseModel dbModel) {
		dbModels.add(dbModel);
	}
	
	List<DatabaseModel> getDbModels() {
		return dbModels;
	}
	
	public DatabaseModel getDbByName(String helperClass) {
		// iterate over all dbs and find matching
		for (DatabaseModel dbModel : dbModels) {
			if (dbModel.getDbHelperClass().equals(helperClass)) {
				return dbModel;
			}
		}
		return null;
	}
	
	public DatabaseModel getDefaultDb() {
		if (dbModels.size() > 0) {
			return dbModels.get(0);
		}
		return null;
	}
	
	/**
	 * Read the current model state from a file in support of incremental 
	 * compilation. This is necessary because the annotation processor has
	 * access to only classes which have been annotated (and any resulting
	 * generated classes on subsequent rounds), but DatabaseHelper classes
	 * aren't available when doing incremental compilation on a new @Entity.
	 * 
	 * @see
	 * http://stackoverflow.com/questions/10585665/how-can-i-examine-the-whole-source-tree-with-an-annotation-processor
	 * https://github.com/sentinelt/evo-classindex
	 * 
	 * @param Filer used by the annotation processor
	 */
	void readIndex(Filer filer) {
		StandardLocation location = StandardLocation.SOURCE_OUTPUT;
		FileObject indexFile;
		try {
			indexFile = filer.getResource(location, "com.example.storm", ENV_FILE);
			logger.info("Reading index " + indexFile.getName());
			Reader fileReader = indexFile.openReader(true);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = reader.readLine();
			while (line != null && line.startsWith(BEGIN_DATABASE)) {
				DatabaseModel dbModel = DatabaseModel.readFromIndex(reader);
				this.addDatabase(dbModel);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			// gulp--only way to catch not yet existing file on first run
		}
	}

	/**
	 * Write the current model state to a file in support of incremental 
	 * compilation.
	 * 
	 * @see StormEnvironment#readIndex(Filer)
	 * 
	 * @param Filer used by the annotation processor
	 */
	void writeIndex(Filer filer) {
		StandardLocation location = StandardLocation.SOURCE_OUTPUT;
		FileObject indexFile;
		try {
			indexFile = filer.createResource(location, "com.example.storm", ENV_FILE);
			OutputStream fos = indexFile.openOutputStream();
			PrintWriter out = new PrintWriter(fos);
			for (DatabaseModel dbModel : dbModels) {
				dbModel.writeToIndex(out);
			}
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}