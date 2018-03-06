/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.Date;

import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class ThrowableSummary
{
	private final UniqueId requestId;
	private final String message;
	private final String toString;
	private final Date caughtTimestamp;

	/**
	 *
	 * @param message
	 * @param toString
	 * @param caughtTimestamp
	 */
	ThrowableSummary(final UniqueId requestId, final String message, final String toString, final Date caughtTimestamp)
	{
		this.requestId = requestId;
		this.message = message;
		this.toString = toString;
		this.caughtTimestamp = caughtTimestamp;
	}

	/**
	 * @return the requestId
	 */
	public UniqueId getRequestId()
	{
		return requestId;
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
