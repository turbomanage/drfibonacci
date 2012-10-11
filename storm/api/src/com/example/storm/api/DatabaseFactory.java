package com.example.storm.api;

import com.example.storm.TableHelper;

public interface DatabaseFactory {
	
	String getName();
	int getVersion();
	TableHelper[] getTableHelpers();
	
}
