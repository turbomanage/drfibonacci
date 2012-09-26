package com.example.storm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public abstract class ClassModel {

	protected List<String> imports = new ArrayList<String>();
	protected List<Field> fields = new ArrayList<Field>();
	protected String packageName = "com.example.storm.dao";
	protected String className;
	protected TypeElement typeElement;

	public ClassModel(Element element) {
		TypeElement typeElement = (TypeElement) element;
		this.typeElement = typeElement;
		readFields(typeElement);
	}
	
	/**
	 * Subclasses override to provide the path to the associated template. 
	 * 
	 * @return Path to the template
	 */
	public abstract String getTemplatePath();

	protected void readFields(TypeElement type) {
		// Read fields from superclass if any
		TypeMirror superClass = type.getSuperclass();
		if (TypeKind.DECLARED.equals(superClass.getKind())) {
			DeclaredType superType = (DeclaredType) superClass;
			readFields((TypeElement) superType.asElement());
		}
		for (Element child : type.getEnclosedElements()) {
				if (child.getKind() == ElementKind.FIELD) {
					VariableElement field = (VariableElement) child;
					Set<Modifier> modifiers = field.getModifiers();
					if (!modifiers.contains(Modifier.TRANSIENT) && !modifiers.contains(Modifier.PRIVATE)) {
						String javaType = getFieldType(field);
						addField(field.getSimpleName().toString(), javaType);
					}
				}
		}
	}

	protected String getFieldType(VariableElement field) {
		TypeMirror fieldType = field.asType();
		return fieldType.toString();
	}

	public List<String> getImports() {
		return imports;
	}

	public List<Field> getFields() {
		return fields;
	}

	protected void addField(String fieldName, String javaType) {
		fields.add(new Field(fieldName, javaType));
	}

	protected void addImport(String importPath) {
		imports.add(importPath);
	}

	/**
	 * @return Name of the generated class
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return Package name of the generated class
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * Returns the fully qualified class name of the generated class.
	 * 
	 * @return Name of generated class with package prepended
	 */
	public String getQualifiedClassName() {
		return this.packageName + "." + this.className;
	}

	public Element getElement() {
		return typeElement;
	}

}