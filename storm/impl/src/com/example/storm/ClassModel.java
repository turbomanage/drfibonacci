package com.example.storm;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassModel {

	protected String className;
	protected String packageName = "com.example.storm.dao";
	protected List<String> imports = new ArrayList<String>();
	protected List<FieldModel> fields = new ArrayList<FieldModel>();

	public List<String> getImports() {
		return imports;
	}

	public List<FieldModel> getFields() {
		return fields;
	}

	protected void addField(String fieldName, String javaType) {
		fields.add(new FieldModel(fieldName, javaType));
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
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * Returns the fully qualified class name of the generated class.
	 * 
	 * @return Name of generated class with package prepended
	 */
	public String getQualifiedClassName() {
		return this.packageName + "." + this.className;
	}

}