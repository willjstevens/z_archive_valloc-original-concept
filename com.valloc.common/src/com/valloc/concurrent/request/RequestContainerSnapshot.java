/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.Date;
import java.util.List;


/**
 *
 *
 * @author wstevens
 */
public class RequestContainerSnapshot
{
	private final Date snapshotTimestamp;	
	private long totalRequestsMade;
	private int completedRequestsCount;
	private long completedRequestAverageDurationInMillis;
	private long highWatermarkCompletedRequestDuration;
	private int backlogQueueSize;
	private List<ProcessingRequestSummary> queuedRequestLog;
	private List<ProcessingRequestSummary> activeRequestLog;
	private List<CancelSummary> cancelLog;
	private List<ThrowableSummary> exceptionLog;
	private List<RejectSummary> rejectLog;
	
	// off the thread pool executor
	private int threadPoolExecutorActiveCount;
	private long threadPoolExecutorCompletedTaskCount;
	private int threadPoolExecutorCorePoolSize;
	private long threadPoolExecutorKeepAliveTime;
	private int threadPoolExecutorLargestPoolSize;
	private int threadPoolExecutorMaximumPoolSize;
	private int threadPoolExecutorPoolSize;
	private long threadPoolExecutorTaskCount;
		
	/**
	 * @param snapshotTimestamp
	 */
	RequestContainerSnapshot(final Date snapshotTimestamp)
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
	 * @return the backlogQueueSize
	 */
	public int getBacklogQueueSize()
	{
		return backlogQueueSize;
	}

	/**
	 * @param backlogQueueSize the backlogQueueSize to set
	 */
	void setBacklogQueueSize(final int backlogQueueSize)
	{
		this.backlogQueueSize = backlogQueueSize;
	}

	/**
	 * @return the queuedRequestLog
	 */
	public List<ProcessingRequestSummary> getQueuedRequestLog()
	{
		return queuedRequestLog;
	}

	/**
	 * @param queuedRequestLog the queuedRequestLog to set
	 */
	void setQueuedRequestLog(final List<ProcessingRequestSummary> queuedRequestLog)
	{
		this.queuedRequestLog = queuedRequestLog;
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
	 * @param threadPoolExecutorKeepAliveTime the threadPoolExecutorKeepAliveTime to set
	 */
	void setThreadPoolExecutorKeepAliveTime(final long threadPoolExecutorKeepAliveTime)
	{
		this.threadPoolExecutorKeepAliveTime = threadPoolExecutorKeepAliveTime;
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
	 * @return the threadPoolExecutorKeepAliveTime
	 */
	public long getThreadPoolExecutorKeepAliveTime()
	{
		return threadPoolExecutorKeepAliveTime;
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
