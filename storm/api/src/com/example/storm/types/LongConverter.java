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
package com.example.storm.types;

import com.example.storm.api.Converter;

@Converter(forTypes = { long.class, Long.class })
public class LongConverter extends TypeConverter<Long,Long> {

	public static final LongConverter GET = new LongConverter();
	
	@Override
	public SqlType getSqlType() {
		return SqlType.INTEGER;
	}

	@Override
	public BindType getBindType() {
		return BindType.LONG;
	}

	@Override
	public Long toSql(Long javaValue) {
		return javaValue;
	}

	@Override
	public Long fromSql(Long sqlValue) {
		return sqlValue;
	}

	@Override
	public Long fromString(String strValue) {
		return Long.valueOf(strValue);
	}

}
