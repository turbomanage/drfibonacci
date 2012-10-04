package com.example.storm.apt;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

import com.example.storm.api.Converter;

/**
 * Obtains the name of one type listed in {@link Converter#forTypes()}.
 * 
 * @author drfibonacci
 */
public class ConverterTypeAnnotationValueVisitor extends SimpleAnnotationValueVisitor6<String, ProcessorLogger> {

	@Override
	public String visitType(TypeMirror type, ProcessorLogger logger) {
		return type.toString();
	}

}
