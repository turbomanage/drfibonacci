package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassModel {

	private String className;
	private String packageName;
	protected List<String> imports = new ArrayList<String>();
	protected List<FieldModel> fields = new ArrayList<FieldModel>();

	public abstract String getTemplatePath();
	/**
	 * Fully qualified classname, used to create generated file name
	 * 
	 * @return
	 */
	public abstract String getGeneratedClass();
	
	public List<String> getImports() {
		return imports;
	}

	public List<FieldModel> getFields() {
		return fields;
	}

	protected void addField(String fieldName, String javaType) {
		FieldModel field = new FieldModel(fieldName, javaType);
		fields.add(field);
		// add import for converter if needed
		String converterType = field.getQualifiedConverterClass();
		if (!imports.contains(converterType)) {
			imports.add(converterType);
		}
	}

	protected void addImport(String importPath) {
		imports.add(importPath);
	}

	/**
	 * @return Name of the generated class
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return Package name of the generated class
	 */
	public String getPackage() {
		return this.packageName;
	}

	/**
	 * Returns the fully qualified class name of the generated class.
	 * 
	 * @return Name of generated class with package prepended
	 */
	public String getQualifiedClassName() {
		return this.getPackage() + "." + this.getClassName();
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	protected String capFirst(String anyName) {
		String capFirstLetter = anyName.substring(0, 1).toUpperCase();
		return capFirstLetter + anyName.substring(1); 
	}


}