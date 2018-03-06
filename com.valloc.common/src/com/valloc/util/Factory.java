/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

/**
 * 
 *
 * @author wstevens
 */
public interface Factory<T>
{
	public T newInstance();
}
