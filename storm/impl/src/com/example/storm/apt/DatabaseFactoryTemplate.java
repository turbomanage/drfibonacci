package com.example.storm.apt;

public class DatabaseFactoryTemplate extends ClassTemplate {

	public DatabaseFactoryTemplate(ClassModel model) {
		super(model);
	}

	@Override
	public String getTemplatePath() {
		return "DatabaseFactory.ftl";
	}

	@Override
	public String getPackage() {
		return ((DatabaseModel) model).getPackage();
	}

	@Override
	public String getGeneratedClass() {
		return ((DatabaseModel) model).getFactoryClass();
	}

}
