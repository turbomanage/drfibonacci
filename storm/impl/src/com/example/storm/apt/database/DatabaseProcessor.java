package com.example.storm.apt.database;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

import com.example.storm.api.Database;
import com.example.storm.apt.ClassProcessor;
import com.example.storm.apt.StormEnvironment;

public class DatabaseProcessor extends ClassProcessor {

	private DatabaseModel databaseModel;

	public DatabaseProcessor(Element el, StormEnvironment stormEnv) {
		super(el, stormEnv);
	}

	@Override
	public DatabaseModel getModel() {
		return this.databaseModel;
	}

	@Override
	public void populateModel() {
		Database dba = this.typeElement.getAnnotation(Database.class);
		databaseModel = new DatabaseModel(dba.name(), dba.version(), getQualifiedClassName());
		super.populateModel();
	}

	@Override
	protected void inspectField(VariableElement field) {
		// None to inspect
	}

}