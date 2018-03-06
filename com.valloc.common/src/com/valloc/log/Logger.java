/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import java.util.logging.Handler;

import com.valloc.CategoryType;

/**
 * 
 *
 *
 * @author wstevens
 */
public interface Logger
{
	public void error(String message, Throwable throwable);
	public void error(String message, Throwable throwable, Object... args);
	public void warn(String message);
	public void warn(String message, Object... args);
	public void info(String message);
	public void info(String message, Object... args);
	public void fine(String message);
	public void fine(String message, Object... args);
	public void finer(String message);
	public void finer(String message, Object... args);
	public void finest(String message);
	public void finest(String message, Object... args);

	public boolean isFine();
	public boolean isFiner();
	public boolean isFinest();
	
	public void registerCategory(CategoryType categoryType);
	public void addToParentCategories(Category category);
	
	String getName();
	void setLogLevel(LogLevel logLevel);
	void setInactive();	
		
	Handler[] getHandlers();
	void addHandler(Handler handler);
	void addHandler(CustomHandler<?> handler);
	void removeHandler(Handler handler);
	void removeHandler(CustomHandler<?> handler);
}
