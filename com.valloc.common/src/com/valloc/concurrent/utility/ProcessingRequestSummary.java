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
public class ProcessingRequestSummary
{
	private final UniqueId id;
	private final UtilityType utilityType;
	private final Date significantTimestamp;
	
	/**
	 * @param id
	 * @param priority
	 */
	ProcessingRequestSummary(final UniqueId id, final UtilityType utilityType, final Date significantTimestamp)
	{
		this.id = id;
		this.utilityType = utilityType;
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
	 * @return the utilityType
	 */
	public UtilityType getUtilityType()
	{
		return utilityType;
	}

	/**
	 * @return the significantTimestamp
	 */
	public Date getSignificantTimestamp()
	{
		return significantTimestamp;
	}
}
