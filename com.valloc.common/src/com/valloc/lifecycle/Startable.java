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
public interface Startable extends CycleStageCallable
{
	public void start();
}
