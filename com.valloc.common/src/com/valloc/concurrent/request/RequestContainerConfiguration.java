/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import com.valloc.config.Configuration;

/**
 *
 *
 * @author wstevens
 */
public abstract class RequestContainerConfiguration implements Configuration
{
	public int corePoolSize;
	public int maxPoolSize;
	public int queueBoundSize;
	public int excessThreadTimeoutInSeconds;
	public int shutdownAwaitInSeconds;
	public boolean doAwaitShutdown;
}
