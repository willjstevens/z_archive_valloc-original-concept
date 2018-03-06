/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

/**
 * A simple interface for builder objects to implement.
 *
 * @author wstevens
 */
public interface Builder<T>
{
	/**
	 * Provides access for performing the final assembly and/or instantiation of the target build 
	 * object <code>T</code>.
	 * @return
	 */
	T build();
}