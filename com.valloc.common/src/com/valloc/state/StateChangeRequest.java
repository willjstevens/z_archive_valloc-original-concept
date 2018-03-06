/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import com.valloc.lifecycle.LifecycleChange;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class StateChangeRequest
{
	private final UniqueId id;
	private final LifecycleChange requestedChange;
	private final StateChangeRequester requester;
	
	/**
	 * @param id
	 * @param requestedChange
	 * @param requester
	 */
	public StateChangeRequest(final UniqueId id, final LifecycleChange requestedChange, final StateChangeRequester requester)
	{
		this.id = id;
		this.requestedChange = requestedChange;
		this.requester = requester;
	}

	/**
	 * @return the id
	 */
	public UniqueId getId()
	{
		return id;
	}

	/**
	 * @return the requestedChange
	 */
	public LifecycleChange getRequestedChange()
	{
		return requestedChange;
	}

	/**
	 * @return the requester
	 */
	public StateChangeRequester getRequester()
	{
		return requester;
	}
}
