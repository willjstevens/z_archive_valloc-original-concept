/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.thread;

import java.lang.Thread.State;
import java.util.Date;

/**
 *
 *
 * @author wstevens
 */
public final class ThreadSummary
{
	private final long tid;
	private final String name;
	private final int priority;
	private final boolean isAlive;
	private final boolean isInterrupted;
	private final Thread.State state;
	private final Date creationTimestamp;
	private Date startTimestamp;
	private Date runCompleteTimestamp;
	private Date interruptTimestamp;
	
	/**
	 * @param tid
	 * @param name
	 * @param priority
	 * @param isInterrupted
	 * @param state
	 */
	ThreadSummary(final long tid, final String name, final int priority, final boolean isAlive, final boolean isInterrupted, final State state, final Date creationTimestamp)
	{
		this.tid = tid;
		this.name = name;
		this.priority = priority;
		this.isAlive = isAlive;
		this.isInterrupted = isInterrupted;
		this.state = state;
		this.creationTimestamp = creationTimestamp;
	}

	/**
	 * @return the tid
	 */
	public long getTid()
	{
		return tid;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the priority
	 */
	public int getPriority()
	{
		return priority;
	}

	/**
	 * @return the isAlive
	 */
	public boolean isAlive()
	{
		return isAlive;
	}

	/**
	 * @return the isInterrupted
	 */
	public boolean isInterrupted()
	{
		return isInterrupted;
	}

	/**
	 * @return the state
	 */
	public Thread.State getState()
	{
		return state;
	}

	/**
	 * @return the startTimestamp
	 */
	public Date getStartTimestamp()
	{
		return startTimestamp;
	}

	/**
	 * @param startTimestamp the startTimestamp to set
	 */
	public void setStartTimestamp(final Date startTimestamp)
	{
		this.startTimestamp = startTimestamp;
	}

	/**
	 * @return the runCompleteTimestamp
	 */
	public Date getRunCompleteTimestamp()
	{
		return runCompleteTimestamp;
	}

	/**
	 * @param runCompleteTimestamp the runCompleteTimestamp to set
	 */
	public void setRunCompleteTimestamp(final Date runCompleteTimestamp)
	{
		this.runCompleteTimestamp = runCompleteTimestamp;
	}

	/**
	 * @return the interruptTimestamp
	 */
	public Date getInterruptTimestamp()
	{
		return interruptTimestamp;
	}

	/**
	 * @param interruptTimestamp the interruptTimestamp to set
	 */
	public void setInterruptTimestamp(final Date interruptTimestamp)
	{
		this.interruptTimestamp = interruptTimestamp;
	}

	/**
	 * @return the creationTimestamp
	 */
	public Date getCreationTimestamp()
	{
		return creationTimestamp;
	}
}
