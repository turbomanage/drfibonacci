package com.example.storm.apt.converter;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

import com.example.storm.api.Converter;
import com.example.storm.apt.ProcessorLogger;

/**
 * Obtains the list of all types declared in a {@link Converter#forTypes()}
 * annotation.
 * 
 * @author drfibonacci
 */
public class ConverterTypeAnnotationValuesVisitor extends SimpleAnnotationValueVisitor6<String[], ProcessorLogger> {

	@Override
	public String[] visitArray(List<? extends AnnotationValue> vals, ProcessorLogger logger) {
		List<String> types = new ArrayList<String>();
		for (AnnotationValue val : vals) {
			types.add(val.accept(new ConverterTypeAnnotationValueVisitor(), logger));
		}
		return types.toArray(new String[]{});
	}

}
