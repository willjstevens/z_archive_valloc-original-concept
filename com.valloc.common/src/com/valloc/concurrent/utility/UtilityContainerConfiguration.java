/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import com.valloc.config.Configuration;

/**
 *
 *
 * @author wstevens
 */
public abstract class UtilityContainerConfiguration implements Configuration
{
	public int corePoolSize;
	public int shutdownAwaitInSeconds;
	public boolean doAwaitShutdown;
}
