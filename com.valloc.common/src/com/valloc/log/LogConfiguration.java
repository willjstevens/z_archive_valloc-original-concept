/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import java.net.URL;

import com.valloc.Constants;
import com.valloc.config.Configuration;

/**
 *
 *
 * @author wstevens
 */
public final class LogConfiguration implements Configuration
{
	private final static int DEFAULT_FILE_SIZE 					= 2 << 19; // > 1MB
	private final static int DEFAULT_FILE_GENERATION_COUNT		= 5;
	private final static boolean DEFAULT_APPEND_POLICY			= true;
	private final static LogLevel DEFAULT_LOG_LEVEL				= LogLevel.ALL;
	
	public final static String DEFAULT_HANDLER_ENCODING		= Constants.UTF_16BE;
		
	private int fileSize 				= DEFAULT_FILE_SIZE;
	private int fileGenerationCount 	= DEFAULT_FILE_GENERATION_COUNT;
	private boolean fileAppendPolicy 	= DEFAULT_APPEND_POLICY;
	private String consoleEncoding 		= DEFAULT_HANDLER_ENCODING;	
	private String fileEncoding 		= DEFAULT_HANDLER_ENCODING;
	private LogLevel logLevel 			= DEFAULT_LOG_LEVEL;
	private URL parentDirectoryUrl; 
	private boolean isConfigSet;
	
	/**
	 * @return the logLevel
	 */
	public LogLevel getLogLevel()
	{
		return logLevel;
	}

	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(final String logLevelStr)
	{
		this.logLevel = LogLevel.toLogLevel(logLevelStr);
	}

	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(final LogLevel logLevel)
	{
		this.logLevel = logLevel;
	}
	
	/**
	 * @return the consoleEncoding
	 */
	public String getConsoleEncoding()
	{
		return consoleEncoding;
	}

	/**
	 * @param consoleEncoding the consoleEncoding to set
	 */
	public void setConsoleEncoding(final String consoleEncoding)
	{
		this.consoleEncoding = consoleEncoding;
	}

	/**
	 * @return the fileEncoding
	 */
	public String getFileEncoding()
	{
		return fileEncoding;
	}

	/**
	 * @param fileEncoding the fileEncoding to set
	 */
	public void setFileEncoding(final String fileEncoding)
	{
		this.fileEncoding = fileEncoding;
	}

	/**
	 * @return the parentDirectoryUrl
	 */
	public URL getParentDirectoryUrl()
	{
		return parentDirectoryUrl;
	}
	
	/**
	 * @param parentDirectoryUrl the parentDirectoryUrl to set
	 */
	public void setParentDirectoryUrl(final URL logDirectoryUrl)
	{
		this.parentDirectoryUrl = logDirectoryUrl;
	}
	
	/**
	 * @return the fileSize
	 */
	public int getFileSize()
	{
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(final int fileSize)
	{
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileGenerationCount
	 */
	public int getFileGenerationCount()
	{
		return fileGenerationCount;
	}

	/**
	 * @param fileGenerationCount the fileGenerationCount to set
	 */
	public void setFileGenerationCount(final int fileGenerationCount)
	{
		this.fileGenerationCount = fileGenerationCount;
	}

	/**
	 * @return the fileAppendPolicy
	 */
	public boolean doAppend()
	{
		return fileAppendPolicy;
	}

	/**
	 * @param fileAppendPolicy the fileAppendPolicy to set
	 */
	public void setFileAppendPolicy(final boolean fileAppendPolicy)
	{
		this.fileAppendPolicy = fileAppendPolicy;
	}

	/**
	 * @return the isConfigSet
	 */
	public boolean isConfigSet()
	{
		return isConfigSet;
	}

	/**
	 * @param isConfigSet the isConfigSet to set
	 */
	public void setIsConfigSet(final boolean isConfigSet)
	{
		this.isConfigSet = isConfigSet;
	}
}
