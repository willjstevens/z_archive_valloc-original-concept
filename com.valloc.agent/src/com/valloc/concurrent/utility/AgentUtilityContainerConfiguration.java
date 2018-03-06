/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;





/**
 *
 *
 * @author wstevens
 */
public class AgentUtilityContainerConfiguration extends UtilityContainerConfiguration
{
	
	public static final int CORE_POOL_SIZE_DEFAULT					= 10;
	public static final int CORE_POOL_SIZE_MIN						= 5;
	public static final int CORE_POOL_SIZE_MAX						= 20;

	public static final int SHUTDOWN_AWAIT_SECONDS_DEFAULT			= 20;
	public static final int SHUTDOWN_AWAIT_SECONDS_MIN				= 5;
	public static final int SHUTDOWN_AWAIT_SECONDS_MAX				= 30;
	
	public static final boolean DO_AWAIT_SHUTDOWN_DEFAULT			= true;
	
	public AgentUtilityContainerConfiguration()
	{
		corePoolSize = CORE_POOL_SIZE_DEFAULT;
		shutdownAwaitInSeconds = SHUTDOWN_AWAIT_SECONDS_DEFAULT;
		doAwaitShutdown = DO_AWAIT_SHUTDOWN_DEFAULT;
	}
}
