package com.example.storm;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@SupportedAnnotationTypes({ "com.example.storm.Entity","com.example.storm.Database" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MainProcessor extends AbstractProcessor {
	private ProcessorLogger logger;
	private Configuration cfg = new Configuration();
	private List<EntityModel> entities = new ArrayList<EntityModel>();

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		this.logger = new ProcessorLogger(processingEnv.getMessager());
		logger.info("Running MainProcessor...");

		cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/res"));

//		for (TypeElement annotationType : annotations) {}
		
		for (Element element : roundEnv.getElementsAnnotatedWith(Entity.class)) {
			Entity annotation = element.getAnnotation(Entity.class);
			EntityModel em = new EntityModel(element);
			processTemplateForModel(em);
			// retain for DatabaseHelper class 
			entities.add(em);
		}
		
		for (Element element : roundEnv.getElementsAnnotatedWith(Database.class)) {
			Database dbAnno = element.getAnnotation(Database.class);
			DatabaseModel dm = new DatabaseModel(element, dbAnno.name(), dbAnno.version());
			// Add entity DAOs
			for (EntityModel em : entities) {
				dm.addDaoClass(em.getQualifiedClassName());
			}
			processTemplateForModel(dm);
		}
		
		return true;
	}

	private void processTemplateForModel(ClassModel model) {
		JavaFileObject file;
		try {
			file = processingEnv.getFiler().createSourceFile(model.getQualifiedClassName());
			logger.info("Creating file  " + file.getName());
			Writer out = file.openWriter();
			Template t = cfg.getTemplate(model.getTemplatePath());
			logger.info("Processing template " + t.getName());
			t.process(model, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("EntityProcessor error", e, model.getElement());
		} catch (TemplateException e) {
			logger.error("EntityProcessor error", e, model.getElement());
		}
	}
}