package com.example.storm.apt.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.example.storm.api.Converter;
import com.example.storm.apt.StormEnvironment;
import com.example.storm.exception.TypeNotSupportedException;
import com.example.storm.types.BlobConverter;
import com.example.storm.types.BooleanConverter;
import com.example.storm.types.ByteConverter;
import com.example.storm.types.CharConverter;
import com.example.storm.types.DateConverter;
import com.example.storm.types.DoubleConverter;
import com.example.storm.types.FloatConverter;
import com.example.storm.types.IntegerConverter;
import com.example.storm.types.LongConverter;
import com.example.storm.types.ShortConverter;
import com.example.storm.types.StringConverter;
import com.example.storm.types.TypeConverter;

/**
 * @author drfibonacci
 *
 */
public class TypeMapper {

	// Fully qualified field type name, fully qualified TypeConverter
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
			map.put(clazz.getCanonicalName(), converter.getClass().getCanonicalName());
		}
	}
	
	/**
	 * Register a custom {@link TypeConverter} for a given data (field) type.
	 * This method is called at compile time by the annotation processor.
	 * In order for the TypeConverter to be visible, it must be in a jar
	 * on the client project's annotation factory classpath.
	 * 
	 * @param String converterClass Fully-qualified classname
	 * @param String type Fully-qualified classname
	 */
	public static boolean registerConverter(String converterClass, String type) {
		if (map.containsKey(type) && !map.get(type).equals(converterClass))
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

	public static void writeToIndex(PrintWriter out) {
		out.println(StormEnvironment.BEGIN_CONVERTERS);
		for (String forType : map.keySet()) {
			String converterClass = map.get(forType); 
			out.format("%s,%s\n", forType, converterClass);
		}
		out.println(StormEnvironment.END_CONVERTERS);
	}

	public static void readFromIndex(BufferedReader reader) throws IOException {
		// Clear the built-in definitions since they'll be read from file
//		map.clear();
		String line = reader.readLine();
		while (line != null && !line.equals(StormEnvironment.END_CONVERTERS)) {
			String[] converterMapping = line.split(",");
			registerConverter(converterMapping[0], converterMapping[1]);
		}
	}
	
}
