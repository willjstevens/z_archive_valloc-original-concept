/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;

import com.valloc.MessageSummary;
import com.valloc.config.ConfigurationChangeListener;
import com.valloc.interrupt.InterruptType;
import com.valloc.state.StateChangeListener;
import com.valloc.thread.Frequency;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public interface UtilityContainer extends StateChangeListener, ConfigurationChangeListener<UtilityContainerConfiguration>, ThreadFactory, RejectedExecutionHandler
{
	public <R> FutureResult<R> queueExecution(ResultUtilityExecutor<R> utilityExecutor);
	public <R> FutureResult<R> scheduleExecution(ResultUtilityExecutor<R> utilityExecutor, Frequency delay);
	public void scheduleAtFixedDelay(UtilityExecutor utilityExecutor, Frequency frequency);
	public void scheduleAtFixedDelay(UtilityExecutor utilityExecutor, Frequency frequency, UtilityCompletionListener completionListener);
	public void scheduleAtFixedRate(UtilityExecutor utilityExecutor, Frequency frequency);
	public void scheduleAtFixedRate(UtilityExecutor utilityExecutor, Frequency frequency, UtilityCompletionListener completionListener);
	public void removeFurtherExecutions(UniqueId utilityId);
	public void requestCancellation(UniqueId utilityId);
	public void requestCancellation(UniqueId utilityId, InterruptType interruptType);	
	public UtilityContainerSnapshot takeThreadContainerSnapshot();
	public void captureCompletionLogDetails(boolean doCapture);
	public void flushCompletionLogDetails();
	public void purgeStats();

	ScheduledFuture<?> schedule(Runnable utilityExecutor, int secondsDelay);
	<R> void executionBeginning(ManagedRunnableScheduledFuture<R> task);
	void executionComplete(UniqueId utilityId);
	MessageSummary start();
	MessageSummary destroy();
	MessageSummary kill();

	long getTotalRequestsMade();
	int getScheduledQueueSize();
	List<ProcessingRequestSummary> getActiveRequestLog();
	int getThreadPoolExecutorActiveCount();
	int getThreadPoolExecutorPoolSize();
	int getThreadPoolExecutorCorePoolSize();
	int getThreadPoolExecutorMaximumPoolSize();
	int getThreadPoolExecutorLargestPoolSize();
	long getThreadPoolExecutorTaskCount();
	long getThreadPoolExecutorCompletedTaskCount();
}
