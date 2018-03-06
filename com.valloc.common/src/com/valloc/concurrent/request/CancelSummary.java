/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

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
	private final UniqueId requestId;
	private final InterruptType interruptType;
	private final Date cancelTimestamp;
	private final boolean wasSuccessfullyCancelled;
	
	/**
	 * @param requestId
	 * @param interruptType
	 * @param cancelTimestamp
	 */
	CancelSummary(final UniqueId requestId, final InterruptType interruptType, final Date cancelTimestamp, final boolean wasSuccessfullyCancelled)
	{
		this.requestId = requestId;
		this.interruptType = interruptType;
		this.cancelTimestamp = cancelTimestamp;
		this.wasSuccessfullyCancelled = wasSuccessfullyCancelled;
	}

	/**
	 * @return the requestId
	 */
	public UniqueId getRequestId()
	{
		return requestId;
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
