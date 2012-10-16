package com.example.storm.apt;


public abstract class ClassTemplate {

	protected ClassModel model;
	
	public ClassTemplate(ClassModel model) {
		this.model = model;
	}
	
	public abstract String getTemplatePath();
	public abstract String getPackage();
	public abstract String getGeneratedClass();
	
	public ClassModel getModel() {
		return this.model;
	}
	
}
