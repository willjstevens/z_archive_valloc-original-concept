/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.Date;

import com.valloc.interrupt.InterruptType;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class CancelSummary
{
	private final UniqueId utilityId;
	private final InterruptType interruptType;
	private final Date cancelTimestamp;
	private final boolean wasSuccessfullyCancelled;
	
	/**
	 * @param utilityId
	 * @param interruptType
	 * @param cancelTimestamp
	 */
	CancelSummary(final UniqueId utilityId, final InterruptType interruptType, final Date cancelTimestamp, final boolean wasSuccessfullyCancelled)
	{
		this.utilityId = utilityId;
		this.interruptType = interruptType;
		this.cancelTimestamp = cancelTimestamp;
		this.wasSuccessfullyCancelled = wasSuccessfullyCancelled;
	}

	/**
	 * @return the utilityId
	 */
	public UniqueId getUtilityId()
	{
		return utilityId;
	}

	/**
	 * @return the interruptType
	 */
	public InterruptType getCancelType()
	{
		return interruptType;
	}

	/**
	 * @return the cancelTimestamp
	 */
	public Date getCancelTimestamp()
	{
		return cancelTimestamp;
	}

	/**
	 * @return the wasSuccessfullyCancelled
	 */
	boolean wasSuccessfullyCancelled()
	{
		return wasSuccessfullyCancelled;
	}
}
