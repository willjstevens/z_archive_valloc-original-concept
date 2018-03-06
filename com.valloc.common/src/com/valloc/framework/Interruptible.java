/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.interrupt.InterruptEscapeException;
import com.valloc.interrupt.InterruptType;

/**
 *
 *
 * @author wstevens
 */
@Deprecated
public interface Interruptible
{
	/* Called by outside component; common implementation probably done by parent class. */
	public void requestInterrupt(InterruptType interruptType);
	/* Invoked by derived class to easily check status and escape if needbe. */ 
	public void checkInterruption() throws InterruptEscapeException;
	/* Implemented by derived class to handle specific logic related to cancelling. */
	public abstract void handleInterrupt();
	/* Query methods, implemented by parent. */
	public boolean isSubjectInterrupted();
	public InterruptType getInterruptionType();
}
