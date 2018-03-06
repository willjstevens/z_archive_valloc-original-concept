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
public final class UnassignedState extends AbstractState
{
	private static final UnassignedState unassignedState = new UnassignedState();
	
	private UnassignedState() { super(); }
	
	public static State getUnassignedState()
	{
		return unassignedState;
	}
}
