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
public enum LifecycleState
{
	UNASSIGNED,
	BOOTSTRAPPING,
	BOOTSTRAPPED,
	INITIALIZING,
	INITIALIZED,
	STARTING,
	ACTIVE,
	SUSPENDING,
	SUSPENDED,
	RESUMING,
	STOPPING,
	STOPPED,
	SHUTTING_DOWN,
	SHUTDOWN,
	DESTROYING,
	DESTROYED,
	KILLING,
	KILLED,
	TERMINATED;
}