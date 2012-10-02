package com.example.storm.apt;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.VariableElement;

import com.example.storm.exception.TypeNotSupportedException;

public class EntityProcessor extends ClassProcessor {

	private static final String TAG = EntityProcessor.class.getName();
	private static final String TEMPLATE_PATH = "EntityDAO.ftl";
	protected EntityModel entityModel;
	
	protected EntityProcessor(Element el, ProcessorLogger logger) {
		super(el, logger);
	}
	
	@Override
	protected EntityModel getModel() {
		return this.entityModel;
	}

	@Override
	protected String getTemplatePath() {
		return TEMPLATE_PATH;
	}

	@Override
	protected void populateModel() {
		this.entityModel = new EntityModel();
		PackageElement packageElement = (PackageElement) this.typeElement.getEnclosingElement();
		entityModel.entityPackageName = packageElement.getQualifiedName().toString();
		entityModel.entityName = this.typeElement.getSimpleName().toString();
		entityModel.className = entityModel.entityName + "Dao";
		entityModel.addImport(entityModel.entityPackageName + "." + entityModel.entityName);
		readFields(typeElement);
		verifyIdField();
	}

	private void verifyIdField() {
		// Ensure that there is a field named 'id' of type int or long
		for (FieldModel field : entityModel.getFields()) {
			if ("id".equals(field.getFieldName())) { 
				if ("int".equals(field.getJavaType()) || "long".equals(field.getJavaType())) {
					return;
				}
			}
		}
		logger.error(TAG + ": entity must contain an id field of type int or long", typeElement);
	}

	@Override
	protected void inspectField(VariableElement field) {
		Set<Modifier> modifiers = field.getModifiers();
		if (!modifiers.contains(Modifier.TRANSIENT)
				&& !modifiers.contains(Modifier.PRIVATE)) {
			String javaType = getFieldType(field);
			try {
				String sqlType = TypeMapper.getSqlType(javaType);
				entityModel.addField(field.getSimpleName().toString(), javaType);
			} catch (TypeNotSupportedException e) {
				logger.error(TAG, e, field);
			} catch (Exception e) {
				logger.error(TAG, e, field);
			}
		}
	}

}
