/*******************************************************************************
 * Copyright 2012 Google, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.turbomanage.storm.apt.entity;

import com.turbomanage.storm.apt.converter.TypeMapper;
import com.turbomanage.storm.exception.TypeNotSupportedException;
import com.turbomanage.storm.types.TypeConverter;

/**
 * Model of a persisted field
 *
 * @author David M. Chandler
 */
public class FieldModel {

	private String fieldName, colName, javaType;

	public FieldModel(String fieldName, String javaType) {
		this.fieldName = fieldName;
		this.javaType = javaType;
		// TODO Use @Id or @ColumnName annotation instead
		if ("id".equals(fieldName)) {
			this.colName = "_id";
		} else {
			this.colName = fieldName;
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getColName() {
		return colName.toUpperCase();
	}

	public String getJavaType() {
		return javaType;
	}

	private TypeConverter getConverter() {
		return TypeMapper.getConverter(javaType);
	}

	/**
	 * Convenience method for more compact Dao templates. Returns the name of
	 * this field's converter class sans package name.
	 *
	 * @return
	 */
	public String getConverterName() {
		return getConverter().getClass().getSimpleName();
	}

	/**
	 * Fully-qualified name of the converter class for this field.
	 *
	 * @return String classname
	 */
	public String getQualifiedConverterClass() {
		return getConverter().getClass().getName();
	}

	/**
	 * Morph bind type like INT ==> Int so it can be used in a Cursor getXxx
	 * method name. Never called at runtime.
	 *
	 * @return
	 */
	public String getBindType() {
		String bindType = getConverter().getBindType().name();
		return bindType.charAt(0) + bindType.toLowerCase().substring(1);
	}

	public String getSqlType() {
		// TODO Hack. Add @Id annotation instead.
		if ("id".equals(fieldName))
			return "INTEGER PRIMARY KEY AUTOINCREMENT";
		try {
			return TypeMapper.getSqlType(javaType);
		} catch (TypeNotSupportedException e) {
			// swallow as its already been reported
			return null;
		}
	}

	public String getSetter() {
		return "set" + capFirst(fieldName);
	}

	public String getGetter() {
		if ("boolean".equals(javaType))
			return "is" + capFirst(fieldName);
		else
			return "get" + capFirst(fieldName);
	}

	/**
	 * Capitalizes the first letter to create a valid getter/setter name.
	 *
	 * @param String
	 * @return String
	 */
	private String capFirst(String anyName) {
		// obscure Java convention:
		// if second letter capitalized, leave it alone
		if (anyName.length() > 1)
			if (anyName.charAt(1) >= 'A' && anyName.charAt(1) <= 'Z')
				return anyName;
		String capFirstLetter = anyName.substring(0, 1).toUpperCase();
		return capFirstLetter + anyName.substring(1);
	}

	public boolean isNullable() {
		return javaType.contains(".") || javaType.contains("[]");
	}

}
