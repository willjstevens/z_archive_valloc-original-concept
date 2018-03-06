/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import com.valloc.MessageSummary;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class StateChangeResponse
{
	private final UniqueId id;
	private final LifecycleState resultingState;
	private final MessageSummary messageSummary;
	private final boolean didStateChange;

	/**
	 * @param id
	 * @param resultingState
	 * @param didStateChange
	 */
	public StateChangeResponse(final UniqueId id, final LifecycleState resultingState, final MessageSummary messageSummary, final boolean didStateChange)
	{
		this.id = id;
		this.resultingState = resultingState;
		this.messageSummary = messageSummary;
		this.didStateChange = didStateChange;
	}
	
	
	/**
	 * @return the id
	 */
	public UniqueId getId()
	{
		return id;
	}

	/**
	 * @return the resultingState
	 */
	public LifecycleState getResultingState()
	{
		return resultingState;
	}

	/**
	 * @return the messageSummary
	 */
	public MessageSummary getMessageSummary()
	{
		return messageSummary;
	}

	/**
	 * @return the didStateChange
	 */
	public boolean didStateChange()
	{
		return didStateChange;
	}
}
