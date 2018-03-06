/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.Date;
import java.util.concurrent.Callable;

import com.valloc.Priority;
import com.valloc.framework.FrameworkExecutor;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.interrupt.InterruptType;
import com.valloc.util.UniqueId;

/**
 * 
 * 
 * @author wstevens
 */
class ExecutorWrapper implements Callable<UniqueId>, FrameworkExecutor, PrioritizableRequestElement
{
	private final FrameworkExecutor frameworkExecutor;
	private Date inceptionTimestamp;
	private final long containerId;
	private InterruptType interruptType;

	/**
	 * 
	 */
	ExecutorWrapper(final FrameworkExecutor executor, final long containerId) {
		this.frameworkExecutor = executor;
		this.containerId = containerId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public UniqueId call() throws Exception {
		execute();
		return frameworkExecutor.id();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute() {
		frameworkExecutor.execute();
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.valloc.Interruptible#cancel()
//	 */
//	@Override
//	public void handleInterrupt() {
//		frameworkExecutor.handleInterrupt();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.valloc.Interruptible#checkCancellation()
//	 */
//	@Override
//	public void checkInterruption() throws InterruptEscapeException {
//		frameworkExecutor.checkInterruption();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.valloc.Interruptible#markForCancel(com.valloc.InterruptionType)
//	 */
//	@Override
//	public void requestInterrupt(final InterruptType interruptType) {
//		frameworkExecutor.requestInterrupt(this.interruptType);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.valloc.Interruptible#wasCancelled()
//	 */
//	@Override
//	public boolean isSubjectInterrupted() {
//		return frameworkExecutor.isSubjectInterrupted();
//	}

	@Override
	public UniqueId id() {
		return frameworkExecutor.id();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.domain.system.PrioritizableRunnable#getPriority()
	 */
	@Override
	public Priority getPriority() {
		return frameworkExecutor.getPriority();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.container.PrioritizableRequestElement#getTimestamp()
	 */
	@Override
	public Date getTimestamp() {
		return inceptionTimestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.container.PrioritizableRequestElement#setTimestamp(java.util.Date)
	 */
	@Override
	public void setTimestamp(final Date timestamp) {
		inceptionTimestamp = timestamp;
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.valloc.Interruptible#getCancelType()
//	 */
//	@Override
//	public InterruptType getInterruptionType() {
//		return interruptType;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.PrioritizableRequestElement#getContainerId()
	 */
	@Override
	public long getContainerId() {
		return containerId;
	}

	
	InterruptType getCancelType() {
		return interruptType;
	}
	
	void setCancelType(final InterruptType interruptType) {
		this.interruptType = interruptType;
	}

	@Override
	public InterruptTracker getInterruptTracker() {
		return frameworkExecutor.getInterruptTracker();
	}

	@Override
	public void setInterruptTracker(final InterruptTracker interruptTracker) {
		frameworkExecutor.setInterruptTracker(interruptTracker);
	}
	
	
}
