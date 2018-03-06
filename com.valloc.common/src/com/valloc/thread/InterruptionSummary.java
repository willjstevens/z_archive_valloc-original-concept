/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.thread;

import java.util.Date;

/**
 *
 *
 * @author wstevens
 */
public final class InterruptionSummary
{
	private final long tid;
	private final String threadName;
	private final Date interruptTimestamp;
	
	/**
	 * @param tid
	 * @param threadName
	 * @param interruptTimestamp
	 */
	InterruptionSummary(final long tid, final String threadName, final Date interruptTimestamp)
	{
		this.tid = tid;
		this.threadName = threadName;
		this.interruptTimestamp = interruptTimestamp;
	}

	/**
	 * @return the tid
	 */
	public long getTid()
	{
		return tid;
	}

	/**
	 * @return the threadName
	 */
	public String getThreadName()
	{
		return threadName;
	}

	/**
	 * @return the interruptTimestamp
	 */
	public Date getInterruptTimestamp()
	{
		return interruptTimestamp;
	}
}
