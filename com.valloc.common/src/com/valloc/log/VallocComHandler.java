/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.valloc.util.Factory;

/**
 *
 *
 * @author wstevens
 */
public class VallocComHandler extends Handler implements Factory<VallocComHandler>, CustomHandler<VallocComHandler>
{
	private final LogManager logManager = LogManager.manager();
	@SuppressWarnings("unused")
	private LogConfiguration logConfiguration;
	private VallocComHandler() {}
	
	@Override
	public VallocComHandler newInstance()
	{
		final VallocComHandler retval = new VallocComHandler();
		
		setErrorManager(new ErrorManager(this));
		setLevel(logManager.getLogLevel().level());
		
		return retval;
	}

	@Override
	public void publish(final LogRecord record)
	{
	}

	@Override
	public void flush()
	{
	}
	
	@Override
	public void close() throws SecurityException
	{
	}

	@Override
	public Handler toHandler()
	{
		return this;
	}
	
	/**
	 * @param logConfiguration the logConfiguration to set
	 */
	@Override
	public void setLogConfiguration(final LogConfiguration logConfiguration)
	{
		this.logConfiguration = logConfiguration;
	}
}
