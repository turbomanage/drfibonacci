package com.example.storm.apt.converter;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import com.example.storm.apt.ClassProcessor;
import com.example.storm.apt.StormEnvironment;

public class ConverterProcessor extends ClassProcessor {

	private ConverterModel cm;

	public ConverterProcessor(Element el, StormEnvironment stormEnv) {
		super(el, stormEnv);
	}

	@Override
	protected ConverterModel getModel() {
		return cm;
	}

	@Override
	// TODO verify presence of static GET field
	public void populateModel() {
		String converterClass = this.typeElement.getQualifiedName().toString();
		List<? extends AnnotationMirror> annoMirrors = this.typeElement
				.getAnnotationMirrors();
		for (AnnotationMirror anno : annoMirrors) {
			Map<? extends ExecutableElement, ? extends AnnotationValue> annoValues = anno
					.getElementValues();
			for (AnnotationValue val : annoValues.values()) {
				String[] types = val.accept(
						new ConverterTypeAnnotationValuesVisitor(),
						stormEnv.getLogger());
				for (String type : types) {
					if (TypeMapper.registerConverter(converterClass, type))
						stormEnv.getLogger()
								.info(converterClass + " registered for type "
										+ type);
					else
						stormEnv.getLogger()
								.error("Converter already registered for type "
										+ type, this.typeElement);
				}
			}
		}
	}

}