package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import com.example.storm.SQLiteDao;
import com.example.storm.api.Persistable;
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
//		TODO Choose correct Dao base class based on interfaces
		this.entityModel = new EntityModel();
		PackageElement packageElement = (PackageElement) this.typeElement.getEnclosingElement();
		entityModel.entityPackageName = packageElement.getQualifiedName().toString();
		inspectClass(this.typeElement);
		entityModel.entityName = this.typeElement.getSimpleName().toString();
		entityModel.className = entityModel.entityName + "Dao";
		entityModel.addImport(entityModel.entityPackageName + "." + entityModel.entityName);
		readFields(typeElement);
	}

	private void inspectClass(TypeElement entity) {
		// get list of interfaces
		List<String> iNames = new ArrayList<String>();
		List<? extends TypeMirror> interfaces = entity.getInterfaces();
		for (TypeMirror i : interfaces) {
			String iName = i.toString();
			iNames.add(iName);
		}
		if (iNames.contains(Persistable.class.getName())) {
			this.entityModel.setBaseDaoClass(SQLiteDao.class);
		} else {
			logger.error(TAG + ": Entities must implement Persistable", entity);
		}
	}

	@Override
	protected void inspectField(VariableElement field) {
		Set<Modifier> modifiers = field.getModifiers();
		if (!modifiers.contains(Modifier.TRANSIENT)) {
			String javaType = getFieldType(field);
			try {
				String sqlType = TypeMapper.getSqlType(javaType);
				// TODO verify getter + setter
				entityModel.addField(field.getSimpleName().toString(), javaType);
			} catch (TypeNotSupportedException e) {
				logger.error(TAG, e, field);
			} catch (Exception e) {
				logger.error(TAG, e, field);
			}
		}
	}

}
