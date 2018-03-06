/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 *
 * @author wstevens
 */
public class ThreadManagerSnapshot
{
	private final Date snapshotTimestamp;
	private final EnumMap<ThreadCategory, Set<ThreadSummary>> managedThreads = new EnumMap<ThreadCategory, Set<ThreadSummary>>(ThreadCategory.class);
	private final List<InterruptionSummary> interruptionLog = new ArrayList<InterruptionSummary>();
	private final List<ExceptionSummary> exceptionLog = new ArrayList<ExceptionSummary>();
	
	/**
	 * @param snapshotTimestamp
	 */
	ThreadManagerSnapshot(final Date snapshotTimestamp)
	{
		this.snapshotTimestamp = snapshotTimestamp;
	}

	/**
	 * 
	 * @param exceptionSummaries
	 */
	public void addAllExceptionSummaries(final List<ExceptionSummary> exceptionSummaries)
	{
		exceptionLog.addAll(exceptionSummaries);
	}

	/**
	 * 
	 * @param interruptionSummaries
	 */
	public void addAllInterruptionSummaries(final List<InterruptionSummary> interruptionSummaries)
	{
		interruptionLog.addAll(interruptionSummaries);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.EnumMap#put(java.lang.Enum, java.lang.Object)
	 */
	public void addThreadCategory(final ThreadCategory key)
	{
		final Set<ThreadSummary> categoryThreads = new HashSet<ThreadSummary>();
		managedThreads.put(key, categoryThreads);
	}
	
	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.EnumMap#put(java.lang.Enum, java.lang.Object)
	 */
	public void addCategoryThread(final ThreadCategory threadCategory, final ThreadSummary managedThread)
	{
		final Set<ThreadSummary> categoryThreads = managedThreads.get(threadCategory);
		categoryThreads.add(managedThread);
	}

	/**
	 * @return the managedthreads
	 */
	public EnumMap<ThreadCategory, Set<ThreadSummary>> getManagedthreads()
	{
		return managedThreads;
	}

	/**
	 * @return the interruptionlog
	 */
	public List<InterruptionSummary> getInterruptionlog()
	{
		return interruptionLog;
	}

	/**
	 * @return the exceptionlog
	 */
	public List<ExceptionSummary> getExceptionlog()
	{
		return exceptionLog;
	}

	/**
	 * @return the snapshotTimestamp
	 */
	public Date getSnapshotTimestamp()
	{
		return snapshotTimestamp;
	}
}
