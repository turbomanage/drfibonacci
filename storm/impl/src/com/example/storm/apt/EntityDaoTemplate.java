package com.example.storm.apt;

public class EntityDaoTemplate extends ClassTemplate {

	public EntityDaoTemplate(ClassModel model) {
		super(model);
	}

	@Override
	public String getTemplatePath() {
		return "EntityDao.ftl";
	}

	@Override
	public String getPackage() {
		return ((EntityModel) model).getDaoPackage();
	}

	@Override
	public String getGeneratedClass() {
		return getPackage() + "." + ((EntityModel) model).getDaoName();
	}

}
