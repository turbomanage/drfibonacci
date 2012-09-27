package com.example.storm;

public class TooManyResultsException extends RuntimeException {

	public TooManyResultsException(String detailMessage) {
		super(detailMessage);
	}

	public TooManyResultsException(Throwable throwable) {
		super(throwable);
	}

	public TooManyResultsException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
