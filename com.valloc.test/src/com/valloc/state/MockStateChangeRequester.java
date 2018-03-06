/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import com.valloc.state.StateChangeRequester;
import com.valloc.state.StateChangeResponse;

/**
 *
 *
 * @author wstevens
 */
public final class MockStateChangeRequester implements StateChangeRequester
{
	private StateChangeResponse response;
	
	@Override
	public void requestCompleted(final StateChangeResponse response) 
	{
		this.response = response;
	}

	public StateChangeResponse getResponse()
	{
		return response;
	}
}