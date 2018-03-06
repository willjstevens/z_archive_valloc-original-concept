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
public interface Bootstrapable extends CycleStageCallable
{
	public void bootstrap(); 
}
