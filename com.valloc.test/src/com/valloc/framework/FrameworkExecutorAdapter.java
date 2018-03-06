/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.Priority;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.util.UniqueId;

/**
 * 
 * 
 * @author wstevens
 */
public class FrameworkExecutorAdapter implements FrameworkExecutor
{
	private final UniqueId uniqueId;
	private final Priority priority;
	private InterruptTracker interruptTracker;

	/**
	 * @param uniqueId
	 * @param priority
	 */
	public FrameworkExecutorAdapter(final UniqueId id, final Priority priority) {
		this.uniqueId = id;
		this.priority = priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute() {
		
		// implicitly check and bypass
		interruptTracker.checkInterrupt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.Identifiable#id()
	 */
	@Override
	public UniqueId id() {
		return uniqueId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.domain.system.Prioritizable#getPriority()
	 */
	@Override
	public Priority getPriority() {
		return priority;
	}

	@Override
	public InterruptTracker getInterruptTracker() {
		return interruptTracker;
	}

	@Override
	public void setInterruptTracker(final InterruptTracker interruptTracker) {
		this.interruptTracker = interruptTracker;
	}

	// /* (non-Javadoc)
	// * @see com.valloc.Interruptible#cancel()
	// */
	// @Override
	// public void handleInterrupt()
	// {
	// }
	//
	// /* (non-Javadoc)
	// * @see com.valloc.Interruptible#checkCancellation()
	// */
	// @Override
	// public void checkInterruption() throws InterruptEscapeException
	// {
	// }
	//
	// /* (non-Javadoc)
	// * @see com.valloc.Interruptible#markForCancel(com.valloc.InterruptionType)
	// */
	// @Override
	// public void requestInterrupt(final InterruptType interruptionType)
	// {
	// }
	//
	// /* (non-Javadoc)
	// * @see com.valloc.Interruptible#wasCancelled()
	// */
	// @Override
	// public boolean isSubjectInterrupted()
	// {
	// return false;
	// }
	//
	// /* (non-Javadoc)
	// * @see com.valloc.Interruptible#getCancelType()
	// */
	// @Override
	// public InterruptType getInterruptionType()
	// {
	// return null;
	// }

}
