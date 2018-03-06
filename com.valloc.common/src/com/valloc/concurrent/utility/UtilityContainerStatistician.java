/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
final class UtilityContainerStatistician
{	
	private final UtilityContainer utilityContainer;
	private boolean doCaptureCompletionLogDetails;
	private int completedRequestsCount;
	private int completedRequestSingleImmediate;
	private int completedRequestSingleScheduled;
	private int completedRequestRepeatingFixedScheduled;
	private int completedRequestRepeatingDelayedScheduled;
	private long completedRequestAverageDurationInMillis;
	private long highWatermarkCompletedRequestDuration;
	private final List<CompletionSummary> completionLog = new ArrayList<CompletionSummary>();
	private final List<CancelSummary> cancelLog = new ArrayList<CancelSummary>();
	private final List<ThrowableSummary> exceptionLog = new ArrayList<ThrowableSummary>();	
	private final List<RejectSummary> rejectLog = new ArrayList<RejectSummary>();
	private final Lock readWriteLock = new ReentrantLock(true);
	
	UtilityContainerStatistician(final UtilityContainer utilityContainer)
	{
		this.utilityContainer = utilityContainer;
	}

	public UtilityContainerSnapshot takeThreadContainerSnapshot()
	{
		UtilityContainerSnapshot snapshot = null;
		
		try {
			readWriteLock.lock();			
			snapshot = new UtilityContainerSnapshot(Util.nowTimestamp());
			snapshot.setTotalRequestsMade(utilityContainer.getTotalRequestsMade());
			snapshot.setCompletedRequestsCount(completedRequestsCount);
			snapshot.setCompletedRequestSingleImmediate(completedRequestSingleImmediate);
			snapshot.setCompletedRequestSingleScheduled(completedRequestSingleScheduled);
			snapshot.setCompletedRequestRepeatingDelayedScheduled(completedRequestRepeatingDelayedScheduled);
			snapshot.setCompletedRequestRepeatingFixedScheduled(completedRequestRepeatingFixedScheduled);
			snapshot.setCompletedRequestAverageDurationInMillis(completedRequestAverageDurationInMillis);
			snapshot.setHighWatermarkCompletedRequestDuration(highWatermarkCompletedRequestDuration);
			snapshot.setScheduledQueueSize(utilityContainer.getScheduledQueueSize());
			snapshot.setActiveRequestLog(utilityContainer.getActiveRequestLog());
			snapshot.setCompletionLog(completionLog);
			snapshot.setCancelLog(cancelLog);
			snapshot.setExceptionLog(exceptionLog);
			snapshot.setRejectLog(rejectLog);
			snapshot.setThreadPoolExecutorActiveCount(utilityContainer.getThreadPoolExecutorActiveCount());
			snapshot.setThreadPoolExecutorCompletedTaskCount(utilityContainer.getThreadPoolExecutorCompletedTaskCount());
			snapshot.setThreadPoolExecutorCorePoolSize(utilityContainer.getThreadPoolExecutorCorePoolSize());
			snapshot.setThreadPoolExecutorLargestPoolSize(utilityContainer.getThreadPoolExecutorLargestPoolSize());
			snapshot.setThreadPoolExecutorMaximumPoolSize(utilityContainer.getThreadPoolExecutorMaximumPoolSize());
			snapshot.setThreadPoolExecutorPoolSize(utilityContainer.getThreadPoolExecutorPoolSize());
			snapshot.setThreadPoolExecutorTaskCount(utilityContainer.getThreadPoolExecutorTaskCount());
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
			completedRequestSingleImmediate = 0;
			completedRequestSingleScheduled = 0;
			completedRequestRepeatingFixedScheduled = 0;
			completedRequestRepeatingDelayedScheduled = 0;
			completedRequestAverageDurationInMillis = 0;
			highWatermarkCompletedRequestDuration = 0;
			completionLog.clear();
			cancelLog.clear();
			exceptionLog.clear();
			rejectLog.clear();
		} finally {
			readWriteLock.unlock();
		}
	}
	
	void reportCompletedRequest(final CompletionSummary completionSummary)
	{
		try {
			readWriteLock.lock();
			
			if (doCaptureCompletionLogDetails) {			
				this.completionLog.add(completionSummary);
			}
			completedRequestsCount++; 

			switch (completionSummary.getUtilityType()) {
			case SINGLE_IMMEDIATE:				completedRequestSingleImmediate++;			break;
			case SINGLE_SCHEDULED:				completedRequestSingleScheduled++;			break;
			case REPEATING_FIXED_SCHEDULED:		completedRequestRepeatingFixedScheduled++; 	break;
			case REPEATING_DELAYED_SCHEDULED: 	completedRequestRepeatingDelayedScheduled++; 	
			}
					
			// reestablish a new normal request running time average
			final long start = completionSummary.getStartTimestamp().getTime();
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
	 * @param doCaptureCompletionLogDetails the doCaptureCompletionLogDetails to set
	 */
	void setDoCaptureCompletionLogDetails(final boolean doCaptureCompletionLogDetails)
	{
		this.doCaptureCompletionLogDetails = doCaptureCompletionLogDetails;
	}
	
	void flushCompletionLogDetails()
	{
		try {
			readWriteLock.lock();
			completionLog.clear();
		} finally {
			readWriteLock.unlock();
		}
	}
}
