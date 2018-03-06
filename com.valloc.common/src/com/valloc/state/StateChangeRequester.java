/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

/**
 *
 *
 * @author wstevens
 */
public interface StateChangeRequester
{
	public void requestCompleted(StateChangeResponse response);
}
