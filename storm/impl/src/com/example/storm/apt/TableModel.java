package com.example.storm.apt;

import java.util.List;

public class TableModel extends ClassModel {

	private static final String TABLE_SUFFIX = "Table";
	private EntityModel entityModel;

	public TableModel(EntityModel em) {
		this.entityModel = em;
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
		return entityModel.getDaoPackage() + "." + getTableName() + TABLE_SUFFIX;
	}

	public List<FieldModel> getFields() {
		return entityModel.getFields();
	}
	
	public String getTableName() {
		// TODO Make configurable in @Entity
		return entityModel.getEntityName();
	}
	
}
