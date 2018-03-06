/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.thread;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.thread.BaseThreadManager;
import com.valloc.thread.ExceptionSummary;
import com.valloc.thread.InterruptionSummary;
import com.valloc.thread.ManagedThread;
import com.valloc.thread.ThreadCategory;
import com.valloc.thread.ThreadManager;
import com.valloc.thread.ThreadManagerSnapshot;
import com.valloc.thread.ThreadSummary;
import com.valloc.util.Util;

/**
 * 
 *
 * @author wstevens
 */
public final class ThreadManagerTest extends AbstractTest
{
	private static ThreadManager threadManager;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		threadManager = BaseThreadManager.getInstance();
	}

	@Before
	public void setUp() throws Exception
	{
		threadManager.purge();
	}

	@After
	public void tearDown() throws Exception
	{
	}
	
	/**
	 * Test basic functionality and usage of thread manager.
	 */
	@Test
	public void threadManager_basic_newThread()
	{				
		final ManagedThread thread1 = threadManager.newThread(new Sleeper(1), ThreadCategory.FRAMEWORK_REQUEST, "New Thread: 1 Second");		
		final ThreadManagerSnapshot snapshot1 = threadManager.takeThreadManagerSnapshot();		
		final EnumMap<ThreadCategory, Set<ThreadSummary>> threadCategories = snapshot1.getManagedthreads();
		final Set<ThreadSummary> frameworkRequestThreads = threadCategories.get(ThreadCategory.FRAMEWORK_REQUEST);
		final ThreadSummary threadSummary = frameworkRequestThreads.iterator().next();
		
		Assert.assertEquals(1, threadCategories.size());
		Assert.assertEquals(1, frameworkRequestThreads.size());
		Assert.assertEquals(thread1.getId(), threadSummary.getTid());
		Assert.assertEquals(Thread.State.NEW, threadSummary.getState());
	}
	
	/**
	 * Test basic functionality and usage of thread manager.
	 */
	@Test
	public void threadManager_basic_runningThread()
	{
		final ManagedThread thread1 = threadManager.newThread(new Sleeper(3), ThreadCategory.FRAMEWORK_REQUEST, "Running Thread: 3 Second");
		thread1.start();		
		final ThreadManagerSnapshot snapshot1 = threadManager.takeThreadManagerSnapshot();
		
		final EnumMap<ThreadCategory, Set<ThreadSummary>> threadCategories = snapshot1.getManagedthreads();
		final Set<ThreadSummary> frameworkRequestThreads = threadCategories.get(ThreadCategory.FRAMEWORK_REQUEST);
		final ThreadSummary threadSummary = frameworkRequestThreads.iterator().next();
		
		Assert.assertEquals(1, threadCategories.size());
		Assert.assertEquals(1, frameworkRequestThreads.size());
		Assert.assertEquals(thread1.getId(), threadSummary.getTid());
		Assert.assertEquals(Thread.State.RUNNABLE, threadSummary.getState());
	}
	
	/**
	 * Test basic functionality and usage of an interrupted thread being handled correctly.
	 */
	@Test
	public void threadManager_basic_interruptedThread()
	{
		final ManagedThread thread1 = threadManager.newThread(new Sleeper(4), ThreadCategory.FRAMEWORK_REQUEST, "Interrupted Thread: 1 Second");
		thread1.start();
		Util.quietSleep(1000);
		// we interrupt here as 'outsider' just as something like request container might do
		thread1.interrupt(); 
		Util.quietSleep(1000); // allow time to have thread remove itself from registry and add to log
		
		final ThreadManagerSnapshot snapshot = threadManager.takeThreadManagerSnapshot();
		final List<InterruptionSummary> interruptions = snapshot.getInterruptionlog();
		Assert.assertEquals(1, interruptions.size()); // verify it's logged
		Assert.assertEquals(0, snapshot.getManagedthreads().size()); // verify the interruption was cleaned up
	}
	
	/**
	 * Test basic functionality and usage of an exception-throwing thread being handled correctly.
	 */
	@Test
	public void threadManager_basic_throwExceptionThread()
	{
		final ManagedThread thread1 = threadManager.newThread(new ExceptionThrower(2), ThreadCategory.FRAMEWORK_REQUEST, "Exception Thread: 2 Second");
		thread1.start();		 
		Util.quietSleep(4000); // allow time to have thread remove itself from registry and add to log
		
		final ThreadManagerSnapshot snapshot = threadManager.takeThreadManagerSnapshot();
		final List<ExceptionSummary> exceptions = snapshot.getExceptionlog();
		Assert.assertEquals(1, exceptions.size()); // verify it's logged
		Assert.assertEquals(0, snapshot.getManagedthreads().size()); // verify the interruption was cleaned up
	}
		
	/**
	 * Test elaborate thread creatation and usage within the manager and that snapshots are capturing state correctly.
	 * This also demonstrates resources in the manager are being reclaimed as expected.
	 */
	@Test
	public void threadManager_elaborate()
	{
		// these 2 should never start but should be registered in the manager
		threadManager.newThread(new Sleeper(1), ThreadCategory.FRAMEWORK_REQUEST, "New Thread #1; Not ever executed");		
		threadManager.newThread(new Sleeper(6), ThreadCategory.FRAMEWORK_REQUEST, "New Thread #2; Not ever executed");
		// these 2 will start and be long-running for first snapshot to safely occur with them in a running status
		final ManagedThread frameworkRequest_running1 = threadManager.newThread(new Sleeper(12), ThreadCategory.FRAMEWORK_REQUEST, "Running Thread #1: 12 seconds");
		frameworkRequest_running1.start();
		final ManagedThread frameworkRequest_running2 = threadManager.newThread(new Sleeper(10), ThreadCategory.FRAMEWORK_REQUEST, "Running Thread #2: 10 seconds");
		frameworkRequest_running2.start();
		// this will immediately run and complete in 1 seconds so it's removed and can later verify it is NOT present in the manager (or not be counted)
		final ManagedThread frameworkRequest_completed1 = threadManager.newThread(new Sleeper(0), ThreadCategory.FRAMEWORK_REQUEST, "Completed Thread #1: 0 seconds");
		frameworkRequest_completed1.start();
		Util.quietSleep(1000); // time for completed thread to complete 
		final ThreadManagerSnapshot snapshot1 = threadManager.takeThreadManagerSnapshot();
		
		// these are created and executed here, but not interrupted or with exception until later 
		final ManagedThread frameworkRequest_interrupted1 = threadManager.newThread(new Sleeper(20), ThreadCategory.FRAMEWORK_REQUEST, "Interruption Thread #1: 20 seconds");
		frameworkRequest_interrupted1.start();
		final ManagedThread frameworkRequest_interrupted2 = threadManager.newThread(new Sleeper(20), ThreadCategory.FRAMEWORK_REQUEST, "Interruption Thread #2: 20 seconds");
		frameworkRequest_interrupted2.start();
		final ManagedThread frameworkRequest_interrupted3 = threadManager.newThread(new Sleeper(20), ThreadCategory.FRAMEWORK_REQUEST, "Interruption Thread #3: 20 seconds");
		frameworkRequest_interrupted3.start();
		final ManagedThread frameworkRequest_exceptioner1 = threadManager.newThread(new ExceptionThrower(23), ThreadCategory.FRAMEWORK_REQUEST, "Exception Thread #1: 23 seconds");
		frameworkRequest_exceptioner1.start();
		final ManagedThread frameworkRequest_exceptioner2 = threadManager.newThread(new ExceptionThrower(22), ThreadCategory.FRAMEWORK_REQUEST, "Exception Thread #2: 22 seconds");
		frameworkRequest_exceptioner2.start();
		final ThreadManagerSnapshot snapshot2 = threadManager.takeThreadManagerSnapshot();
		
		// these are provided for basic threads falling under alternative thread categories
		final ManagedThread completionService = threadManager.newThread(new Sleeper(10), ThreadCategory.FRAMEWORK_REQUEST_COMPLETION_SERVICE, "Alternative Category Thread (completion service) #1: 10 seconds");
		completionService.start();
		final ManagedThread backgroundUtil = threadManager.newThread(new Sleeper(10), ThreadCategory.BACKGROUND_UTILITY, "Alternative Category Thread (background utility) #2: 10 seconds");
		backgroundUtil.start();
		final ManagedThread queueService = threadManager.newThread(new Sleeper(10), ThreadCategory.QUEUE_MESSAGE_SPINNER, "Alternative Category Thread (queue message) #3: 10 seconds");
		queueService.start();
		final ThreadManagerSnapshot snapshot3 = threadManager.takeThreadManagerSnapshot();

		// first sleep for 12 seonds allowing '10 second batch' to complete for snapshot
		Util.quietSleep(12000);
		final ThreadManagerSnapshot snapshot4 = threadManager.takeThreadManagerSnapshot();
		
		// now we interrupt target threads from 'outside' just as a ThreadPoolExecutor would on threads, before completion
		frameworkRequest_interrupted1.interrupt();
		frameworkRequest_interrupted2.interrupt();
		frameworkRequest_interrupted3.interrupt();
		
		// now sleep for 17 seconds to allow for interruptions and exceptions to finish
		Util.quietSleep(17000);
		final ThreadManagerSnapshot snapshot5 = threadManager.takeThreadManagerSnapshot();
		
		// test 1st snapshot
		EnumMap<ThreadCategory, Set<ThreadSummary>> catThreads = snapshot1.getManagedthreads();
		Set<ThreadSummary> frameworkReqThreads = catThreads.get(ThreadCategory.FRAMEWORK_REQUEST);
		Assert.assertEquals(1, catThreads.size()); // only 1 category should be existant at this time
		Assert.assertEquals(4, frameworkReqThreads.size()); // 2 new threads and 2 long-running sleepers == 4 expected;
		
		// test 2nd snapshot
		catThreads = snapshot2.getManagedthreads();
		frameworkReqThreads = catThreads.get(ThreadCategory.FRAMEWORK_REQUEST);
		// 9 threads justified by:
		// 		- 2 new threads, never executed 
		//		- 2 10-second running threads [frameworkRequest_running1, frameworkRequest_running2]
		//		- 3 20-second running threads [frameworkRequest_interrupted1, frameworkRequest_interrupted2, frameworkRequest_interrupted3]
		//		- 2 ~22-second running threads [frameworkRequest_exceptioner1, frameworkRequest_exceptioner2]
		Assert.assertEquals(9, frameworkReqThreads.size());
		
		// test 3rd snapshot
		catThreads = snapshot3.getManagedthreads();
		frameworkReqThreads = catThreads.get(ThreadCategory.FRAMEWORK_REQUEST);
		Assert.assertEquals(4, catThreads.size()); // now we should have a total of 4 category threads
		// 12 threads justified by:
		// 		- 9 from above (from snapshot 2) 
		//		- 3 additional from the 3 other categories
		Assert.assertEquals(9, frameworkReqThreads.size());
		Assert.assertEquals(1, catThreads.get(ThreadCategory.FRAMEWORK_REQUEST_COMPLETION_SERVICE).size());
		Assert.assertEquals(1, catThreads.get(ThreadCategory.BACKGROUND_UTILITY).size());
		Assert.assertEquals(1, catThreads.get(ThreadCategory.QUEUE_MESSAGE_SPINNER).size());
		
		// test 4th snapshot
		catThreads = snapshot4.getManagedthreads();
		frameworkReqThreads = catThreads.get(ThreadCategory.FRAMEWORK_REQUEST);
		Assert.assertEquals(1, catThreads.size()); // back to only 1 category since other threads in 3 categories finished and hence reclaimed
		// 7 threads justified by:
		// 		- finished: frameworkRequest_running1, frameworkRequest_running2 
		//		- finished: completionService, backgroundUtil, queueService
		Assert.assertEquals(7, frameworkReqThreads.size());
		Assert.assertEquals(0, snapshot4.getInterruptionlog().size()); // nothing yet
		Assert.assertEquals(0, snapshot4.getExceptionlog().size()); // nothing yet
		
		// test 5th snapshot
		catThreads = snapshot5.getManagedthreads();
		frameworkReqThreads = catThreads.get(ThreadCategory.FRAMEWORK_REQUEST);
		Assert.assertEquals(1, catThreads.size()); // still 1 category as the 2 original new threads never were executed
		Assert.assertEquals(2, frameworkReqThreads.size()); // 2 threads which were never executed
		Assert.assertEquals(3, snapshot5.getInterruptionlog().size()); // interruptions have occurred
		Assert.assertEquals(2, snapshot5.getExceptionlog().size()); // exceptions have occurred	
	}
	
	private class Sleeper implements Runnable
	{
		final int seconds;
		private Sleeper(final int seconds) {
			this.seconds = seconds;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
			} catch (final InterruptedException e) {
				println("Caught InterruptedException in Sleeper: " + e.getMessage());
			}
		}
	}
	
	private class ExceptionThrower extends Sleeper
	{
		private ExceptionThrower(final int seconds) {
			super(seconds);
		}
		@Override
		public void run() {
			super.run();
			throwIntentionalException();
		}
	}
}