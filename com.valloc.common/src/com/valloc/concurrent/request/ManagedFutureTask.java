/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.valloc.CategoryType;
import com.valloc.Identifiable;
import com.valloc.Priority;
import com.valloc.framework.FrameworkManager;
import com.valloc.interrupt.InterruptCompletionListener;
import com.valloc.interrupt.InterruptFuture;
import com.valloc.interrupt.InterruptType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.util.PairContainer;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
final class ManagedFutureTask<V> extends FutureTask<V> implements PrioritizableRunnable, Identifiable<UniqueId>, InterruptCompletionListener
{
	private static final Logger logger = LogManager.manager().getLogger(ManagedFutureTask.class, CategoryType.CONCURRENT_CONTAINER_REQUEST);
	private final ExecutorWrapper executorWrapper;
	private final RequestContainerStatistician statistician;
	private boolean throwableEncountered;
	private final FrameworkManager frameworkManager;
	
	/**
	 * @param callable
	 */
	ManagedFutureTask(final Callable<V> callable, final FrameworkManager frameworkManager, final RequestContainerStatistician statistician) {
		super(callable);
		
		if (!(callable instanceof ExecutorWrapper)) {
			throw new IllegalArgumentException("Callable argument needs to be of aggregate type " + ExecutorWrapper.class.getSimpleName());
		}
		this.executorWrapper = (ExecutorWrapper) callable;
		this.frameworkManager = frameworkManager;
		this.statistician = statistician;
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.FutureTask#done()
	 */
	@Override
	protected void done() {
		super.done();
		
		try { // invoke get to trigger possible throwable that occurred within executor
			get();
		} catch (final ExecutionException e) {
			final Throwable cause = e.getCause();
			logger.error("Managed task request %s threw throwable.", cause, executorWrapper.id());
			setException(cause);
		} catch (final InterruptedException e) {
			logger.error("Managed task request %s has been interrupted.", e, executorWrapper.id());
			setException(e);
		} catch (final CancellationException expected) {
			logger.finer("Managed task request %s has was cancelled as possibly expected.", executorWrapper.id());
		}		
		
		final boolean normallyCompleted = !isCancelled() && !throwableEncountered;
		if (normallyCompleted) {
			final Date inceptionTimestamp = executorWrapper.getTimestamp();
			statistician.reportCompletedRequest(inceptionTimestamp);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.FutureTask#cancel(boolean)
	 * 
	 * NOTE: This is ONLY called from the ThreadPoolExecutor level (or unit testing) when the container
	 * issues a shutdown()/shutdownNow() and calls this hook method from the container level.
	 * 
	 * It is NOT to be called from the application framework level via RequestContainer.requestCancellation()
	 * when a general application framework interrupt or Tx rollback has been requested. Those are issued from the
	 * framework manager in a more orderly fashion.  By doing that, they will naturally be allowed necessary time
	 * to interrupt, complete and naturally fall out of thread scope when the task completes interruption. 
	 */
	@Override
	public boolean cancel(final boolean mayInterruptIfRunning) {
		boolean wasCancelled = false; // not yet
		if (logger.isFiner()) {
			logger.finer("A cancel request was issued to request %s and had a mayInterruptIfRunning flag set as %b.", executorWrapper.id(), mayInterruptIfRunning);
		}

		// Establish common arguments and interpret interrupt type: 
		final UniqueId interruptId = executorWrapper.id();
		// This interruptType could be null if NOT set from requestContainer.requestCancellation();
		InterruptType interruptType = executorWrapper.getCancelType();
		// So, if null then interpret as an outside-in system shutdown (like from ThreadPoolExecutor.shutdown/Now())
		//		in which we generically set it as a type of InterruptType.SYSTEM
		if (interruptType == null) {
			interruptType = InterruptType.SYSTEM;
		}
		
		// 
		// Step 1: Issue interrupt to Valloc application framework.
		//	
		if (!mayInterruptIfRunning) {
			final InterruptCompletionListener completionListener = this;
			final InterruptFuture interruptFuture = frameworkManager.requestInterrupt(interruptId, interruptType, completionListener);
			// since it is NOT to be interrupted while running, we have patience and wait to finish...
			final Result result = interruptFuture.blockForResult();
			if (logger.isFiner()) {
				logger.finer("Managed task request %s has completed waiting for framework level interruption and has a result status as %s.", executorWrapper.id(), result.getMessageSummary());
			}
		} else {
			frameworkManager.requestInterrupt(interruptId, interruptType);
			if (logger.isFiner()) {
				logger.finer("Managed task request %s has issued a framework level interrupt and will immediately continue on.", executorWrapper.id());
			}
		}
		
		// 
		// Step 2: Issue interrupt at thread and executor container level.
		//	
		// notify and cancel on pool thread for container to receive
		wasCancelled = super.cancel(mayInterruptIfRunning);
		if (logger.isFiner()) {
			logger.finer("Managed task request %s had cancel invoked and completed with a status of %b.", executorWrapper.id(), wasCancelled);
		}
		
		// report to statistician
		final Date cancelTimestamp = Util.nowTimestamp();
		final CancelSummary cancelSummary = new CancelSummary(interruptId, interruptType, cancelTimestamp, wasCancelled);
		statistician.reportCancelledRequest(cancelSummary);
		
		return wasCancelled;
	}
	
	@Override
	public void onCompletion(final InterruptType interruptType, final Result result) {
		if (logger.isFiner()) {
			logger.finer("Managed task request %s was interrupted and interruption has been reported as completed.", executorWrapper.id());
		}
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.FutureTask#setException(java.lang.Throwable)
	 */
	@Override
	protected void setException(final Throwable throwable) {
		super.setException(throwable);

		throwableEncountered = true;
		final UniqueId id = executorWrapper.id();
		final String message = throwable.getMessage();
		final String toString = throwable.toString();
		final Date exceptionTimestamp = Util.nowTimestamp();
		final ThrowableSummary throwableSummary = new ThrowableSummary(id, message, toString, exceptionTimestamp);
		statistician.reportExceptionThrownRequest(throwableSummary);
	}

	/* (non-Javadoc)
	 * @see com.valloc.domain.system.Prioritizable#getPriority()
	 */
	@Override
	public Priority getPriority() {
		return executorWrapper.getPriority();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final PrioritizableRunnable subject) {
		int retval = 1; // default to this being lower in priority
		
		final Priority thisPriority = executorWrapper.getPriority();
		final Priority subjectPriority = subject.getPriority();
		
		if (thisPriority.isHigher(subjectPriority)) {
			retval = -1;
		} else if (thisPriority.equals(subjectPriority)) {
			final long thisContainerId = executorWrapper.getContainerId();
			final long subjectContainerId = subject.getContainerId();
			retval = thisContainerId < subjectContainerId ? -1 : (thisContainerId > subjectContainerId ? 1 : 0);
		}
		
		return retval;
	}

	/* (non-Javadoc)
	 * @see com.valloc.concurrent.container.PrioritizableRequestElement#getTimestamp()
	 */
	@Override
	public Date getTimestamp() {
		return executorWrapper.getTimestamp();
	}

	/* (non-Javadoc)
	 * @see com.valloc.concurrent.container.PrioritizableRequestElement#setTimestamp(java.util.Date)
	 */
	@Override
	public void setTimestamp(final Date timestamp) {
		executorWrapper.setTimestamp(timestamp);
	}

//	InterruptType getCancelType() {
//		return executorWrapper.getInterruptionType();
//	}

	/**
	 * @return the runnableExecutorWrapper
	 */
	ExecutorWrapper getExecutorWrapper() {
		return executorWrapper;
	}

	/* (non-Javadoc)
	 * @see com.valloc.Identifiable#id()
	 */
	@Override
	public UniqueId id() {
		return executorWrapper.id();
	}

	/* (non-Javadoc)
	 * @see com.valloc.concurrent.request.PrioritizableRequestElement#getContainerId()
	 */
	@Override
	public long getContainerId() {
		return executorWrapper.getContainerId();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final PairContainer<String, Object> params = new PairContainer<String, Object>();
		params.addPair("priority", getPriority());
		params.addPair("inception-timestamp", getTimestamp());
		params.addPair("request-id", id());
		final String paramStr = params.toString();
		return paramStr;
	}	
}
