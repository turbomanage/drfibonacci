package com.example.storm.apt;

import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import com.example.storm.api.Converter;
import com.example.storm.api.Database;
import com.example.storm.api.Entity;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@SupportedAnnotationTypes({ "com.example.storm.api.Entity","com.example.storm.api.Database","com.example.storm.api.Converter" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MainProcessor extends AbstractProcessor {
	private ProcessorLogger logger;
	private Configuration cfg = new Configuration();

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		this.logger = new ProcessorLogger(processingEnv.getMessager());
		logger.info("Running MainProcessor...");
		StormEnvironment stormEnv = new StormEnvironment(logger);	

		cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/res"));

//		for (TypeElement annotationType : annotations) {}

		for (Element element : roundEnv.getElementsAnnotatedWith(Converter.class)) {
			logger.info("processing " + element.getSimpleName());
			ConverterProcessor cproc = new ConverterProcessor(element, stormEnv);
			cproc.populateModel();
		}
		
		// First pass on @Database annotations to get all db names
		for (Element element : roundEnv.getElementsAnnotatedWith(Database.class)) {
			DatabaseProcessor dbProc = new DatabaseProcessor(element, stormEnv);
			dbProc.populateModel();
			stormEnv.addDatabase(dbProc);
		}
		
		for (Element element : roundEnv.getElementsAnnotatedWith(Entity.class)) {
			EntityProcessor eproc = new EntityProcessor(element, stormEnv);
			eproc.populateModel();
			processTemplate(processingEnv, cfg, eproc.getModel());
		}
		
		// Second pass to generate DatabaseFactory templates now that
		// all entities have been associated with a db
		for (DatabaseProcessor dbProc : stormEnv.getDbProcessors()) {
			processTemplate(processingEnv, cfg, dbProc.getModel());
		}
		
		return true;
	}

	void processTemplate(ProcessingEnvironment processingEnv, Configuration cfg, ClassModel model) {
		JavaFileObject file;
		try {
			file = processingEnv.getFiler().createSourceFile(
					model.getGeneratedClass());
			logger.info("Creating file " + file.getName());
			Writer out = file.openWriter();
			Template t = cfg.getTemplate(model.getTemplatePath());
			logger.info("Generating " + model.getClassName() + " with " + t.getName());
			t.process(model, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("Template error", e);
		}
	}

}