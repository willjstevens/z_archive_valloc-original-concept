/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.interrupt.InterruptTracker;

/**
 *
 *
 * @author wstevens
 */
public interface DesktopFrameworkManager extends FrameworkManager
{
	public InterruptTracker createAndRegisterInterruptTracker();
}
