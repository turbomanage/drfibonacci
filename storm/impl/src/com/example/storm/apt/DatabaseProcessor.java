package com.example.storm.apt;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

import com.example.storm.api.Database;

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
	protected void populateModel() {
		Database dba = this.typeElement.getAnnotation(Database.class);
		databaseModel = new DatabaseModel(dba.name(), dba.version());
		super.populateModel();
	}

	@Override
	protected void inspectField(VariableElement field) {
		// None to inspect
	}

}