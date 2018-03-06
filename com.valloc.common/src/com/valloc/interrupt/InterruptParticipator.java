/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.interrupt;


/**
 *
 *
 * @author wstevens
 */
public interface InterruptParticipator
{
	public InterruptTracker getInterruptTracker();
	public void setInterruptTracker(InterruptTracker interruptTracker);
}
