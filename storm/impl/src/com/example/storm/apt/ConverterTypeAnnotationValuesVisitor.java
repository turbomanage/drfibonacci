package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

public class ConverterTypeAnnotationValuesVisitor extends SimpleAnnotationValueVisitor6<String[], ProcessorLogger> {

	@Override
	public String[] visitArray(List<? extends AnnotationValue> vals, ProcessorLogger logger) {
		logger.info("found array");
		List<String> types = new ArrayList<String>();
		for (AnnotationValue val : vals) {
			types.add(val.accept(new ConverterTypeAnnotationValueVisitor(), logger));
		}
		return types.toArray(new String[]{});
	}

}
