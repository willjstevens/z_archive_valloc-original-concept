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
public abstract class AbstractStateFactory implements StateFactory
{
	/*
	 * (non-Javadoc)
	 * @see com.valloc.state.StateFactory#getStateObject(com.valloc.lifecycle.LifecycleState)
	 */
	@Override
	public State getStateObject(final LifecycleState resultingState, final StateMachine stateMachine)
	{
		State retval = null;
		
		switch (resultingState) {
		case UNASSIGNED: 	retval = getUnassignedState();		break;
		case BOOTSTRAPPED:	retval = getBootstrappedState();	break;
		case INITIALIZED: 	retval = getInitializedState();		break;
		case ACTIVE:		retval = getActiveState();			break;
		case SUSPENDED:		retval = getSuspendedState();		break;
		case STOPPED:		retval = getStoppedState();			break;
		case SHUTDOWN:		retval = getShutdownState();		break;
		case DESTROYED:		retval = getDestroyedState();		break;
		case KILLED:		retval = getKilledState();			break;
		case TERMINATED:	retval = getTerminatedState();
		}
		retval.setStateMachine(stateMachine);
		
		return retval;
	}
	
	// Overridden as desired:
	@Override
	public State getUnassignedState() 		{ return null; }
	@Override
	public State getBootstrappedState() 	{ return null; }
	@Override
	public State getInitializedState()		{ return null; }
	@Override
	public State getActiveState()			{ return null; }
	@Override
	public State getSuspendedState()		{ return null; }
	@Override
	public State getStoppedState()			{ return null; }
	@Override
	public State getShutdownState()			{ return null; }
	@Override
	public State getDestroyedState()		{ return null; }
	@Override
	public State getKilledState()			{ return null; }
	@Override
	public State getTerminatedState()		{ return null; }
}
