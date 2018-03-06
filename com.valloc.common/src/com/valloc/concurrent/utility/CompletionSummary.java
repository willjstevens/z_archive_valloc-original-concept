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
public final class CompletionSummary
{
	private final UniqueId utilityId;
	private final UtilityType utilityType;
	private final Date startTimestamp;
	private final Date endTimestamp;
	
	/**
	 * @param utilityId
	 * @param startTimestamp
	 * @param endTimestamp
	 */
	CompletionSummary(final UniqueId utilityId, final UtilityType utilityType, final Date startTimestamp, final Date endTimestamp)
	{
		this.utilityId = utilityId;
		this.utilityType = utilityType;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}

	/**
	 * @return the utilityId
	 */
	public UniqueId getUtilityId()
	{
		return utilityId;
	}

	/**
	 * @return the utilityType
	 */
	public UtilityType getUtilityType()
	{
		return utilityType;
	}

	/**
	 * @return the startTimestamp
	 */
	public Date getStartTimestamp()
	{
		return startTimestamp;
	}

	/**
	 * @return the endTimestamp
	 */
	public Date getEndTimestamp()
	{
		return endTimestamp;
	}
}
