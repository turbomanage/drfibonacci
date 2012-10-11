package com.example.storm.entity;

import com.example.storm.api.Entity;
import com.example.storm.api.Persistable;

@Entity
public class Person implements Persistable {

	private long id;
	private String firstName, lastName;
	
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
