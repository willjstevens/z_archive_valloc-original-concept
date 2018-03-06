/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;

import com.valloc.MessageSummary;
import com.valloc.config.ConfigurationChangeListener;
import com.valloc.framework.FrameworkExecutor;
import com.valloc.framework.FrameworkManager;
import com.valloc.interrupt.InterruptType;
import com.valloc.state.StateChangeListener;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public interface RequestContainer extends StateChangeListener, ConfigurationChangeListener<RequestContainerConfiguration>, ThreadFactory, RejectedExecutionHandler
{
	public void queueForExecution(FrameworkExecutor executor);
	public void requestCancellation(UniqueId requestId);
	public void requestCancellation(UniqueId requestId, InterruptType interruptType);	
	public RequestContainerSnapshot takeThreadContainerSnapshot();
	public void purgeStats();
	
	void executionBeginning(UniqueId requestId, ManagedFutureTask<UniqueId> task);
	void executionComplete(UniqueId requestId);
	MessageSummary start();
	MessageSummary destroy();
	MessageSummary kill();

	long getTotalRequestsMade();
	int getBacklogQueueSize();
	List<ProcessingRequestSummary> getQueuedRequestLog();
	List<ProcessingRequestSummary> getActiveRequestLog();
	int getThreadPoolExecutorActiveCount();
	int getThreadPoolExecutorPoolSize();
	int getThreadPoolExecutorCorePoolSize();
	int getThreadPoolExecutorMaximumPoolSize();
	int getThreadPoolExecutorLargestPoolSize();
	long getThreadPoolExecutorTaskCount();
	long getThreadPoolExecutorKeepAliveTime();
	long getThreadPoolExecutorCompletedTaskCount();
	
	public void setFrameworkManager(FrameworkManager frameworkManager);
}
