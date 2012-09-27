package com.example.storm;

@Entity
public class TestEntity extends ModelBase {
	
	private int privateField;
	transient int transientField;
	Integer intField;
	String stringField;
	
	public TestEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public TestEntity(int intValue, String strValue) {
		this.intField = intValue;
		this.stringField = strValue;
	}
	
	public int getPrivateField() {
		return privateField;
	}
	public void setPrivateField(int privateField) {
		this.privateField = privateField;
	}
	public int getTransientField() {
		return transientField;
	}
	public void setTransientField(int transientField) {
		this.transientField = transientField;
	}
	public Integer getIntField() {
		return intField;
	}
	public void setIntField(Integer intField) {
		this.intField = intField;
	}
	public String getStringField() {
		return stringField;
	}
	public void setStringField(String stringField) {
		this.stringField = stringField;
	}
	
}
