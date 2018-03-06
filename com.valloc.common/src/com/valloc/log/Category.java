/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;

import com.valloc.CategoryType;

/**
 *
 *
 * @author wstevens
 */
public final class Category
{	
	private final CategoryType categoryType;
	private final LogManager logManager = LogManager.manager();
	private LogLevel logLevel = logManager.getLogLevel();
	private final Set<Logger> loggers = new HashSet<Logger>();
	private final Set<Handler> handlers = new HashSet<Handler>();
	private final Set<CustomHandler<?>> customHandlers = new HashSet<CustomHandler<?>>();
	private boolean isActive;
	
	
	/**
	 * 
	 */
	Category(final CategoryType categoryType)
	{
		this.categoryType = categoryType;
	}

	public void addLogger(final Logger logger)
	{
		if (!loggers.contains(logger)) {
			loggers.add(logger);
			logger.addToParentCategories(this);
			
			for (final Handler handler : handlers) {
				logger.addHandler(handler);
			}
			for (final CustomHandler<?> handler : customHandlers) {
				logger.addHandler(handler);
			}
			
			final LogLevel relevantLogLevel = isActive ? this.logLevel : logManager.getLogLevel();
			logger.setLogLevel(relevantLogLevel);
		}
	}
		
	/**
	 * @return the logLevel
	 */
	public LogLevel getLogLevel()
	{
		return logLevel;
	}

	/**
	 * @return the isActive
	 */
	boolean isActive()
	{
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	void setActive(final LogLevel logLevel)
	{
		isActive = true;		
		this.logLevel = logLevel;
		
		for (final Logger logger : loggers) {
			logger.setLogLevel(logLevel);
		}
	}

	void setInactive()
	{
		isActive = false;

		for (final Logger logger : loggers) {
			logger.setInactive();
		}
	}
	

	void addHandler(final Handler handler)
	{
		handlers.add(handler);
		for (final Logger logger : loggers) {
			logger.addHandler(handler);
		}
	}
	
	void addHandler(final CustomHandler<?> handler)
	{
		customHandlers.add(handler);
		for (final Logger logger : loggers) {
			logger.addHandler(handler);
		}
	}
	
	void removeHandler(final Handler handler)
	{
		handlers.remove(handler);
		for (final Logger logger : loggers) {
			logger.removeHandler(handler);
		}
	}
	
	void removeHandler(final CustomHandler<?> handler)
	{
		customHandlers.remove(handler);
		for (final Logger logger : loggers) {
			logger.removeHandler(handler);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryType == null) ? 0 : categoryType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Category)) {
			return false;
		}
		final Category other = (Category) obj;
		if (categoryType == null) {
			if (other.categoryType != null) {
				return false;
			}
		} else if (!categoryType.equals(other.categoryType)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return categoryType.id();
	}
}
