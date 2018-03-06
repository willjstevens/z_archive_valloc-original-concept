/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.spinner;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.valloc.ApplicationException;

/**
 * 
 * 
 * @author wstevens
 */
public final class ConditionTriggerSpinner extends AbstractSpinner implements TriggerSpinner
{
	private final Lock lock = new ReentrantLock();
	private final Condition waiter = lock.newCondition();

	/**
	 * @param spinHandler
	 */
	public ConditionTriggerSpinner(final SpinHandler spinHandler) {
		super(spinHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.spinner.AbstractSpinner#spinAndWait()
	 */
	@Override
	public void spinAndWait() throws RuntimeException {
		super.spinAndWait();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.spinner.AbstractSpinner#block()
	 */
	@Override
	void block() {
		lock.lock();
		try {
			waiter.await();
		} catch (final InterruptedException e) {
			throw new ApplicationException(e);
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.spinner.TriggerSpinner#pullTrigger()
	 */
	@Override
	public void pullTrigger() {
		lock.lock();
		try {
			waiter.signal(); // should only be 1 thread waiting - this thread
		} finally {
			lock.unlock();
		}
	}
}
