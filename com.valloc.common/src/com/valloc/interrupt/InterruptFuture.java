/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.interrupt;

import java.util.concurrent.CountDownLatch;

import com.valloc.Identifiable;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class InterruptFuture implements Identifiable<UniqueId>
{
	private final UniqueId id;
	private final CountDownLatch latch;
	private InterruptType interruptType;
	private Result result;

	public InterruptFuture(final UniqueId id, final CountDownLatch latch) {
		this.id = id;
		this.latch = latch;
	}

	@Override
	public UniqueId id() {
		return id;
	}

	public Result blockForResult() {
		try {
			latch.await();
			if (result == null) {
				throw new IllegalArgumentException();
			}
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public InterruptType getInterruptType() {
		return interruptType;
	}

	void setInterruptType(final InterruptType interruptType) {
		this.interruptType = interruptType;
	}

	public Result getResult() {
		return result;
	}

	void setResult(final Result result) {
		this.result = result;
	}
}
