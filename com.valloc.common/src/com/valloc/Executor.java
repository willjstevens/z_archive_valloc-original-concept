/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

/**
 * A generic interface for anything which will be executed; similar to <code>Thread.run</code> but
 * even more generic with regards it is not correlated with multithreading. 
 *
 * @author wstevens
 */
public interface Executor 
{
	/**
	 * The base method to execute on any implemention of this interface. 
	 */
	public void execute();		
}