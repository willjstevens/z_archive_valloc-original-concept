/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.Date;

import com.valloc.interrupt.InterruptType;

/**
 *
 *
 * @author wstevens
 */
public class ContainerExecutionResult
{
	private final Date startTime;
	private final Date lastCompletedTime;
	private final InterruptType interruptType;
	private final Throwable throwable;
	
	/**
	 * 
	 * @param startTime
	 * @param lastCompletedTime
	 * @param interruptType
	 * @param throwable
	 */
	ContainerExecutionResult(final Date startTime, final Date lastCompletedTime, final InterruptType interruptType, final Throwable throwable)
	{
		this.startTime = startTime;
		this.lastCompletedTime = lastCompletedTime;
		this.interruptType = interruptType;
		this.throwable = throwable;
	}
	
	/**
	 * @return the startTime
	 */
	public Date getStartTime()
	{
		return startTime;
	}

	/**
	 * @return the lastCompletedTime
	 */
	public Date getLastCompletedTime()
	{
		return lastCompletedTime;
	}

	/**
	 * @return the interruptType
	 */
	public InterruptType getCancelType()
	{
		return interruptType;
	}
	
	/**
	 * @return the throwable
	 */
	public Throwable getThrowable()
	{
		return throwable;
	}
	
	public boolean wasSuccess()
	{
		return interruptType == null && throwable == null;
	}
	
	public boolean wasCancel()
	{
		return interruptType != null;
	}
	
	public boolean wasError()
	{
		return throwable != null;
	}
}

