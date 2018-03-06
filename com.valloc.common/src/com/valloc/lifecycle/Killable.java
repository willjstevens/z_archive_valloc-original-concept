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
public interface Killable extends CycleStageCallable
{
	public void kill();
}
