/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.Date;
import java.util.List;


/**
 *
 *
 * @author wstevens
 */
public class UtilityContainerSnapshot
{
	private final Date snapshotTimestamp;	
	private long totalRequestsMade;
	private int completedRequestsCount;
	private int completedRequestSingleImmediate;
	private int completedRequestSingleScheduled;
	private int completedRequestRepeatingFixedScheduled;
	private int completedRequestRepeatingDelayedScheduled;
	private long completedRequestAverageDurationInMillis;
	private long highWatermarkCompletedRequestDuration;
	private int scheduledQueueSize;
	private List<ProcessingRequestSummary> activeRequestLog;
	private List<CompletionSummary> completionLog;
	private List<CancelSummary> cancelLog;
	private List<ThrowableSummary> exceptionLog;
	private List<RejectSummary> rejectLog;
	
	// off the thread pool executor
	private int threadPoolExecutorActiveCount;
	private long threadPoolExecutorCompletedTaskCount;
	private int threadPoolExecutorCorePoolSize;
	private int threadPoolExecutorLargestPoolSize;
	private int threadPoolExecutorMaximumPoolSize;
	private int threadPoolExecutorPoolSize;
	private long threadPoolExecutorTaskCount;
		
	/**
	 * @param snapshotTimestamp
	 */
	UtilityContainerSnapshot(final Date snapshotTimestamp)
	{
		this.snapshotTimestamp = snapshotTimestamp;
	}
	
	/**
	 * @return the totalRequestsMade
	 */
	public long getTotalRequestsMade()
	{
		return totalRequestsMade;
	}

	/**
	 * @param totalRequestsMade the totalRequestsMade to set
	 */
	void setTotalRequestsMade(final long totalRequestsMade)
	{
		this.totalRequestsMade = totalRequestsMade;
	}

	/**
	 * @param activeRequestLog the activeRequestLog to set
	 */
	void setActiveRequestLog(final List<ProcessingRequestSummary> activeRequestLog)
	{
		this.activeRequestLog = activeRequestLog;
	}

	/**
	 * @param cancelLog the cancelLog to set
	 */
	void setCancelLog(final List<CancelSummary> cancelLog)
	{
		this.cancelLog = cancelLog;
	}

	/**
	 * @param exceptionLog the exceptionLog to set
	 */
	void setExceptionLog(final List<ThrowableSummary> exceptionLog)
	{
		this.exceptionLog = exceptionLog;
	}

	/**
	 * @param completedRequestsCount the completedRequestsCount to set
	 */
	void setCompletedRequestsCount(final int normalRequestsCount)
	{
		this.completedRequestsCount = normalRequestsCount;
	}

	/**
	 * @param completedRequestAverageDurationInMillis the completedRequestAverageDurationInMillis to set
	 */
	void setCompletedRequestAverageDurationInMillis(final long normalRequestAverageDurationInMillis)
	{
		this.completedRequestAverageDurationInMillis = normalRequestAverageDurationInMillis;
	}

	/**
	 * @param highWatermarkCompletedRequestDuration the highWatermarkCompletedRequestDuration to set
	 */
	void setHighWatermarkCompletedRequestDuration(final long highWatermarkNormalRequestDuration)
	{
		this.highWatermarkCompletedRequestDuration = highWatermarkNormalRequestDuration;
	}

	/**
	 * @param completedRequestSingleImmediate the completedRequestSingleImmediate to set
	 */
	void setCompletedRequestSingleImmediate(final int completedRequestSingleImmediate)
	{
		this.completedRequestSingleImmediate = completedRequestSingleImmediate;
	}

	/**
	 * @param completedRequestSingleScheduled the completedRequestSingleScheduled to set
	 */
	void setCompletedRequestSingleScheduled(final int completedRequestSingleScheduled)
	{
		this.completedRequestSingleScheduled = completedRequestSingleScheduled;
	}

	/**
	 * @param completedRequestRepeatingFixedScheduled the completedRequestRepeatingFixedScheduled to set
	 */
	void setCompletedRequestRepeatingFixedScheduled(final int completedRequestRepeatingFixedScheduled)
	{
		this.completedRequestRepeatingFixedScheduled = completedRequestRepeatingFixedScheduled;
	}

	/**
	 * @param completedRequestRepeatingDelayedScheduled the completedRequestRepeatingDelayedScheduled to set
	 */
	void setCompletedRequestRepeatingDelayedScheduled(final int completedRequestRepeatingDelayedScheduled)
	{
		this.completedRequestRepeatingDelayedScheduled = completedRequestRepeatingDelayedScheduled;
	}

	/**
	 * @return the completedRequestsCount
	 */
	public int getCompletedRequestsCount()
	{
		return completedRequestsCount;
	}

	/**
	 * @return the completedRequestAverageDurationInMillis
	 */
	public long getCompletedRequestAverageDurationInMillis()
	{
		return completedRequestAverageDurationInMillis;
	}

	/**
	 * @return the highWatermarkCompletedRequestDuration
	 */
	public long getHighWatermarkCompletedRequestDuration()
	{
		return highWatermarkCompletedRequestDuration;
	}

	/**
	 * @return the snapshotTimestamp
	 */
	public Date getSnapshotTimestamp()
	{
		return snapshotTimestamp;
	}

	/**
	 * @return the activeRequestLog
	 */
	public List<ProcessingRequestSummary> getActiveRequestLog()
	{
		return activeRequestLog;
	}

	/**
	 * @return the completionLog
	 */
	public List<CompletionSummary> getCompletionLog()
	{
		return completionLog;
	}

