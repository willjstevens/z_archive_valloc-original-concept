/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.concurrent.utility.UtilityExecutor;
import com.valloc.interrupt.InterruptEscapeException;
import com.valloc.interrupt.InterruptType;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class UtilityExecutorAdapter implements UtilityExecutor
{
	private final UniqueId id;
	private InterruptType interruptionType;
	
	/**
	 * @param id
	 */
	public UtilityExecutorAdapter(final UniqueId id)
	{
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute()
	{
	}

	/* (non-Javadoc)
	 * @see com.valloc.Identifiable#id()
	 */
	@Override
	public UniqueId id()
	{
		return id;
	}

	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#cancel()
	 */
	@Override
	public void handleInterrupt()
	{
	}

	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#checkCancellation()
	 */
	@Override
	public void checkInterruption() throws InterruptEscapeException
	{
	}

	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#markForCancel(com.valloc.InterruptionType)
	 */
	@Override
	public void requestInterrupt(final InterruptType interruptionType)
	{
		this.interruptionType = interruptionType;
	}

	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#wasCancelled()
	 */
	@Override
	public boolean isSubjectInterrupted()
	{
		return interruptionType != null;
	}

	/* (non-Javadoc)
	 * @see com.valloc.Interruptible#getCancelType()
	 */
	@Override
	public InterruptType getInterruptionType()
	{
		return interruptionType;
	}
}
