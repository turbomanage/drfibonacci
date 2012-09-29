package com.example.storm;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Base class that introspects an annotated class using the Mirror API and
 * populates a model for use by a code generation template.
 * 
 * @author drfibonacci
 */
/**
 * @author drfibonacci
 *
 */
public abstract class ClassProcessor {

	protected TypeElement typeElement;
	protected ProcessorLogger logger;
	
	/**
	 * Subclasses override to provide a template to Freemarker. 
	 * 
	 * @return Path to the template
	 */
	protected abstract String getTemplatePath();
	
	/**
	 * Subclasses override to provide a model for a template.
	 * 
	 * @return ClassModel
	 */
	protected abstract ClassModel getModel();

	/**
	 * Subclasses override to populate the model.
	 * Invoked by main annotation processor.
	 */
	protected abstract void populateModel();
	
	/**
	 * Subclasses override to inspect each field and possibly add it
	 * to the model. Invoked by main annotation processor.
	 * 
	 * @param field VariableElement that represents a class field
	 */
	protected abstract void inspectField(VariableElement field);

	/**
	 * Constructor intended to be overridden by subclasses.
	 * 
	 * @param el
	 * @param logger
	 */
	protected ClassProcessor(Element el, ProcessorLogger logger) {
		this.typeElement = (TypeElement) el;
		this.logger = logger;
	}

	protected void readFields(TypeElement type) {
		// Read fields from superclass if any
		TypeMirror superClass = type.getSuperclass();
		if (TypeKind.DECLARED.equals(superClass.getKind())) {
			DeclaredType superType = (DeclaredType) superClass;
			readFields((TypeElement) superType.asElement());
		}
		for (Element child : type.getEnclosedElements()) {
			if (child.getKind() == ElementKind.FIELD) {
				VariableElement field = (VariableElement) child;
				inspectField(field);
			}
		}
	}

	protected String getFieldType(VariableElement field) {
		TypeMirror fieldType = field.asType();
		return fieldType.toString();
	}

	void processTemplate(ProcessingEnvironment processingEnv, Configuration cfg) {
		JavaFileObject file;
		try {
			file = processingEnv.getFiler().createSourceFile(
					getModel().getQualifiedClassName());
			logger.info("Creating file  " + file.getName());
			Writer out = file.openWriter();
			Template t = cfg.getTemplate(getTemplatePath());
			logger.info("Processing template " + t.getName());
			t.process(getModel(), out);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("EntityProcessor error", e, this.typeElement);
		} catch (TemplateException e) {
			logger.error("EntityProcessor error", e, this.typeElement);
		}
	}

}