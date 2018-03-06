/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.Date;

import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class RejectSummary
{
	private final UniqueId requestId;
	private final Date rejectTimestamp;
	
	/**
	 * @param requestId
	 * @param rejectTimestamp
	 */
	RejectSummary(final UniqueId requestId, final Date rejectTimestamp)
	{
		this.requestId = requestId;
		this.rejectTimestamp = rejectTimestamp;
	}

	/**
	 * @return the requestId
	 */
	public UniqueId getRequestId()
	{
		return requestId;
	}

	/**
	 * @return the rejectTimestamp
	 */
	public Date getRejectTimestamp()
	{
		return rejectTimestamp;
	}
}
