package com.example.storm;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;

/**
 * Representation of an entity POJO. The public methods are referenced
 * in a FreeMarker template to generate a DAO class. 
 * 
 * @author drfibonacci
 */
public class EntityModel extends ClassModel {
	
	private static final String TEMPLATE_PATH = "EntityDAO.ftl";
	private static final String BASE_DAO_PACKAGE = "com.example.storm";
	private static final String BASE_DAO_CLASS = "SQLiteDao";

	protected String entityPackageName;
	protected String entityName;
	private List<String> sqlTypes = new ArrayList<String>();

	public EntityModel(Element element) {
		super(element);
		PackageElement packageElement = (PackageElement) this.typeElement.getEnclosingElement();
		this.entityPackageName = packageElement.getQualifiedName().toString();
		this.entityName = this.typeElement.getSimpleName().toString();
		this.className = this.entityName + "Dao";
		addImport(entityPackageName + "." + entityName);
		addImport(getBaseDaoPackage() + "." + getBaseDaoClass());
	}

	public List<String> getSqlTypes() {
		return sqlTypes;
	}

	public String getBaseDaoPackage() {
		return BASE_DAO_PACKAGE;
	}
	public String getBaseDaoClass() {
		return BASE_DAO_CLASS;
	}

	@Override
	public String getTemplatePath() {
		return TEMPLATE_PATH;
	}

	public String getEntityPackageName() {
		return this.entityPackageName;
	}

	public String getEntityName() {
		return entityName;
	}
	
	public String getTableName() {
		return entityName;
	}

}
