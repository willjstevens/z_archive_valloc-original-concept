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
public interface BoundedQueueOverflowHandler<E>
{
	public void handleOverflowElement(E element);
}
