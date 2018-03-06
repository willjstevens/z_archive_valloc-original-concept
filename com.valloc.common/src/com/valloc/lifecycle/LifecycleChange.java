/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.lifecycle;

/**
 *
 *
 *
 * @author wstevens
 */
public enum LifecycleChange
{
	BOOTSTRAP	(LifecycleState.BOOTSTRAPPED),
	INITIALIZE	(LifecycleState.INITIALIZED),
	START		(LifecycleState.ACTIVE),
	SUSPEND		(LifecycleState.SUSPENDED),
	RESUME		(LifecycleState.ACTIVE),
	STOP		(LifecycleState.STOPPED),
	SHUTDOWN	(LifecycleState.SHUTDOWN),
	DESTROY		(LifecycleState.DESTROYED),
	KILL		(LifecycleState.KILLED);
	
	private LifecycleState targetState;
	
	private LifecycleChange(final LifecycleState targetState) {
		this.targetState = targetState;
	}

	public LifecycleState targetState() {
		return targetState;
	}
}