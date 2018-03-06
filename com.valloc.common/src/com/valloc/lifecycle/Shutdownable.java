/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.lifecycle;

/**
 * 
 *
 * @author wstevens
 */
public interface Shutdownable extends CycleStageCallable
{
	public void shutdown();
}
