package com.example.storm.apt;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

public class ConverterTypeAnnotationValueVisitor extends SimpleAnnotationValueVisitor6<String, ProcessorLogger> {

	@Override
	public String visitType(TypeMirror type, ProcessorLogger logger) {
		logger.info("found type " + type.toString());
		return type.toString();
	}

}
