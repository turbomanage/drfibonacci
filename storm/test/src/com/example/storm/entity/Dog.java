package com.example.storm.entity;

import java.util.Date;

import com.example.storm.api.Entity;
import com.example.storm.api.Persistable;

@Entity
public class Dog implements Persistable {

	long id;
	String name;
	Date birthday;
	
	public Dog() {
		// TODO Auto-generated constructor stub
	}
	
	public Dog(String name, Date bday) {
		this.name = name;
		this.birthday = bday;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
}
