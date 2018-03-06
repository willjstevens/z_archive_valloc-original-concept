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
public class InterruptHandlerAdapter implements InterruptHandler
{
	private InterruptTracker interruptTracker;

	public InterruptHandlerAdapter(final InterruptTracker interruptTracker) {
		this.interruptTracker = interruptTracker;
	}

	public InterruptHandlerAdapter(final InterruptParticipator interruptParticipatorSource) {
		this.interruptTracker = interruptParticipatorSource.getInterruptTracker();
	}

	/* (non-Javadoc)
	 * @see com.valloc.interrupt.InterruptParticipator#getInterruptTracker()
	 */
	@Override
	public InterruptTracker getInterruptTracker() {
		return interruptTracker;
	}

	/* (non-Javadoc)
	 * @see com.valloc.interrupt.InterruptParticipator#setInterruptTracker(com.valloc.interrupt.InterruptTracker)
	 */
	@Override
	public void setInterruptTracker(final InterruptTracker interruptTracker) {
		this.interruptTracker = interruptTracker;
	}

	/* (non-Javadoc)
	 * @see com.valloc.interrupt.InterruptHandler#handleInterrupt(com.valloc.interrupt.InterruptType, com.valloc.util.Result)
	 */
	@Override
	public void handleInterrupt(final InterruptType interruptType, final Result result) {
		throw new IllegalStateException("Override me.");
	}

}
