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
public interface UtilityCompletionListener
{
	/**
	 * 
	 * @return boolean If it should continue to reschedule the next iteration.
	 */
	public void utilityComplete(ContainerExecutionResult result);
}
