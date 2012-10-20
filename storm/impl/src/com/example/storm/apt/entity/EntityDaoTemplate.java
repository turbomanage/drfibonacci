package com.example.storm.apt.entity;

import com.example.storm.apt.ClassModel;
import com.example.storm.apt.ClassTemplate;

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
