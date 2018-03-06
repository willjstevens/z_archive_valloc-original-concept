/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.interrupt;

import com.valloc.util.Result;

/**
 *
 *
 * @author wstevens
 */
public interface InterruptCompletionListener
{
	public void onCompletion(InterruptType interruptType, Result result);
}
