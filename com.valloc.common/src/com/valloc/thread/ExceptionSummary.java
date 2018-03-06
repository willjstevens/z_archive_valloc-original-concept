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
public final class ExceptionSummary
{
	private final long tid;
	private final String threadName;
	private final String message;
	private final String toString;
	private final Date caughtTimestamp;

	/**
	 * @param tid
	 * @param threadName
	 * @param message
	 * @param toString
	 * @param caughtTimestamp
	 */
	ExceptionSummary(final long tid, final String threadName, final String message, final String toString, final Date caughtTimestamp)
	{
		this.tid = tid;
		this.threadName = threadName;
		this.message = message;
		this.toString = toString;
		this.caughtTimestamp = caughtTimestamp;
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
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @return the toString
	 */
	public String getToString()
	{
		return toString;
	}

	/**
	 * @return the caughtTimestamp
	 */
	public Date getCaughtTimestamp()
	{
		return caughtTimestamp;
	}
}
