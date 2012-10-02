package com.example.storm.apt;

public class ConverterModel extends ClassModel {

	private String[] types;

	public ConverterModel(String converterClass, String converterPkg,
			String[] types) {
		this.className = converterClass;
		this.packageName = converterPkg;
		this.types = types;
	}

	String[] getConvertForTypes() {
		return types;
	}

}
