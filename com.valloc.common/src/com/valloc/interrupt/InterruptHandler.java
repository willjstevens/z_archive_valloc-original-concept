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
public interface InterruptHandler extends InterruptParticipator
{
	public void handleInterrupt(InterruptType interruptType, Result result);
}
