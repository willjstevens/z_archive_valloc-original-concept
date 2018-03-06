/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import com.valloc.MessageSummary;
import com.valloc.lifecycle.LifecycleState;

/**
 *
 *
 * @author wstevens
 */
public interface StateMachine
{
	public void requestStateChange(StateChangeRequest request);
	public LifecycleState getActiveState();
	public StateFactory getStateFactory();
	public void setStateChangeMessageSummary(MessageSummary stateChangeMessageSummary);
	public void addStateChangeListener(StateChangeListener listener);
	public void removeStateChangeListener(StateChangeListener listener);
}
