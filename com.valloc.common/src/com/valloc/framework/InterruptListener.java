/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.Identifiable;
import com.valloc.interrupt.InterruptType;

/**
 *
 *
 * @author wstevens
 */
@Deprecated
public interface InterruptListener<T, I> extends Identifiable<I>
{
	public void onInterrupt(InterruptType interruptType, T object);
}
