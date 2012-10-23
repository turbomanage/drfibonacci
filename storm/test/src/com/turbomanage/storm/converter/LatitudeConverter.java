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
package com.turbomanage.storm.converter;

import com.turbomanage.storm.api.Converter;
import com.turbomanage.storm.types.DoubleConverter;
import com.turbomanage.storm.types.TypeConverter;

@Converter(forTypes = { Latitude.class })
public class LatitudeConverter extends TypeConverter<Latitude, Double> {

	@Override
	public Double toSql(Latitude javaValue) {
		return javaValue.getDegLat();
	}

	@Override
	public Latitude fromSql(Double sqlValue) {
		return new Latitude(sqlValue);
	}

	@Override
	public Double fromString(String strValue) {
		return DoubleConverter.GET.fromString(strValue);
	}

	@Override
	public String toString(Double sqlValue) {
		return DoubleConverter.GET.toString(sqlValue);
	}

	@Override
	public com.turbomanage.storm.types.TypeConverter.SqlType getSqlType() {
		return SqlType.REAL;
	}

	@Override
	public com.turbomanage.storm.types.TypeConverter.BindType getBindType() {
		return BindType.DOUBLE;
	}

}
