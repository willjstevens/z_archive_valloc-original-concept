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
public interface Destroyable extends CycleStageCallable
{
	public void destroy();
}