	/**
	 * @return the cancelLog
	 */
	public List<CancelSummary> getCancelLog()
	{
		return cancelLog;
	}

	/**
	 * @return the exceptionLog
	 */
	public List<ThrowableSummary> getExceptionLog()
	{
		return exceptionLog;
	}

	/**
	 * @return the rejectLog
	 */
	public List<RejectSummary> getRejectLog()
	{
		return rejectLog;
	}

	/**
	 * @return the scheduledQueueSize
	 */
	public int getScheduledQueueSize()
	{
		return scheduledQueueSize;
	}

	/**
	 * @param scheduledQueueSize the scheduledQueueSize to set
	 */
	void setScheduledQueueSize(final int backlogQueueSize)
	{
		this.scheduledQueueSize = backlogQueueSize;
	}

	/**
	 * @param rejectLog the rejectLog to set
	 */
	void setRejectLog(final List<RejectSummary> rejectLog)
	{
		this.rejectLog = rejectLog;
	}

	/**
	 * @param threadPoolExecutorActiveCount the threadPoolExecutorActiveCount to set
	 */
	void setThreadPoolExecutorActiveCount(final int threadPoolExecutorActiveCount)
	{
		this.threadPoolExecutorActiveCount = threadPoolExecutorActiveCount;
	}

	/**
	 * @param threadPoolExecutorCompletedTaskCount the threadPoolExecutorCompletedTaskCount to set
	 */
	void setThreadPoolExecutorCompletedTaskCount(
			final long threadPoolExecutorCompletedTaskCount)
	{
		this.threadPoolExecutorCompletedTaskCount = threadPoolExecutorCompletedTaskCount;
	}

	/**
	 * @param threadPoolExecutorCorePoolSize the threadPoolExecutorCorePoolSize to set
	 */
	void setThreadPoolExecutorCorePoolSize(final int threadPoolExecutorCorePoolSize)
	{
		this.threadPoolExecutorCorePoolSize = threadPoolExecutorCorePoolSize;
	}

	/**
	 * @param threadPoolExecutorLargestPoolSize the threadPoolExecutorLargestPoolSize to set
	 */
	void setThreadPoolExecutorLargestPoolSize(final int threadPoolExecutorLargestPoolSize)
	{
		this.threadPoolExecutorLargestPoolSize = threadPoolExecutorLargestPoolSize;
	}

	/**
	 * @param threadPoolExecutorMaximumPoolSize the threadPoolExecutorMaximumPoolSize to set
	 */
	void setThreadPoolExecutorMaximumPoolSize(final int threadPoolExecutorMaximumPoolSize)
	{
		this.threadPoolExecutorMaximumPoolSize = threadPoolExecutorMaximumPoolSize;
	}

	/**
	 * @param threadPoolExecutorPoolSize the threadPoolExecutorPoolSize to set
	 */
	void setThreadPoolExecutorPoolSize(final int threadPoolExecutorPoolSize)
	{
		this.threadPoolExecutorPoolSize = threadPoolExecutorPoolSize;
	}

	/**
	 * @param threadPoolExecutorTaskCount the threadPoolExecutorTaskCount to set
	 */
	void setThreadPoolExecutorTaskCount(final long threadPoolExecutorTaskCount)
	{
		this.threadPoolExecutorTaskCount = threadPoolExecutorTaskCount;
	}

	/**
	 * @param completionLog the completionLog to set
	 */
	void setCompletionLog(final List<CompletionSummary> completionLog)
	{
		this.completionLog = completionLog;
	}

	/**
	 * @return the completedRequestSingleImmediate
	 */
	public int getCompletedRequestSingleImmediate()
	{
		return completedRequestSingleImmediate;
	}

	/**
	 * @return the completedRequestSingleScheduled
	 */
	public int getCompletedRequestSingleScheduled()
	{
		return completedRequestSingleScheduled;
	}

	/**
	 * @return the completedRequestRepeatingFixedScheduled
	 */
	public int getCompletedRequestRepeatingFixedScheduled()
	{
		return completedRequestRepeatingFixedScheduled;
	}

	/**
	 * @return the completedRequestRepeatingDelayedScheduled
	 */
	public int getCompletedRequestRepeatingDelayedScheduled()
	{
		return completedRequestRepeatingDelayedScheduled;
	}

	/**
	 * @return the threadPoolExecutorActiveCount
	 */
	public int getThreadPoolExecutorActiveCount()
	{
		return threadPoolExecutorActiveCount;
	}

	/**
	 * @return the threadPoolExecutorCompletedTaskCount
	 */
	public long getThreadPoolExecutorCompletedTaskCount()
	{
		return threadPoolExecutorCompletedTaskCount;
	}

	/**
	 * @return the threadPoolExecutorCorePoolSize
	 */
	public int getThreadPoolExecutorCorePoolSize()
	{
		return threadPoolExecutorCorePoolSize;
	}

	/**
	 * @return the threadPoolExecutorLargestPoolSize
	 */
	public int getThreadPoolExecutorLargestPoolSize()
	{
		return threadPoolExecutorLargestPoolSize;
	}

	/**
	 * @return the threadPoolExecutorMaximumPoolSize
	 */
	public int getThreadPoolExecutorMaximumPoolSize()
	{
		return threadPoolExecutorMaximumPoolSize;
	}

	/**
	 * @return the threadPoolExecutorPoolSize
	 */
	public int getThreadPoolExecutorPoolSize()
	{
		return threadPoolExecutorPoolSize;
	}

	/**
	 * @return the threadPoolExecutorTaskCount
	 */
	public long getThreadPoolExecutorTaskCount()
	{
		return threadPoolExecutorTaskCount;
	}
}
