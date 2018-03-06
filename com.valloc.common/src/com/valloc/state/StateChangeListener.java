/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import com.valloc.lifecycle.LifecycleState;

/**
 *
 *
 * @author wstevens
 */
public interface StateChangeListener
{
	public void stateChanged(LifecycleState newState);
}
