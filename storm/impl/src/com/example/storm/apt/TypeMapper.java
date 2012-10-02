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
import com.example.storm.types.java.DateConverter;
import com.example.storm.types.java.DoubleConverter;
import com.example.storm.types.java.FloatConverter;
import com.example.storm.types.java.IntegerConverter;
import com.example.storm.types.java.LongConverter;
import com.example.storm.types.java.ShortConverter;
import com.example.storm.types.java.StringConverter;
import com.example.storm.types.java.TypeConverter;

/**
 * @author drfibonacci
 *
 */
public class TypeMapper {

	private static Map<String, String> map = new HashMap<String, String>();

	static {
		register(new BlobConverter());
		register(new BooleanConverter());
		register(new ByteConverter());
		register(new CharConverter());
		register(new DateConverter());
		register(new DoubleConverter());
		register(new FloatConverter());
		register(new IntegerConverter());
		register(new LongConverter());
		register(new ShortConverter());
		register(new StringConverter());
	}
	
	/**
	 * For testing only. Run this class to see a list of all built-in
	 * converters.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		dumpConverters();
	}

	public static String getSqlType(String javaType) throws TypeNotSupportedException {
		TypeConverter converter = getConverter(javaType);
		if (converter != null) {
			return converter.getSqlType().name();
		}
		throw new TypeNotSupportedException("Could not find converter for type " + javaType);
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
	public static TypeConverter getConverter(String javaType) throws TypeNotSupportedException {
		String cType = map.get(javaType);
		if (cType == null) {
			throw new TypeNotSupportedException("Fields of type " + javaType + " are not supported. Consider adding your own TypeConverter.");
		}
		TypeConverter converter;
		try {
			
			converter = (TypeConverter) Class.forName(cType).newInstance();
			if (converter != null) {
				return converter;
			}
		} catch (InstantiationException e) {
			throw new TypeNotSupportedException("Could not instantiate " + cType + " due to " + e.getClass().getCanonicalName());
		} catch (IllegalAccessException e) {
			throw new TypeNotSupportedException("Could not instantiate " + cType + " due to " + e.getClass().getCanonicalName());
		} catch (ClassNotFoundException e) {
			throw new TypeNotSupportedException("Could not instantiate " + cType + " due to " + e.getClass().getCanonicalName());
		}
		throw new TypeNotSupportedException("Fields of type " + javaType + " are not supported. Consider adding your own TypeConverter.");
	}
	
	public static void dumpConverters() {
		for (String type : map.keySet()) {
			System.out.println(String.format("%s for %s", type, map.get(type)));
		}
	}
}
