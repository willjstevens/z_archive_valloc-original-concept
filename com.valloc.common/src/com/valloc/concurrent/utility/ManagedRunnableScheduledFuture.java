/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.Date;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.valloc.CategoryType;
import com.valloc.Identifiable;
import com.valloc.interrupt.InterruptType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.util.PairContainer;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public final class ManagedRunnableScheduledFuture<V> implements RunnableScheduledFuture<V>, Identifiable<UniqueId>
{
	private static final Logger logger = LogManager.manager().getLogger(BaseUtilityContainer.class, CategoryType.CONCURRENT_CONTAINER_REQUEST);
	private final RunnableScheduledFuture<V> source;
	private final BaseExecutorWrapper executorWrapper;
	private final UtilityContainerStatistician statistician;
	private boolean throwableEncountered;
		
	public ManagedRunnableScheduledFuture(final RunnableScheduledFuture<V> source, final BaseExecutorWrapper baseExecutorWrapper, final UtilityContainerStatistician statistician)
	{
		this.source = source;
		this.executorWrapper = baseExecutorWrapper;
		this.statistician = statistician;
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.RunnableScheduledFuture#isPeriodic()
	 */
	@Override
	public boolean isPeriodic()
	{
		return source.isPeriodic();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
	 */
	@Override
	public long getDelay(final TimeUnit unit)
	{
		return source.getDelay(unit);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final Delayed o)
	{
		return source.compareTo(o);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.RunnableFuture#run()
	 */
	@Override
	public void run()
	{
		source.run();
		
		get(); // trigger any exception fallout
		
		final Throwable throwable = executorWrapper.getCaughtThrowable();
		if (throwable != null) {
			setException(throwable);
		}
		
		final boolean normallyCompleted = !isCancelled() && !throwableEncountered || isMarkedForRemoval();
		if (normallyCompleted) {
			final UniqueId utilityId = executorWrapper.id();
			final UtilityType utilityType = executorWrapper.getUtilityType();
			final Date startTimestamp = executorWrapper.getInceptionTimestamp();
			final Date endTimestamp = Util.nowTimestamp();
			final CompletionSummary completionSummary = new CompletionSummary(utilityId, utilityType, startTimestamp, endTimestamp);
			statistician.reportCompletedRequest(completionSummary);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#cancel(boolean)
	 */
	@Override
	public boolean cancel(final boolean mayInterruptIfRunning)
	{
		// first now notify and cancel on pool thread for container to receive;
		//		 also to remove from scheduling
		final boolean wasCancelled = source.cancel(mayInterruptIfRunning);
				 
		if (!isMarkedForRemoval()) {
			// first notify framework executor
			final InterruptType interruptType = executorWrapper.getPassthroughCancelType();
			executorWrapper.requestInterrupt(interruptType);	
			// report to statistician if cancel and not remove
			final UniqueId id = executorWrapper.id();
			final Date cancelTimestamp = Util.nowTimestamp();
			final CancelSummary cancelSummary = new CancelSummary(id, interruptType, cancelTimestamp, wasCancelled);
			statistician.reportCancelledRequest(cancelSummary);
		}
		
		return wasCancelled;
	}

	private boolean isMarkedForRemoval()
	{		
		boolean isMarkedForRemoval = false; // set to true by default
		
		if (executorWrapper instanceof RepeatingExecutionWrapper) {
			final RepeatingExecutionWrapper runnableExecutorWrapper = (RepeatingExecutionWrapper) executorWrapper;
			if (runnableExecutorWrapper.isMarkedForRemoval()) {
				// this condition is cancel is done through removing from further scheduling, so 
				//		do not report as genuine cancel to client code and statistician
				isMarkedForRemoval = true; 
			}
		}
		
		return isMarkedForRemoval;
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#get()
	 */
	@Override
	public V get()
	{
		V futureResult = null;
		
		try { // invoke get to trigger possible throwable that occurred within executor
			futureResult = source.get();
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
		
		return futureResult;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#get(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		return source.get(timeout, unit);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#isCancelled()
	 */
	@Override
	public boolean isCancelled()
	{
		return source.isCancelled();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#isDone()
	 */
	@Override
	public boolean isDone()
	{
		return source.isDone();
	}

	protected void setException(final Throwable throwable)
	{
		throwableEncountered = true;
		final UniqueId id = executorWrapper.id();
		final String message = throwable.getMessage();
		final String toString = throwable.toString();
		final Date exceptionTimestamp = Util.nowTimestamp();
		final ThrowableSummary throwableSummary = new ThrowableSummary(id, message, toString, exceptionTimestamp);
		statistician.reportExceptionThrownRequest(throwableSummary);
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.Identifiable#id()
	 */
	@Override
	public UniqueId id()
	{
		return executorWrapper.id();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final PairContainer<String, Object> params = new PairContainer<String, Object>();
		params.addPair("request-id", id());
		final String paramStr = params.toString();
		return paramStr;
	}

	/**
	 * @return the utilityType
	 */
	UtilityType getUtilityType()
	{
		return executorWrapper.getUtilityType();
	}

	/**
	 * @return the executorWrapper
	 */
	BaseExecutorWrapper getExecutorWrapper()
	{
		return executorWrapper;
	}
}
