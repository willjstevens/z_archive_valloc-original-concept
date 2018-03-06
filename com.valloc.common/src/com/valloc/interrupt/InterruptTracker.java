/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.interrupt;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.valloc.Identifiable;
import com.valloc.MessageSummaryStatus;
import com.valloc.framework.FrameworkManager;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class InterruptTracker implements Identifiable<UniqueId>
{
	private final UniqueId id;
	private final CountDownLatch latch;
	private final FrameworkManager frameworkManager;
	private final Set<InterruptHandler> interruptHandlers = new HashSet<InterruptHandler>();
	private final InterruptFuture future;
	private InterruptType interruptType;
	private InterruptCompletionListener completionListener;

	// defaults to this; but can be reset on joinable interrupt
	private Result result = new Result(MessageSummaryStatus.INTERRUPT);
	private boolean isJoinableInterrupt;
	private boolean isJoinedResultRealized;
	private Lock joinableInterruptLock;
	private Condition joinableInterruptCondition;

	public InterruptTracker(final UniqueId id, final FrameworkManager frameworkManager) {
		this.id = id;
		this.frameworkManager = frameworkManager;
		latch = new CountDownLatch(1);
		future = new InterruptFuture(id, latch);
	}

	@Override
	public UniqueId id() {
		return id;
	}

	public void markIsJoinableInterrupt() {
		joinableInterruptLock = new ReentrantLock();
		joinableInterruptCondition = joinableInterruptLock.newCondition();
		isJoinableInterrupt = true;
	}

	public boolean isJoinableInterrupt() {
		return isJoinableInterrupt;
	}

	public void blockForInterruptJoin() {
		if (!isJoinableInterrupt) {
			throw new IllegalStateException("Cannot block on an interrupt that is not marked for joinable interrupt.");
		}

		if (!isJoinedResultRealized) { // check before querying the lock..
			try {
				joinableInterruptLock.lock();
				// check once more after acquiring lock to see if last object user didn't change it
				if (!isJoinedResultRealized) {
					joinableInterruptCondition.awaitUninterruptibly();
				}
			} finally {
				joinableInterruptLock.unlock();
			}
		}
	}

	public void interruptJoinRealizedAndRelease(final Result result) {
		if (isJoinableInterrupt) {
			try {
				joinableInterruptLock.lock();
				this.result = result;
				this.isJoinedResultRealized = true;
				joinableInterruptCondition.signal();
			} finally {
				joinableInterruptLock.unlock();
			}
		}
	}

	public InterruptFuture requestInterrupt(final InterruptType interruptType, final InterruptCompletionListener completionListener) {
		this.interruptType = interruptType;
		this.completionListener = completionListener; // could be null
		return future;
	}

	public void checkInterrupt() throws InterruptEscapeException {
		if (interrupted()) {
			// step 1: cycle through all interrupt interruptHandlers and notify
			for (final InterruptHandler handler : interruptHandlers) {
				handler.handleInterrupt(interruptType, result);
			}
			// step 2: call completion listener if set
			if (completionListener != null) { // check since this optional
				completionListener.onCompletion(interruptType, result);
			}
			// set result back into tracker
			future.setResult(result);
			// step 3: notify anything blocking on spinner; this reference is used
			//		in the future object
			latch.countDown();
			// step 4: notify framework manager to remove the tracked interrupt tracker object
			frameworkManager.interruptionComplete(id);
			// step 5: now that all interrupts are accomplished, throw escape exception
			throw new InterruptEscapeException();
		}
	}

	public void subscribeInterrputHandler(final InterruptHandler handler) {
		interruptHandlers.add(handler);
	}

	public void unsubscribeInterrputHandler(final InterruptHandler handler) {
		interruptHandlers.remove(handler);
	}

	public InterruptType getInterruptType() {
		return interruptType;
	}

	public boolean interrupted() {
		return interruptType != null;
	}

	public InterruptFuture getFuture() {
		if (future == null) {
			throw new IllegalStateException("Future has not been set yet; perhaps no interrupt has occurred.");
		}
		return future;
	}

	@Override
	public String toString() {
		return id.toString();
	}
	
	
}
