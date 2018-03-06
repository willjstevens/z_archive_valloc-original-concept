/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.Date;

import com.valloc.Priority;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class ProcessingRequestSummary
{
	private final UniqueId id;
	private final Priority priority;
	private final Date significantTimestamp;
	
	/**
	 * @param id
	 * @param priority
	 */
	ProcessingRequestSummary(final UniqueId id, final Priority priority, final Date significantTimestamp)
	{
		this.id = id;
		this.priority = priority;
		this.significantTimestamp = significantTimestamp;
	}

	/**
	 * @return the id
	 */
	public UniqueId getId()
	{
		return id;
	}

	/**
	 * @return the priority
	 */
	public Priority getPriority()
	{
		return priority;
	}
	
	/**
	 * @return the significantTimestamp
	 */
	public Date getSignificantTimestamp()
	{
		return significantTimestamp;
	}
}
