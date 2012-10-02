package com.example.storm.apt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;

import com.example.storm.api.Converter;
import com.example.storm.exception.TypeNotSupportedException;
import com.example.storm.types.java.BlobConverter;
import com.example.storm.types.java.BooleanConverter;
import com.example.storm.types.java.ByteConverter;
import com.example.storm.types.java.CharConverter;
import com.example.storm.types.java.DoubleConverter;
import com.example.storm.types.java.FloatConverter;
import com.example.storm.types.java.IntegerConverter;
import com.example.storm.types.java.LongConverter;
import com.example.storm.types.java.ShortConverter;
import com.example.storm.types.java.StringConverter;
import com.example.storm.types.java.TypeConverter;

public class TypeMapper {

	private static Map<String, String> map = new HashMap<String, String>();

	static {
		register(new BlobConverter());
		register(new BooleanConverter());
		register(new ByteConverter());
		register(new CharConverter());
		register(new DoubleConverter());
		register(new FloatConverter());
		register(new IntegerConverter());
		register(new LongConverter());
		register(new ShortConverter());
		register(new StringConverter());
	}
	
	public static void main(String[] args) {	}

	public static String getSqlType(String javaType) throws TypeNotSupportedException {
		return getConverter(javaType).getSqlType().name();
	}

	/**
	 * Register a built-in {@link TypeConverter}. Converters that are part of the API
	 * jar must use normal reflection to access the annotation values. 
	 * 
	 * @param converter
	 */
	private static void register(TypeConverter converter) {
		Converter annotation = converter.getClass().getAnnotation(Converter.class);
		Class[] forTypes = annotation.forTypes();
		for (Class clazz : forTypes) {
			map.put(clazz.getCanonicalName(), converter.getClass().getName());
			System.out.println("registered " + converter.getClass().getCanonicalName() + " for " + clazz.getCanonicalName());
		}
	}
	
	/**
	 * Register a custom {@link TypeConverter}.
	 * 
	 * @param cm
	 * @param logger
	 * @param typeElement
	 */
//	static void registerConverter(ConverterModel cm, ProcessorLogger logger, TypeElement typeElement) {
//		for (String type : cm.getConvertForTypes()) {
//			if (cm != null) {
//				if (map.containsKey(type)) 
//					logger.error("Converter already registered for type " + type, typeElement);
//				else {
//					map.put(type, cm.getQualifiedClassName());
//					logger.info("registered " + cm.getClassName() + " for " + type);
//				}
//			}
//		}
//	}
	static boolean registerConverter(String converterClass, String type) {
		if (map.containsKey(type))
			return false;
		map.put(type, converterClass);
		return true;
	}

	/**
	 * Obtain an instead of a {@link TypeConverter} for the given Java type.
	 * The instance is used only to obtain the cursor method name and SQL type
	 * associated with the TypeConverter on behalf of templates. These are
	 * instance methods so the TypeConverter interface can enforce type safety;
	 * however, they could be made static in which case this method would simply
	 * return a String class name instead of an instance.
	 * 
	 * @param javaType
	 * @return
	 */
	public static TypeConverter getConverter(String javaType) {
		String cType = map.get(javaType);
		TypeConverter converter;
		try {
			converter = (TypeConverter) Class.forName(cType).newInstance();
			if (converter != null) {
				return converter;
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new TypeNotSupportedException("Fields of type " + javaType + " are not supported.");
	}
	
	public static void dumpConverters(ProcessorLogger logger) {
		for (String type : map.keySet()) {
			logger.info(String.format("%s for %s", type, map.get(type)));
		}
	}
}
