/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.Date;

import com.valloc.interrupt.InterruptEscapeException;
import com.valloc.interrupt.InterruptType;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
abstract class BaseExecutorWrapper implements UtilityExecutor
{
	private final UtilityType utilityType;
	private final Date inceptionTimestamp;
	private InterruptType passThroughCancelType;
	private Throwable caughtThrowable;
	
	/**
	 * 
	 * @param executor
	 */
	BaseExecutorWrapper(final UtilityType utilityType, final Date inceptionTimestamp)
	{
		this.utilityType = utilityType;
		this.inceptionTimestamp = inceptionTimestamp;
	}

	protected abstract UtilityExecutor getUtilityExecutor();
	
	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#cancel()
	 */
	@Override
	public void handleInterrupt()
	{
		getUtilityExecutor().handleInterrupt();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#checkCancellation()
	 */
	@Override
	public void checkInterruption() throws InterruptEscapeException
	{
		getUtilityExecutor().checkInterruption();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#markForCancel(com.valloc.InterruptionType)
	 */
	@Override
	public void requestInterrupt(final InterruptType interruptType)
	{
		getUtilityExecutor().requestInterrupt(interruptType);
	}

	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#wasCancelled()
	 */
	@Override
	public boolean isSubjectInterrupted()
	{
		return getUtilityExecutor().isSubjectInterrupted();
	}

	@Override
	public UniqueId id()
	{
		return getUtilityExecutor().id();
	}


	public Date getInceptionTimestamp()
	{
		return inceptionTimestamp;
	}

	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#getCancelType()
	 */
	@Override
	public InterruptType getInterruptionType()
	{
		return getUtilityExecutor().getInterruptionType();
	}

	/**
	 * @param passthroughCancelType the passthroughCancelType to set
	 */
	void setPassthroughCancelType(final InterruptType interruptType)
	{
		this.passThroughCancelType = interruptType;
	}

	/**
	 * @return the passthroughCancelType
	 */
	InterruptType getPassthroughCancelType()
	{
		return passThroughCancelType;
	}

	/**
	 * @return the utilityType
	 */
	UtilityType getUtilityType()
	{
		return utilityType;
	}

	/**
	 * @return the caughtThrowable
	 */
	Throwable getCaughtThrowable()
	{
		return caughtThrowable;
	}

	/**
	 * @param caughtThrowable the caughtThrowable to set
	 */
	void setCaughtThrowable(final Throwable caughtThrowable)
	{
		this.caughtThrowable = caughtThrowable;
	}
	
	boolean hasCaughtThrowable()
	{
		return caughtThrowable != null;
	}
}
