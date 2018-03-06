/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;




/**
 *
 *
 * @author wstevens
 */
public class ServerRequestContainerConfiguration extends RequestContainerConfiguration
{
	
	public static final int CORE_POOL_SIZE_DEFAULT					= 10;
	public static final int CORE_POOL_SIZE_MIN						= 5;
	public static final int CORE_POOL_SIZE_MAX						= 20;
	
	public static final int MAX_POOL_SIZE_DEFAULT					= 25;
	public static final int MAX_POOL_SIZE_MIN						= 20;
	public static final int MAX_POOL_SIZE_MAX						= 40;
	
	public static final int QUEUE_BOUND_SIZE_DEFAULT				= 10;
	public static final int QUEUE_BOUND_SIZE_MIN					= 5;
	public static final int QUEUE_BOUND_SIZE_MAX					= 20;
	
	public static final int EXCESS_THREAD_TIMEOUT_SECONDS_DEFAULT	= 60;
	public static final int EXCESS_THREAD_TIMEOUT_SECONDS_MIN		= 30;
	public static final int EXCESS_THREAD_TIMEOUT_SECONDS_MAX		= 300;

	public static final int SHUTDOWN_AWAIT_SECONDS_DEFAULT			= 20;
	public static final int SHUTDOWN_AWAIT_SECONDS_MIN				= 5;
	public static final int SHUTDOWN_AWAIT_SECONDS_MAX				= 30;
	
	public static final boolean DO_AWAIT_SHUTDOWN_DEFAULT			= true;
	
	public ServerRequestContainerConfiguration()
	{
		corePoolSize = CORE_POOL_SIZE_DEFAULT;
		maxPoolSize = MAX_POOL_SIZE_DEFAULT;
		queueBoundSize = QUEUE_BOUND_SIZE_DEFAULT;
		excessThreadTimeoutInSeconds = EXCESS_THREAD_TIMEOUT_SECONDS_DEFAULT;
		shutdownAwaitInSeconds = SHUTDOWN_AWAIT_SECONDS_DEFAULT;
		doAwaitShutdown = DO_AWAIT_SHUTDOWN_DEFAULT;
	}
}
