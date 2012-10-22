package com.example.storm.apt.entity;

import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import com.example.storm.SQLiteDao;
import com.example.storm.api.Entity;
import com.example.storm.api.Persistable;
import com.example.storm.apt.ClassProcessor;
import com.example.storm.apt.StormEnvironment;
import com.example.storm.apt.converter.TypeMapper;
import com.example.storm.apt.database.DatabaseModel;
import com.example.storm.exception.TypeNotSupportedException;

public class EntityProcessor extends ClassProcessor {

	private static final String TAG = EntityProcessor.class.getName();
	private EntityModel entityModel;
	
	public EntityProcessor(Element el, StormEnvironment stormEnv) {
		super(el, stormEnv);
	}
	
	@Override
	public EntityModel getModel() {
		return this.entityModel;
	}
	
	@Override
	public void populateModel() {
		this.entityModel = new EntityModel();
		super.populateModel();
		this.entityModel.addImport(getQualifiedClassName());
		chooseBaseDao();
		chooseDatabase();
		readFields(typeElement);
	}

	protected void chooseBaseDao() {
		List<String> iNames = super.inspectInterfaces();
//		TODO Choose Dao base class based on interfaces
		if (iNames.contains(Persistable.class.getName())) {
			this.entityModel.setBaseDaoClass(SQLiteDao.class);
		} else {
			stormEnv.getLogger().error(TAG + ": Entities must implement Persistable", this.typeElement);
		}
	}

	protected void chooseDatabase() {
		Entity entity = this.typeElement.getAnnotation(Entity.class);
		DatabaseModel defaultDb = stormEnv.getDefaultDb();
		if (entity.dbName().length() > 0) {
			// Add db to entity model and vice versa
			String dbName = entity.dbName();
			DatabaseModel db = stormEnv.getDbByName(dbName);
			if (db != null) {
				this.entityModel.setDatabase(db);
			} else {
				stormEnv.getLogger().error(TAG + ": There is no @Database named " + dbName, this.typeElement);
			}
		} else if (defaultDb != null) {
			this.entityModel.setDatabase(defaultDb);
		} else {
			stormEnv.getLogger().error(TAG + ": You must define at least one @Database", this.typeElement);
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
				stormEnv.getLogger().error(TAG, e, field);
			} catch (Exception e) {
				stormEnv.getLogger().error(TAG, e, field);
			}
		}
	}

}
