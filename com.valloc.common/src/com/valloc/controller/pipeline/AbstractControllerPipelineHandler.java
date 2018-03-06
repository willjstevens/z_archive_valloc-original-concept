/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.interrupt.InterruptTracker;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractControllerPipelineHandler implements ControllerPipelineHandler
{
	private InterruptTracker interruptTracker;

	@Override
	public InterruptTracker getInterruptTracker() {
		return interruptTracker;
	}

	@Override
	public void setInterruptTracker(final InterruptTracker interruptTracker) {
		this.interruptTracker = interruptTracker;
	}
}
