/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.spinner;

import java.util.concurrent.TimeUnit;

import com.valloc.ApplicationException;
import com.valloc.thread.Frequency;

/**
 *
 *
 * @author wstevens
 */
public final class TimeIntervalSpinner extends AbstractSpinner
{
	private final long frequencyInMillis;
	
	/**
	 * @param spinHandler
	 */
	public TimeIntervalSpinner(final SpinHandler spinHandler, final Frequency frequency)
	{
		super(spinHandler);
		frequencyInMillis = TimeUnit.SECONDS.toMillis(frequency.frequencyInSeconds());
	}

	/* (non-Javadoc)
	 * @see com.valloc.concurrent.spinner.AbstractSpinner#spin()
	 */
	@Override
	void block()
	{
		try {
			Thread.sleep(frequencyInMillis);
		} catch (final InterruptedException e) {
			throw new ApplicationException(e);
		}
	}
}
