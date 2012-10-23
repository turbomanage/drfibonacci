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
package com.turbomanage.storm.apt.converter;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import com.turbomanage.storm.apt.ClassProcessor;
import com.turbomanage.storm.apt.StormEnvironment;

public class ConverterProcessor extends ClassProcessor {

	private ConverterModel cm;

	public ConverterProcessor(Element el, StormEnvironment stormEnv) {
		super(el, stormEnv);
	}

	@Override
	protected ConverterModel getModel() {
		return cm;
	}

	@Override
	// TODO verify presence of static GET field
	public void populateModel() {
		String converterClass = this.typeElement.getQualifiedName().toString();
		List<? extends AnnotationMirror> annoMirrors = this.typeElement
				.getAnnotationMirrors();
		for (AnnotationMirror anno : annoMirrors) {
			Map<? extends ExecutableElement, ? extends AnnotationValue> annoValues = anno
					.getElementValues();
			for (AnnotationValue val : annoValues.values()) {
				String[] types = val.accept(
						new ConverterTypeAnnotationValuesVisitor(),
						stormEnv.getLogger());
				for (String type : types) {
					if (TypeMapper.registerConverter(converterClass, type))
						stormEnv.getLogger()
								.info(converterClass + " registered for type "
										+ type);
					else
						stormEnv.getLogger()
								.error("Converter already registered for type "
										+ type, this.typeElement);
				}
			}
		}
	}

}
