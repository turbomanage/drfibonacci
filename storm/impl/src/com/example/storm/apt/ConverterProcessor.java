package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

import com.example.storm.api.Converter;

public class ConverterProcessor extends ClassProcessor {

	private ConverterModel cm;
	
	public ConverterProcessor(Element el, ProcessorLogger logger) {
		super(el, logger);
	}

	@Override
	protected String getTemplatePath() {
		return null;
	}

	@Override
	protected ConverterModel getModel() {
		return cm;
	}

	@Override
	protected void populateModel() {
//		PackageElement pkg = (PackageElement) this.typeElement.getEnclosingElement();
//		String converterPkg = pkg.getQualifiedName().toString();
		String converterClass = this.typeElement.getQualifiedName().toString();
		List<? extends AnnotationMirror> annoMirrors = this.typeElement.getAnnotationMirrors();
		String types = null;
		for (AnnotationMirror anno : annoMirrors) {
			Map<? extends ExecutableElement, ? extends AnnotationValue> annoValues = anno.getElementValues();
			for (AnnotationValue val : annoValues.values()) {
				types = val.accept(new ConverterTypeAnnotationValuesVisitor(), logger);
			}
		}
//		this.cm = new ConverterModel(converterClass, converterPkg, types.split(","));
		for (String type : types.split(",")) {
			while (type != null) {
				if (TypeMapper.registerConverter(converterClass, type))
					logger.info(converterClass + " registered for type " + type);
				else
					logger.error("Converter already registered for type " + type, typeElement);
			}
		}
	}

	@Override
	protected void inspectField(VariableElement field) {
		// TODO Make this optional
	}

}
