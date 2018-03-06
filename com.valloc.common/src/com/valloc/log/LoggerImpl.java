/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import static com.valloc.log.LogLevel.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;

import com.valloc.CategoryType;
import com.valloc.Constants;

/**
 *
 *
 * @author wstevens
 */
final class LoggerImpl implements Logger
{
	private final static String EMPTY_SOURCE_METHOD = Constants.EMPTY_STRING;
	private final static LogLevel MINIMUM_LOG_LEVEL = LogLevel.INFO;
	private final LogManager logManager = LogManager.manager();
	private final java.util.logging.Logger _logger;
	@SuppressWarnings("unused")
	private final Class<?> clazz;
	private final String name;
	private LogLevel logLevel;
	private Level _level;
	private final Set<Category> categoryParents = new HashSet<Category>();
	
	/**
	 * 
	 */
	LoggerImpl(final Class<?> clazz)
	{		
		this.clazz = clazz;		
		this.name = LogManager.classToName(clazz);
		// this also registers with internal jdk log manager
		_logger = java.util.logging.Logger.getLogger(name); 
		_logger.setUseParentHandlers(false); // otherwise it pumps out the default SimpleFormatter line from parent ConsoleHandler
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#error(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void error(final String message, final Throwable throwable)
	{
		_logger.logp(ERROR.level(), name, EMPTY_SOURCE_METHOD, message, throwable);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#error(java.lang.String, java.lang.Throwable, java.lang.Object[])
	 */
	@Override
	public void error(final String message, final Throwable throwable, final Object... args)
	{
		error(String.format(message, args), throwable);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#warning(java.lang.String)
	 */
	@Override
	public void warn(final String message)
	{
		_logger.logp(WARN.level(), name, EMPTY_SOURCE_METHOD, message);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#warning(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void warn(final String message, final Object... args)
	{
		warn(String.format(message, args));
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#info(java.lang.String)
	 */
	@Override
	public void info(final String message)
	{
		_logger.logp(INFO.level(), name, EMPTY_SOURCE_METHOD, message);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#info(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void info(final String message, final Object... args)
	{
		info(String.format(message, args));
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#fine(java.lang.String)
	 */
	@Override
	public void fine(final String message)
	{
		_logger.logp(FINE.level(), name, EMPTY_SOURCE_METHOD, message);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#fine(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void fine(final String message, final Object... args)
	{
		fine(String.format(message, args));
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#finer(java.lang.String)
	 */
	@Override
	public void finer(final String message)
	{
		_logger.logp(FINER.level(), name, EMPTY_SOURCE_METHOD, message);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#finer(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void finer(final String message, final Object... args)
	{
		finer(String.format(message, args));
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#finest(java.lang.String)
	 */
	@Override
	public void finest(final String message)
	{
		_logger.logp(FINEST.level(), name, EMPTY_SOURCE_METHOD, message);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#finest(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void finest(final String message, final Object... args)
	{
		finest(String.format(message, args));
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#isfine()
	 */
	@Override
	public boolean isFine()
	{
		return isLevelEnabled(FINE);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#isfiner()
	 */
	@Override
	public boolean isFiner()
	{
		return isLevelEnabled(FINER);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#isfinest()
	 */
	@Override
	public boolean isFinest()
	{
		return isLevelEnabled(FINEST);
	}

	/*
	 * 
	 * @param desiredLogLevel
	 * @return
	 */
	private boolean isLevelEnabled(final LogLevel desiredLogLevel)
	{
		return logLevel.intLevel() <= desiredLogLevel.intLevel();
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#registerCategory(com.valloc.log.CategoryType)
	 */
	@Override
	public void registerCategory(final CategoryType categoryType)
	{
		logManager.addLoggerToCategory(this, categoryType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.valloc.log.Logger#addToParentCategory(com.valloc.log.Category)
	 */
	@Override
	public void addToParentCategories(final Category category)
	{
		if (!categoryParents.contains(category)) {
			categoryParents.add(category);
			category.addLogger(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#getHandlers()
	 */
	@Override
	public Handler[] getHandlers()
	{
		return _logger.getHandlers();
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#addHandler(java.util.logging.Handler)
	 */
	@Override
	public void addHandler(final Handler handler)
	{
		boolean alreadyPresent = false;
		for (final Handler candidateMatch : getHandlers()) {
			// object reference comparison should work as only a single handler of a given type will
			//		exist in memory for comparison:
			if (handler == candidateMatch) {  
				alreadyPresent = true;
				break;
			}
		}
		if (!alreadyPresent) {
			_logger.addHandler(handler);
		}
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#addHandler(java.util.logging.Handler)
	 */
	@Override
	public void addHandler(final CustomHandler<?> handler)
	{
		_logger.addHandler(handler.toHandler()); 
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.valloc.log.Logger#removeHandler(com.valloc.log.CustomHandler)
	 */
	@Override
	public void removeHandler(final CustomHandler<?> handler)
	{
		_logger.removeHandler(handler.toHandler());
	}
	
	
	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#removeHandler(java.util.logging.Handler)
	 */
	@Override
	public void removeHandler(final Handler handler)
	{
		_logger.removeHandler(handler);
	}

	/* (non-Javadoc)
	 * @see com.valloc.log.Logger#getName()
	 */
	@Override
	public String getName()
	{
		return _logger.getName();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.valloc.log.Logger#setLogLevel(com.valloc.log.LogLevel)
	 */
	@Override
	public void setLogLevel(final LogLevel logLevel)
	{
		this.logLevel = logLevel;
		_level = logLevel.level();
		_logger.setLevel(_level);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.valloc.log.Logger#setInactive()
	 */
	@Override
	public void setInactive()
	{
		boolean hasActiveParentCategory = false;
		for (final Category category : categoryParents) {
			if (category.isActive()) {
				hasActiveParentCategory = true;
				break;
			}
		}
		
		// only set if this logger is still involved in some other active category 
		if (!hasActiveParentCategory) { 
			setLogLevel(MINIMUM_LOG_LEVEL);
		} // else leave level for active category parent
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof LoggerImpl)) {
			return false;
		}
		final LoggerImpl other = (LoggerImpl) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
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
		return name;
	}
}
