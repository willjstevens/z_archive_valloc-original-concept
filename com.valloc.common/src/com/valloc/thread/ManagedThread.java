/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.thread;

import java.util.Date;

import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.util.KeyValueEntryMap;
import com.valloc.util.Util;

/**
 * 
 * 
 * @author wstevens
 */
public class ManagedThread extends Thread
{
	private static final Logger logger = LogManager.manager().getLogger(ManagedThread.class, CategoryType.UTILITY);
	private final ThreadManager threadManager;
	private final ThreadCategory threadCategory;
	private final Date creationTimestamp;
	private Date startTimestamp;
	private Date runCompleteTimestamp;
	private Date interruptTimestamp;

	ManagedThread(final Runnable target, final String name, final ThreadCategory threadCategory, final ThreadManager threadManager) {
		super(target, name);
		this.threadCategory = threadCategory;
		this.threadManager = threadManager;
		creationTimestamp = Util.nowTimestamp();
	}

	public ThreadCategory getThreadCategory() {
		return threadCategory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		startTimestamp = Util.nowTimestamp();
		super.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			super.run();
		} catch (final Throwable t) {
			logger.error(String.format("Caught thrown in wrapped runnable thread %s with reason %s.", this, t.getMessage()), t);
			threadManager.reportThrowable(this, t);
		} finally {
			threadManager.removeThread(this);
			runCompleteTimestamp = Util.nowTimestamp();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		super.interrupt();
		threadManager.reportInterrupted(this);
		interruptTimestamp = Util.nowTimestamp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((int) getId()); // truncating here taking least most significant bits
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ManagedThread)) {
			return false;
		}
		final ManagedThread other = (ManagedThread) obj;

		final long otherId = other.getId();
		final long thisId = this.getId();
		if (thisId != otherId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final KeyValueEntryMap entryMap = new KeyValueEntryMap();
		entryMap.add("id", getId());
		entryMap.add("name", getName());
		entryMap.add("state", getState());
		entryMap.add("threadCategory", threadCategory);
		entryMap.add("priority", getPriority());
		return entryMap.toString();
	}

	/**
	 * @return the creationTimestamp
	 */
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	/**
	 * @return the startTimestamp
	 */
	public Date getStartTimestamp() {
		return startTimestamp;
	}

	/**
	 * @return the runCompleteTimestamp
	 */
	public Date getRunCompleteTimestamp() {
		return runCompleteTimestamp;
	}

	/**
	 * @return the interruptTimestamp
	 */
	public Date getInterruptTimestamp() {
		return interruptTimestamp;
	}
}
