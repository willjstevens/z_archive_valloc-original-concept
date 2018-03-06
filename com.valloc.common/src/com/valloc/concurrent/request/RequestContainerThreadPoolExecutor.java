/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.valloc.Constants;
import com.valloc.framework.FrameworkManager;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;

/**
 * 
 * 
 * @author wstevens
 */
public class RequestContainerThreadPoolExecutor extends ThreadPoolExecutor
{
	private final RequestContainer requestContainer;
	private final RequestContainerStatistician statistician;
	private FrameworkManager frameworkManager;

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
	@SuppressWarnings(Constants.UNCHECKED)
	public RequestContainerThreadPoolExecutor(final RequestContainer requestContainer, final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<? extends Runnable> workQueue, final ThreadFactory threadFactory, final RejectedExecutionHandler handler, final RequestContainerStatistician statistician) {
		// NOTE: Here workQueue is cast to contain Runnable elements to satisfy compiler
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, (BlockingQueue<Runnable>) workQueue, threadFactory, handler);
		this.requestContainer = requestContainer;
		this.statistician = statistician;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.AbstractExecutorService#newTaskFor(java.util.concurrent.Callable)
	 */
	@Override
	protected synchronized <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
		final ManagedFutureTask<T> retval = new ManagedFutureTask<T>(callable, frameworkManager, statistician);
		final Date now = Util.nowTimestamp();
		retval.setTimestamp(now);
		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	 */
	@SuppressWarnings(Constants.UNCHECKED)
	@Override
	protected void beforeExecute(final Thread thread, final Runnable task) {
		super.beforeExecute(thread, task);

		if (!(task instanceof ManagedFutureTask)) {
			throw new IllegalArgumentException("Runnable argument needs to be of type " + ManagedFutureTask.class.getSimpleName());
		}

		final ManagedFutureTask<UniqueId> identifiableTask = (ManagedFutureTask<UniqueId>) task;
		final UniqueId requestId = identifiableTask.id();
		requestContainer.executionBeginning(requestId, identifiableTask);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@SuppressWarnings(Constants.UNCHECKED)
	@Override
	protected void afterExecute(final Runnable task, final Throwable throwable) {
		super.afterExecute(task, throwable);

		if (!(task instanceof ManagedFutureTask)) {
			throw new IllegalArgumentException("Runnable argument needs to be of type " + ManagedFutureTask.class.getSimpleName());
		}

		final ManagedFutureTask<UniqueId> identifiableTask = (ManagedFutureTask<UniqueId>) task;
		final UniqueId requestId = identifiableTask.id();
		requestContainer.executionComplete(requestId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(final Runnable command) {
		super.execute(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
	 */
	@Override
	public void shutdown() {
		super.shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdownNow()
	 */
	@Override
	public List<Runnable> shutdownNow() {
		return super.shutdownNow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#terminated()
	 */
	@Override
	protected void terminated() {
		super.terminated();
	}
	
	public void setFrameworkManager(final FrameworkManager frameworkManager) {
		this.frameworkManager = frameworkManager;
	}
	
}
