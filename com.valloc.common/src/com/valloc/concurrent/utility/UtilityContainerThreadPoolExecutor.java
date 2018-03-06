/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import com.valloc.Constants;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class UtilityContainerThreadPoolExecutor extends ScheduledThreadPoolExecutor
{
	private final UtilityContainer utilityContainer;
	private final UtilityContainerStatistician statistician;

	/**
	 *
	 *
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param threadFactory
	 * @param handler
	 */
	public UtilityContainerThreadPoolExecutor(final UtilityContainer utilityContainer,
										final int corePoolSize,
										final ThreadFactory threadFactory,
										final RejectedExecutionHandler handler,
										final UtilityContainerStatistician statistician)
	{
		// NOTE: Here workQueue is cast to contain Runnable elements to satisfy compiler
		super(corePoolSize, threadFactory, handler);
		this.utilityContainer = utilityContainer;
		this.statistician = statistician;
	}



	/* (non-Javadoc)
	 * @see java.util.concurrent.ScheduledThreadPoolExecutor#decorateTask(java.lang.Runnable, java.util.concurrent.RunnableScheduledFuture)
	 */
	@Override
	protected <V> RunnableScheduledFuture<V> decorateTask(final Runnable runnable, final RunnableScheduledFuture<V> task)
	{
		final RunnableScheduledFuture<V> defaultImpl = super.decorateTask(runnable, task);
		final RepeatingExecutionWrapper repeatingExecutionWrapper = (RepeatingExecutionWrapper) runnable;
		final ManagedRunnableScheduledFuture<V> managedRunnableScheduledFuture = new ManagedRunnableScheduledFuture<V>(defaultImpl, repeatingExecutionWrapper, statistician);

		return managedRunnableScheduledFuture;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ScheduledThreadPoolExecutor#decorateTask(java.util.concurrent.Callable, java.util.concurrent.RunnableScheduledFuture)
	 */
	@Override
	protected <V> RunnableScheduledFuture<V> decorateTask(final Callable<V> callable, final RunnableScheduledFuture<V> task)
	{
		final RunnableScheduledFuture<V> defaultImpl = super.decorateTask(callable, task);
		final SingleExecutionWrapper<V> callableExecutorWrapper = (SingleExecutionWrapper<V>) callable;
		final ManagedRunnableScheduledFuture<V> managedRunnableScheduledFuture = new ManagedRunnableScheduledFuture<V>(defaultImpl, callableExecutorWrapper, statistician);
		return managedRunnableScheduledFuture;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	 */
	@SuppressWarnings({Constants.UNCHECKED, Constants.RAWTYPES})
	@Override
	protected void beforeExecute(final Thread thread, final Runnable task)
	{
		super.beforeExecute(thread, task);

		if (!(task instanceof ManagedRunnableScheduledFuture)) {
			throw new IllegalArgumentException("Runnable argument needs to be of type " + ManagedRunnableScheduledFuture.class.getSimpleName());
		}
		final ManagedRunnableScheduledFuture identifiableTask = (ManagedRunnableScheduledFuture) task;
		utilityContainer.executionBeginning(identifiableTask);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@SuppressWarnings(Constants.RAWTYPES)
	@Override
	protected void afterExecute(final Runnable runnable, final Throwable throwable)
	{
		super.afterExecute(runnable, throwable);

		if (!(runnable instanceof ManagedRunnableScheduledFuture)) {
			throw new IllegalArgumentException("Runnable argument needs to be of type " + ManagedRunnableScheduledFuture.class.getSimpleName());
		}
		final ManagedRunnableScheduledFuture identifiableTask = (ManagedRunnableScheduledFuture) runnable;
		final UniqueId utilityId = identifiableTask.id();
		utilityContainer.executionComplete(utilityId);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(final Runnable command)
	{
		super.execute(command);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
	 */
	@Override
	public void shutdown()
	{
		super.shutdown();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdownNow()
	 */
	@Override
	public List<Runnable> shutdownNow()
	{
		return super.shutdownNow();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#terminated()
	 */
	@Override
	protected void terminated()
	{
		super.terminated();
	}
}
