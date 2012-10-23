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
import com.example.storm.apt.converter.ConverterProcessor;
import com.example.storm.apt.database.DatabaseFactoryTemplate;
import com.example.storm.apt.database.DatabaseModel;
import com.example.storm.apt.database.DatabaseProcessor;
import com.example.storm.apt.entity.EntityDaoTemplate;
import com.example.storm.apt.entity.EntityProcessor;
import com.example.storm.apt.entity.TableHelperTemplate;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@SupportedAnnotationTypes({ "com.example.storm.api.Database","com.example.storm.api.Entity","com.example.storm.api.Converter" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MainProcessor extends AbstractProcessor {
	private ProcessorLogger logger;
	private Configuration cfg = new Configuration();

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		
		cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/res"));
		this.logger = new ProcessorLogger(processingEnv.getMessager());
		logger.info("Running MainProcessor...");
		
		// Exit early if no annotations in this round so we don't overwrite the
		// env file
		if (annotations.size() < 1) {
			return true;
		}
		
		for (TypeElement annotationType : annotations) {
			logger.info("Processing elements with @" + annotationType.getQualifiedName());
		}

		StormEnvironment stormEnv = new StormEnvironment(logger);	
		// Read in previously processed classes to support incremental compilation
		stormEnv.readIndex(processingEnv.getFiler());

		for (Element element : roundEnv.getElementsAnnotatedWith(Converter.class)) {
			try {
				ConverterProcessor cproc = new ConverterProcessor(element, stormEnv);
				cproc.populateModel();
			} catch (Exception e) {
				logger.error("stORM error", e, element);
				return true;
			}
		}
		
		// First pass on @Database annotations to get all db names
		for (Element element : roundEnv.getElementsAnnotatedWith(Database.class)) {
			try {
				DatabaseProcessor dbProc = new DatabaseProcessor(element, stormEnv);
				dbProc.populateModel();
				stormEnv.addDatabase(dbProc.getModel());
			} catch (Exception e) {
				logger.error("stORM error", e, element);
				return true;
			}
		}
		
		for (Element element : roundEnv.getElementsAnnotatedWith(Entity.class)) {
			try {
				EntityProcessor eproc = new EntityProcessor(element, stormEnv);
				eproc.populateModel();
				// Generate EntityDao
				EntityDaoTemplate daoTemplate = new EntityDaoTemplate(eproc.getModel());
				processTemplate(processingEnv, cfg, daoTemplate);
				// Generate EntityTable
				TableHelperTemplate tableHelperTemplate = new TableHelperTemplate(eproc.getModel());
				processTemplate(processingEnv, cfg, tableHelperTemplate);
			} catch (Exception e) {
				logger.error("stORM error", e, element);
				return true;
			}
		}
		
		// Second pass to generate DatabaseFactory templates now that
		// all entities have been associated with a db
		for (DatabaseModel dbModel : stormEnv.getDbModels()) {
			DatabaseFactoryTemplate dbFactoryTemplate = new DatabaseFactoryTemplate(dbModel);
			processTemplate(processingEnv, cfg, dbFactoryTemplate);
		}
		
		// Write all processed dbs to index to support incremental compilation
		stormEnv.writeIndex(processingEnv.getFiler());
		
		return true;
	}

	void processTemplate(ProcessingEnvironment processingEnv, Configuration cfg, ClassTemplate template) {
		JavaFileObject file;
		try {
			file = processingEnv.getFiler().createSourceFile(
					template.getGeneratedClass());
			Writer out = file.openWriter();
			Template t = cfg.getTemplate(template.getTemplatePath());
			logger.info("Generating " + file.getName() + " with " + t.getName());
			t.process(template.getModel(), out);
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("Template error", e);
		}
	}

}