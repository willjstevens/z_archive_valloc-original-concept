/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.spinner;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.ApplicationException;
import com.valloc.concurrent.spinner.ConditionTriggerSpinner;
import com.valloc.concurrent.spinner.SpinHandler;
import com.valloc.concurrent.spinner.Spinner;
import com.valloc.concurrent.spinner.TimeIntervalSpinner;
import com.valloc.concurrent.spinner.TriggerSpinner;
import com.valloc.thread.Frequency;

/**
 * 
 *
 * @author wstevens
 */
public final class SpinnerTest extends AbstractTest
{	
	/**
	 * Test basic handler invokation count of 3 iterations, 3 seconds a piece.
	 */
	@Test
	public void timeIntervalSpinner_basic()
	{
		final int loopAmount = 3;
		final AtomicInteger count = new AtomicInteger();
		final SpinHandler handler = new SpinHandler() {
			@Override
			public void handleSpinIteration(final Spinner spinner) {
				final int currentCount = count.incrementAndGet();
				if (currentCount == loopAmount) {
					spinner.stop();
				}
			}
		};
		final Frequency frequency = Frequency.SECONDS_03;
		final Spinner spinner = new TimeIntervalSpinner(handler, frequency);
		
		spinner.spinAndWait();
		final int expected = loopAmount;
		final int actual = count.intValue();
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test error handling with premature completion.
	 */
	@Test
	public void timeIntervalSpinner_error()
	{
		final int loopAmount = 4;
		final int errorIteration = 2;
		final AtomicInteger count = new AtomicInteger();
		final SpinHandler handler = new SpinHandler() {
			@Override
			public void handleSpinIteration(final Spinner spinner) {
				final int currentCount = count.incrementAndGet();
				if (currentCount == errorIteration) {
					throw new RuntimeException("Surprise!");
				}
				if (currentCount == loopAmount) {
					spinner.stop();
				}
			}
		};
		final Frequency frequency = Frequency.SECONDS_03;
		final Spinner spinner = new TimeIntervalSpinner(handler, frequency);
		
		try {
			spinner.spinAndWait();
		} catch (final ApplicationException e) { /* anticipated, swallow */ }
		final int expected = errorIteration;
		final int actual = count.intValue();
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * Test basic trigger spinner for 1 iteration.
	 */
	@Test
	public void conditionSpinner_basic_1()
	{
		final AtomicBoolean isSet = new AtomicBoolean();
		final SpinHandler handler = new SpinHandler() {
			@Override
			public void handleSpinIteration(final Spinner spinner) {
				isSet.set(true);
				spinner.stop();
			}
		};		
		final TriggerSpinner spinner = new ConditionTriggerSpinner(handler);
		final Runnable triggerPuller = new Runnable() {
			@Override
			public void run() {
				// wait 2 for thread to fire and spinner to get situated on block()
				try {
					Thread.sleep(2000);
					spinner.pullTrigger();
				} catch (final InterruptedException e) {
					printThrowableAndFail(e);
				} 
			}
		}; 
		new Thread(triggerPuller).start();		
		spinner.spinAndWait();		
		Assert.assertTrue(isSet.get());
	}

	/**
	 * Test basic trigger spinner for 5 iterations.
	 */
	@Test
	public void conditionSpinner_basic_2()
	{
		final int loopAmount = 5;
		final AtomicInteger count = new AtomicInteger();
		final SpinHandler handler = new SpinHandler() {
			@Override
			public void handleSpinIteration(final Spinner spinner) {
				final int currentCount = count.incrementAndGet();
				if (currentCount == loopAmount) {
					spinner.stop();
				}
			}
		};		
		final TriggerSpinner spinner = new ConditionTriggerSpinner(handler);		
		final Runnable triggerPuller = new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < loopAmount; i++) {
						Thread.sleep(1000); // wait 2 for thread to fire and spinner to get situated on block()
						spinner.pullTrigger();
					}
				} catch (final InterruptedException e) {
					printThrowableAndFail(e);
				} 
			}
		}; 
		new Thread(triggerPuller).start();	
		spinner.spinAndWait();		
		final int expected = loopAmount;
		final int actual = count.intValue();
		Assert.assertEquals(expected, actual);
	}
		
	/**
	 * Test trigger spinner with error causing premature escape.
	 */
	@Test
	public void conditionSpinner_error()
	{
		final int loopAmount = 5;
		final int errorIteration = 2;
		final AtomicInteger count = new AtomicInteger();
		final SpinHandler handler = new SpinHandler() {
			@Override
			public void handleSpinIteration(final Spinner spinner) {
				final int currentCount = count.incrementAndGet();
				if (currentCount == errorIteration) {
					throw new RuntimeException("Surprise!");
				}
				if (currentCount == loopAmount) {
					spinner.stop();
				}
			}
		};		
		final TriggerSpinner spinner = new ConditionTriggerSpinner(handler);		
		final Runnable triggerPuller = new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < loopAmount; i++) {
						Thread.sleep(1000); // wait 2 for thread to fire and spinner to get situated on block()
						spinner.pullTrigger();
					}
				} catch (final InterruptedException e) {
					printThrowableAndFail(e);
				} 
			}
		}; 
		new Thread(triggerPuller).start();	

		try {
			spinner.spinAndWait();
		} catch (final ApplicationException e) { /* anticipated, swallow */ }
		final int expected = errorIteration;
		final int actual = count.intValue();
		Assert.assertEquals(expected, actual);
	}
}