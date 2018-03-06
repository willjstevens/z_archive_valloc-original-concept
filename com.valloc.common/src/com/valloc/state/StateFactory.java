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
public interface StateFactory
{
	public State getStateObject(LifecycleState desiredState, StateMachine stateMachine);

	public State getUnassignedState();
	public State getBootstrappedState();
	public State getInitializedState();
	public State getActiveState();
	public State getSuspendedState();
	public State getStoppedState();
	public State getShutdownState();
	public State getDestroyedState();
	public State getKilledState();
	public State getTerminatedState();
}
