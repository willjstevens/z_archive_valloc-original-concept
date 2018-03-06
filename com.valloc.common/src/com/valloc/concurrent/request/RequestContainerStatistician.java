/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
final class RequestContainerStatistician
{	
	private RequestContainer requestContainer;
	private int completedRequestsCount;
	private long completedRequestAverageDurationInMillis;
	private long highWatermarkCompletedRequestDuration;
	private final List<CancelSummary> cancelLog = new ArrayList<CancelSummary>();
	private final List<RejectSummary> rejectLog = new ArrayList<RejectSummary>();
	private final List<ThrowableSummary> exceptionLog = new ArrayList<ThrowableSummary>();	
	private final Lock readWriteLock = new ReentrantLock(true);
	
	RequestContainerStatistician() {}

	public RequestContainerSnapshot takeThreadContainerSnapshot()
	{
		RequestContainerSnapshot snapshot = null;
		
		try {
			readWriteLock.lock();			
			snapshot = new RequestContainerSnapshot(Util.nowTimestamp());
			snapshot.setTotalRequestsMade(requestContainer.getTotalRequestsMade());
			snapshot.setCompletedRequestsCount(completedRequestsCount);
			snapshot.setCompletedRequestAverageDurationInMillis(completedRequestAverageDurationInMillis);
			snapshot.setHighWatermarkCompletedRequestDuration(highWatermarkCompletedRequestDuration);
			snapshot.setBacklogQueueSize(requestContainer.getBacklogQueueSize());
			snapshot.setQueuedRequestLog(requestContainer.getQueuedRequestLog());
			snapshot.setActiveRequestLog(requestContainer.getActiveRequestLog());
			snapshot.setCancelLog(cancelLog);
			snapshot.setExceptionLog(exceptionLog);
			snapshot.setRejectLog(rejectLog);
			snapshot.setThreadPoolExecutorActiveCount(requestContainer.getThreadPoolExecutorActiveCount());
			snapshot.setThreadPoolExecutorCompletedTaskCount(requestContainer.getThreadPoolExecutorCompletedTaskCount());
			snapshot.setThreadPoolExecutorCorePoolSize(requestContainer.getThreadPoolExecutorCorePoolSize());
			snapshot.setThreadPoolExecutorKeepAliveTime(requestContainer.getThreadPoolExecutorKeepAliveTime());
			snapshot.setThreadPoolExecutorLargestPoolSize(requestContainer.getThreadPoolExecutorLargestPoolSize());
			snapshot.setThreadPoolExecutorMaximumPoolSize(requestContainer.getThreadPoolExecutorMaximumPoolSize());
			snapshot.setThreadPoolExecutorPoolSize(requestContainer.getThreadPoolExecutorPoolSize());
			snapshot.setThreadPoolExecutorTaskCount(requestContainer.getThreadPoolExecutorTaskCount());
		} finally {
			readWriteLock.unlock();
		}
		
		return snapshot;
	}
	
	/* Used mostly for unit testing. */
	public void purge()
	{
		try {
			readWriteLock.lock();			
			completedRequestsCount = 0;
			completedRequestAverageDurationInMillis = 0;
			highWatermarkCompletedRequestDuration = 0;
			cancelLog.clear();
			exceptionLog.clear();
			rejectLog.clear();
		} finally {
			readWriteLock.unlock();
		}
	}
	
	void reportCompletedRequest(final Date inceptionTimestamp)
	{
		try {
			readWriteLock.lock();
			
			completedRequestsCount++; // kick tallied normally completed requests
			
			// reestablish a new normal request running time average
			final long start = inceptionTimestamp.getTime();
			final long end = Util.now();
			final long duration = end - start;
			completedRequestAverageDurationInMillis = (completedRequestAverageDurationInMillis + duration) / completedRequestsCount; 
			
			// check if this request was the highest yet and assign if so
			if (duration > highWatermarkCompletedRequestDuration) {
				highWatermarkCompletedRequestDuration = duration;
			}
			
		} finally {
			readWriteLock.unlock();
		}
	}
	
	void reportCancelledRequest(final CancelSummary cancelSummary)
	{
		try {
			readWriteLock.lock();
			cancelLog.add(cancelSummary);
		} finally {
			readWriteLock.unlock();
		}
	}

	void reportExceptionThrownRequest(final ThrowableSummary throwableSummary)
	{
		try {
			readWriteLock.lock();
			exceptionLog.add(throwableSummary);
		} finally {
			readWriteLock.unlock();
		}
	}

	void reportRejectedRequest(final RejectSummary rejectSummary)
	{
		try {
			readWriteLock.lock();
			rejectLog.add(rejectSummary);
		} finally {
			readWriteLock.unlock();
		}
	}

	/**
	 * @param requestContainer the requestContainer to set
	 */
	void setThreadContainer(final RequestContainer requestContainer)
	{
		this.requestContainer = requestContainer;
	}
}
