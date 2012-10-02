package com.example.storm.apt;

import java.util.List;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

public class ConverterTypeAnnotationValuesVisitor extends SimpleAnnotationValueVisitor6<String, ProcessorLogger> {

	@Override
	public String visitArray(List<? extends AnnotationValue> vals, ProcessorLogger logger) {
		logger.info("found array");
		String types = "";
		for (AnnotationValue val : vals) {
			types += val.accept(new ConverterTypeAnnotationValuesVisitor(), logger) + ",";
		}
		return types;
	}

	@Override
	public String visitType(TypeMirror type, ProcessorLogger logger) {
		logger.info("found type " + type.toString());
		return type.toString();
	}

}
