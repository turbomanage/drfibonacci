package com.example.storm.apt;

import java.util.List;

public class TableModel extends ClassModel {

	private static final String TABLE_SUFFIX = "Table";
	private EntityModel entityModel;

	public TableModel(EntityModel em) {
		this.entityModel = em;
		addImport(em.getQualifiedClassName());
		addConverterImports();
	}

	private void addConverterImports() {
		for (FieldModel field : entityModel.getFields()) {
			addImport(field.getQualifiedConverterClass());
		}
	}

	public String getEntityName() {
		return this.entityModel.getEntityName();
	}
	
	@Override
	public String getPackage() {
		return this.entityModel.getDaoPackage();
	}
	
	@Override
	public String getTemplatePath() {
		return "TableHelper.ftl";
	}

	@Override
	public String getGeneratedClass() {
		return entityModel.getDaoPackage() + "." + getClassName();
	}

	public List<FieldModel> getFields() {
		return entityModel.getFields();
	}
	
	public String getTableName() {
		// TODO Make configurable in @Entity
		return entityModel.getEntityName();
	}
	
	@Override
	public String getClassName() {
		return getTableName() + TABLE_SUFFIX;
	}
	
}
