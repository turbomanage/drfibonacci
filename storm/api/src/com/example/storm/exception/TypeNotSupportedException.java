package com.example.storm.exception;

public class TypeNotSupportedException extends RuntimeException {

	public TypeNotSupportedException(String arg0) {
		super(arg0);
	}

	public TypeNotSupportedException(Throwable arg0) {
		super(arg0);
	}

	public TypeNotSupportedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
