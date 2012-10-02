package com.example.storm.apt;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

import com.example.storm.api.Database;

public class DatabaseProcessor extends ClassProcessor {

	private static final String TEMPLATE_PATH = "DatabaseHelper.ftl";
	
	private DatabaseModel databaseModel;

	public DatabaseProcessor(Element el, ProcessorLogger logger) {
		super(el, logger);
	}

	@Override
	public DatabaseModel getModel() {
		return this.databaseModel;
	}

	@Override
	protected String getTemplatePath() {
		return TEMPLATE_PATH;
	}

	@Override
	protected void populateModel() {
		Database dba = this.typeElement.getAnnotation(Database.class);
		databaseModel = new DatabaseModel(dba.name(), dba.version());
		databaseModel.className = "DatabaseHelper";
	}

	@Override
	protected void inspectField(VariableElement field) {
		// None to inspect
	}

	/**
	 * Add the DAO class associated with each entity to the DatabaseModel.
	 * 
	 * @param entities
	 */
	void addEntities(List<EntityModel> entities) {
		for (EntityModel em : entities) {
			databaseModel.addDaoClass(em.getQualifiedClassName());
		}
	}

}
