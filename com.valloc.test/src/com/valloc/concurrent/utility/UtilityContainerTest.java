/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.framework.ResultUtilityExecutorAdapter;
import com.valloc.framework.UtilityExecutorAdapter;
import com.valloc.lifecycle.LifecycleChange;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.state.MockStateChangeRequester;
import com.valloc.state.StateChangeRequest;
import com.valloc.state.StateChangeResponse;
import com.valloc.thread.BaseThreadManager;
import com.valloc.thread.Frequency;
import com.valloc.thread.ThreadManager;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;

/**
 *
 *
 *
 * @author wstevens
 */
public class UtilityContainerTest extends AbstractTest
{
	private ThreadManager threadManager;
	private UtilityContainerConfiguration config;
	private UtilityContainer utilityContainer;
	private UtilityContainerStateMachine stateMachine;
	private MockStateChangeRequester requester;

	private void init()
	{
		threadManager = BaseThreadManager.getInstance();
		threadManager.purge();
		config = new ServerUtilityContainerConfiguration();
		utilityContainer = new BaseUtilityContainer(config, threadManager);
		stateMachine = new UtilityContainerStateMachine(utilityContainer);
		requester = new MockStateChangeRequester();
	}

	@Test
	public void utilityContainer_stateMachine_basic()
	{
		init();

		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);
		StateChangeResponse response = requester.getResponse();
		Assert.assertEquals(LifecycleState.ACTIVE, response.getResultingState());
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
		response = requester.getResponse();
		Assert.assertEquals(LifecycleState.TERMINATED, response.getResultingState());
	}

	@Test
	public void utilityContainer_stateMachine_kill()
	{
		init();

		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);
		StateChangeResponse response = requester.getResponse();
		Assert.assertEquals(LifecycleState.ACTIVE, response.getResultingState());
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.KILL, requester);
		stateMachine.requestStateChange(request);
		response = requester.getResponse();
		Assert.assertEquals(LifecycleState.TERMINATED, response.getResultingState());
	}

	@Test
	public void utilityContainer_singleExecution()
	{
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		// test snapshots before
		final UtilityContainerSnapshot snapshotBefore = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshotBefore.getTotalRequestsMade());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestSingleImmediate());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestSingleScheduled());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestRepeatingDelayedScheduled());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestRepeatingFixedScheduled());
		Assert.assertEquals(0, snapshotBefore.getScheduledQueueSize());
		Assert.assertEquals(0, snapshotBefore.getActiveRequestLog().size());
		Assert.assertEquals(0, snapshotBefore.getCompletionLog().size());
		Assert.assertEquals(0, snapshotBefore.getCancelLog().size());
		Assert.assertEquals(0, snapshotBefore.getExceptionLog().size());
		Assert.assertEquals(0, snapshotBefore.getRejectLog().size());
		Assert.assertEquals(0, snapshotBefore.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(0, snapshotBefore.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(0, snapshotBefore.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(1, snapshotBefore.getThreadPoolExecutorPoolSize()); // prestarted with one core thread
		Assert.assertEquals(1, snapshotBefore.getThreadPoolExecutorLargestPoolSize());

		utilityContainer.captureCompletionLogDetails(true);
		// setup and queue 5 standard, single utility executors
		final int reqCnt = 5;
		final ResultUtilityExecutor<String> utilExec1 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-1");
		final ResultUtilityExecutor<String> utilExec2 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-2");
		final ResultUtilityExecutor<String> utilExec3 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-3");
		final ResultUtilityExecutor<String> utilExec4 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-4");
		final ResultUtilityExecutor<String> utilExec5 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-5");
		final FutureResult<String> result1 = utilityContainer.queueExecution(utilExec1);
		final FutureResult<String> result2 = utilityContainer.queueExecution(utilExec2);
		final FutureResult<String> result3 = utilityContainer.queueExecution(utilExec3);
		final FutureResult<String> result4 = utilityContainer.queueExecution(utilExec4);
		final FutureResult<String> result5 = utilityContainer.queueExecution(utilExec5);
		result1.waitForCompletion();
		result2.waitForCompletion();
		result3.waitForCompletion();
		result4.waitForCompletion();
		result5.waitForCompletion();

		// test result contents flowing through container
		final List<String> resultTalley = new ArrayList<String>();
		resultTalley.add(result1.getResult());
		resultTalley.add(result2.getResult());
		resultTalley.add(result3.getResult());
		resultTalley.add(result4.getResult());
		resultTalley.add(result5.getResult());
		for (int i = 1; i <= reqCnt; i++) {
			final String expected = "util-exec-" + i;
			final String actual = resultTalley.get(i-1);
			Assert.assertEquals(expected, actual);
		}

		Util.quietSecondsSleep(2); // wait for all threads to be removed from container registry

		// test snapshot after
		final UtilityContainerSnapshot snapshotAfter = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(reqCnt, snapshotAfter.getTotalRequestsMade());
		Assert.assertEquals(reqCnt, snapshotAfter.getCompletedRequestsCount());
		Assert.assertEquals(reqCnt, snapshotAfter.getCompletedRequestSingleImmediate());
		Assert.assertEquals(0, snapshotAfter.getCompletedRequestSingleScheduled());
		Assert.assertEquals(0, snapshotAfter.getCompletedRequestRepeatingDelayedScheduled());
		Assert.assertEquals(0, snapshotAfter.getCompletedRequestRepeatingFixedScheduled());
		Assert.assertEquals(0, snapshotAfter.getScheduledQueueSize());
		Assert.assertEquals(0, snapshotAfter.getActiveRequestLog().size());
		Assert.assertEquals(reqCnt, snapshotAfter.getCompletionLog().size());
		Assert.assertEquals(0, snapshotAfter.getCancelLog().size());
		Assert.assertEquals(0, snapshotAfter.getExceptionLog().size());
		Assert.assertEquals(0, snapshotAfter.getRejectLog().size());
		Assert.assertEquals(0, snapshotAfter.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(reqCnt, snapshotAfter.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(reqCnt, snapshotAfter.getThreadPoolExecutorCompletedTaskCount());
		// this should be reqCnt + 1; reqCnt in effort to climb to core amount, plus original started core thread in start()
		Assert.assertEquals(reqCnt+1, snapshotAfter.getThreadPoolExecutorPoolSize());
		Assert.assertEquals(reqCnt+1, snapshotAfter.getThreadPoolExecutorLargestPoolSize()); // same as above

		utilityContainer.purgeStats();
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void utilityContainer_singleSchedule()
	{
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		// test snapshots before
		final UtilityContainerSnapshot snapshotBefore = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshotBefore.getTotalRequestsMade());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestSingleImmediate());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestSingleScheduled());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestRepeatingDelayedScheduled());
		Assert.assertEquals(0, snapshotBefore.getCompletedRequestRepeatingFixedScheduled());
		Assert.assertEquals(0, snapshotBefore.getScheduledQueueSize());
		Assert.assertEquals(0, snapshotBefore.getActiveRequestLog().size());
		Assert.assertEquals(0, snapshotBefore.getCompletionLog().size());
		Assert.assertEquals(0, snapshotBefore.getCancelLog().size());
		Assert.assertEquals(0, snapshotBefore.getExceptionLog().size());
		Assert.assertEquals(0, snapshotBefore.getRejectLog().size());
		Assert.assertEquals(0, snapshotBefore.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(0, snapshotBefore.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(0, snapshotBefore.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(1, snapshotBefore.getThreadPoolExecutorPoolSize()); // prestarted with one core thread
		Assert.assertEquals(1, snapshotBefore.getThreadPoolExecutorLargestPoolSize());

		utilityContainer.captureCompletionLogDetails(true);
		// setup and queue 5 standard, single utility executors
		final int reqCnt = 5;
		final ResultUtilityExecutor<String> utilExec1 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-1");
		final ResultUtilityExecutor<String> utilExec2 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-2");
		final ResultUtilityExecutor<String> utilExec3 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-3");
		final ResultUtilityExecutor<String> utilExec4 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-4");
		final ResultUtilityExecutor<String> utilExec5 = new ResultUtilityExecutorAdapter<String>(new UniqueId(), "util-exec-5");
		final FutureResult<String> result1 = utilityContainer.scheduleExecution(utilExec1, Frequency.SECONDS_30);
		final FutureResult<String> result2 = utilityContainer.scheduleExecution(utilExec2, Frequency.SECONDS_15);
		final FutureResult<String> result3 = utilityContainer.scheduleExecution(utilExec3, Frequency.SECONDS_03);
		final FutureResult<String> result4 = utilityContainer.scheduleExecution(utilExec4, Frequency.SECONDS_03);
		final FutureResult<String> result5 = utilityContainer.scheduleExecution(utilExec5, Frequency.SECONDS_03);

		result3.waitForCompletion();
		result4.waitForCompletion();
		result5.waitForCompletion();
		Util.quietSecondsSleep(2); // wait for thread to be removed from container registry
		final UtilityContainerSnapshot snapshotBetween1 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(2, snapshotBetween1.getScheduledQueueSize()); // 2 have not yet started; still in the queue
		Assert.assertEquals(3, snapshotBetween1.getCompletedRequestSingleScheduled());
		Assert.assertEquals(3, snapshotBetween1.getCompletionLog().size());
		Assert.assertEquals(3, snapshotBetween1.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(0, snapshotBetween1.getActiveRequestLog().size());
		Assert.assertEquals(0, snapshotBetween1.getThreadPoolExecutorActiveCount());

		result2.waitForCompletion();
		Util.quietSecondsSleep(2); // wait for thread to be removed from container registry
		final UtilityContainerSnapshot snapshotBetween2 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(1, snapshotBetween2.getScheduledQueueSize()); // 1 has not yet started; still in the queue
		Assert.assertEquals(4, snapshotBetween2.getCompletedRequestSingleScheduled());
		Assert.assertEquals(4, snapshotBetween2.getCompletionLog().size());
		Assert.assertEquals(4, snapshotBetween2.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(0, snapshotBetween2.getActiveRequestLog().size());
		Assert.assertEquals(0, snapshotBetween2.getThreadPoolExecutorActiveCount());

		result1.waitForCompletion();
		Util.quietSecondsSleep(2); // wait for thread to be removed from container registry
		final UtilityContainerSnapshot snapshotBetween3 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshotBetween3.getScheduledQueueSize()); // all have finished and nothing in the delay queue
		Assert.assertEquals(5, snapshotBetween3.getCompletedRequestSingleScheduled());
		Assert.assertEquals(5, snapshotBetween3.getCompletionLog().size());
		Assert.assertEquals(5, snapshotBetween3.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(0, snapshotBetween3.getActiveRequestLog().size());
		Assert.assertEquals(0, snapshotBetween3.getThreadPoolExecutorActiveCount());

		// test result contents flowing through container
		final List<String> resultTalley = new ArrayList<String>();
		resultTalley.add(result1.getResult());
		resultTalley.add(result2.getResult());
		resultTalley.add(result3.getResult());
		resultTalley.add(result4.getResult());
		resultTalley.add(result5.getResult());
		for (int i = 1; i <= reqCnt; i++) {
			final String expected = "util-exec-" + i;
			final String actual = resultTalley.get(i-1);
			Assert.assertEquals(expected, actual);
		}

		// test snapshot after
		Util.quietSecondsSleep(2); // wait for all threads to be removed from container registry
		final UtilityContainerSnapshot snapshotAfter = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(reqCnt, snapshotAfter.getTotalRequestsMade());
		Assert.assertEquals(reqCnt, snapshotAfter.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshotAfter.getCompletedRequestSingleImmediate());
		Assert.assertEquals(reqCnt, snapshotAfter.getCompletedRequestSingleScheduled());
		Assert.assertEquals(5, snapshotAfter.getCompletedRequestSingleScheduled());
		Assert.assertEquals(0, snapshotAfter.getCompletedRequestRepeatingDelayedScheduled());
		Assert.assertEquals(0, snapshotAfter.getCompletedRequestRepeatingFixedScheduled());
		Assert.assertEquals(0, snapshotAfter.getScheduledQueueSize());
		Assert.assertEquals(0, snapshotAfter.getActiveRequestLog().size());
		Assert.assertEquals(reqCnt, snapshotAfter.getCompletionLog().size());
		Assert.assertEquals(0, snapshotAfter.getCancelLog().size());
		Assert.assertEquals(0, snapshotAfter.getExceptionLog().size());
		Assert.assertEquals(0, snapshotAfter.getRejectLog().size());
		Assert.assertEquals(0, snapshotAfter.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(reqCnt, snapshotAfter.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(reqCnt, snapshotAfter.getThreadPoolExecutorCompletedTaskCount());
		// this should be reqCnt + 1; reqCnt in effort to climb to core amount, plus original started core thread in start()
		Assert.assertEquals(reqCnt+1, snapshotAfter.getThreadPoolExecutorPoolSize());
		Assert.assertEquals(reqCnt+1, snapshotAfter.getThreadPoolExecutorLargestPoolSize()); // same as above

		utilityContainer.purgeStats();
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

   @Test
	public void utilityContainer_scheduleDelayed()
	{
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		utilityContainer.captureCompletionLogDetails(true);
		final int utilExecParticipantCount = 10;
		final int desiredIteration = 3; // 3 rounds each executor
		final CountDownLatch latch = new CountDownLatch(utilExecParticipantCount);
		final UtilityExecutor utilExec0 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec1 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec2 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec3 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec4 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec5 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec6 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec7 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec8 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec9 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityCompletionListener listener0 = new SimpleBarrierCompletionListener(utilExec0.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener1 = new SimpleBarrierCompletionListener(utilExec1.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener2 = new SimpleBarrierCompletionListener(utilExec2.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener3 = new SimpleBarrierCompletionListener(utilExec3.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener4 = new SimpleBarrierCompletionListener(utilExec4.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener5 = new SimpleBarrierCompletionListener(utilExec5.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener6 = new SimpleBarrierCompletionListener(utilExec6.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener7 = new SimpleBarrierCompletionListener(utilExec7.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener8 = new SimpleBarrierCompletionListener(utilExec8.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener9 = new SimpleBarrierCompletionListener(utilExec9.id(), utilityContainer, desiredIteration, latch);
		utilityContainer.scheduleAtFixedDelay(utilExec0, Frequency.SECONDS_05, listener0);
		utilityContainer.scheduleAtFixedDelay(utilExec1, Frequency.SECONDS_05, listener1);
		utilityContainer.scheduleAtFixedDelay(utilExec2, Frequency.SECONDS_05, listener2);
		utilityContainer.scheduleAtFixedDelay(utilExec3, Frequency.SECONDS_05, listener3);
		utilityContainer.scheduleAtFixedDelay(utilExec4, Frequency.SECONDS_05, listener4);
		utilityContainer.scheduleAtFixedDelay(utilExec5, Frequency.SECONDS_05, listener5);
		utilityContainer.scheduleAtFixedDelay(utilExec6, Frequency.SECONDS_05, listener6);
		utilityContainer.scheduleAtFixedDelay(utilExec7, Frequency.SECONDS_05, listener7);
		utilityContainer.scheduleAtFixedDelay(utilExec8, Frequency.SECONDS_05, listener8);
		utilityContainer.scheduleAtFixedDelay(utilExec9, Frequency.SECONDS_05, listener9);

		try {
			latch.await();
		} catch (final InterruptedException e) { printThrowableAndFail(e); }
		Util.quietSecondsSleep(3); // allow time for any reclamation

		final int totalCompletions = desiredIteration * utilExecParticipantCount;
		final UtilityContainerSnapshot snapshot1 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot1.getActiveRequestLog().size()); // all are done
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestsCount());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestRepeatingDelayedScheduled());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletionLog().size());
		Assert.assertEquals(0, snapshot1.getScheduledQueueSize()); // queue size
		Assert.assertEquals(0, snapshot1.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(10, snapshot1.getThreadPoolExecutorPoolSize());
		Assert.assertEquals(10, snapshot1.getThreadPoolExecutorLargestPoolSize()); // we hit the core size

		utilityContainer.flushCompletionLogDetails();
		final UtilityContainerSnapshot snapshot2 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot2.getCompletionLog().size());

		utilityContainer.purgeStats();
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void utilityContainer_scheduleRate()
	{
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		utilityContainer.captureCompletionLogDetails(true);
		final int utilExecParticipantCount = 10;
		final int desiredIteration = 3; // 3 rounds each executor
		final CountDownLatch latch = new CountDownLatch(utilExecParticipantCount);
		final UtilityExecutor utilExec0 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec1 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec2 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec3 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec4 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec5 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec6 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec7 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec8 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityExecutor utilExec9 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityCompletionListener listener0 = new SimpleBarrierCompletionListener(utilExec0.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener1 = new SimpleBarrierCompletionListener(utilExec1.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener2 = new SimpleBarrierCompletionListener(utilExec2.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener3 = new SimpleBarrierCompletionListener(utilExec3.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener4 = new SimpleBarrierCompletionListener(utilExec4.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener5 = new SimpleBarrierCompletionListener(utilExec5.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener6 = new SimpleBarrierCompletionListener(utilExec6.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener7 = new SimpleBarrierCompletionListener(utilExec7.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener8 = new SimpleBarrierCompletionListener(utilExec8.id(), utilityContainer, desiredIteration, latch);
		final UtilityCompletionListener listener9 = new SimpleBarrierCompletionListener(utilExec9.id(), utilityContainer, desiredIteration, latch);
		utilityContainer.scheduleAtFixedRate(utilExec0, Frequency.SECONDS_05, listener0);
		utilityContainer.scheduleAtFixedRate(utilExec1, Frequency.SECONDS_05, listener1);
		utilityContainer.scheduleAtFixedRate(utilExec2, Frequency.SECONDS_05, listener2);
		utilityContainer.scheduleAtFixedRate(utilExec3, Frequency.SECONDS_05, listener3);
		utilityContainer.scheduleAtFixedRate(utilExec4, Frequency.SECONDS_05, listener4);
		utilityContainer.scheduleAtFixedRate(utilExec5, Frequency.SECONDS_05, listener5);
		utilityContainer.scheduleAtFixedRate(utilExec6, Frequency.SECONDS_05, listener6);
		utilityContainer.scheduleAtFixedRate(utilExec7, Frequency.SECONDS_05, listener7);
		utilityContainer.scheduleAtFixedRate(utilExec8, Frequency.SECONDS_05, listener8);
		utilityContainer.scheduleAtFixedRate(utilExec9, Frequency.SECONDS_05, listener9);

		try {
			latch.await();
		} catch (final InterruptedException e) { printThrowableAndFail(e); }
		Util.quietSecondsSleep(3); // allow time for any reclamation

		final int totalCompletions = desiredIteration * utilExecParticipantCount;
		final UtilityContainerSnapshot snapshot1 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot1.getActiveRequestLog().size()); // all are done
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestsCount());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestRepeatingFixedScheduled());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletionLog().size());
		Assert.assertEquals(0, snapshot1.getScheduledQueueSize()); // queue size
		Assert.assertEquals(0, snapshot1.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(10, snapshot1.getThreadPoolExecutorPoolSize());
		Assert.assertEquals(10, snapshot1.getThreadPoolExecutorLargestPoolSize()); // we hit the core size

		utilityContainer.flushCompletionLogDetails();
		final UtilityContainerSnapshot snapshot2 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot2.getCompletionLog().size());

		utilityContainer.purgeStats();
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void utilityContainer_scheduleRate_overlapping()
	{
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		utilityContainer.captureCompletionLogDetails(true);
		final int utilExecParticipantCount = 1;
		final int desiredIteration = 3;
		final CountDownLatch latch = new CountDownLatch(utilExecParticipantCount);
		final UtilityExecutor utilExec1 = new FrameworkExecutorSleeper(new UniqueId(), 6);
		final UtilityCompletionListener listener1 = new SimpleBarrierCompletionListener(utilExec1.id(), utilityContainer, desiredIteration, latch);
		utilityContainer.scheduleAtFixedRate(utilExec1, Frequency.SECONDS_05, listener1);

		try {
			latch.await();
		} catch (final InterruptedException e) { printThrowableAndFail(e); }
		Util.quietSecondsSleep(2); // allow time for any reclamation

		final int totalCompletions = desiredIteration * utilExecParticipantCount;
		final UtilityContainerSnapshot snapshot1 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot1.getActiveRequestLog().size()); // all are done
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestsCount());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestRepeatingFixedScheduled());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletionLog().size());
		Assert.assertEquals(0, snapshot1.getScheduledQueueSize()); // queue size
		Assert.assertEquals(0, snapshot1.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(totalCompletions + 1, snapshot1.getThreadPoolExecutorPoolSize());
		Assert.assertEquals(totalCompletions + 1, snapshot1.getThreadPoolExecutorLargestPoolSize());

		utilityContainer.flushCompletionLogDetails();
		final UtilityContainerSnapshot snapshot2 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot2.getCompletionLog().size());

		utilityContainer.purgeStats();
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void utilityContainer_cancelled()
	{
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		utilityContainer.captureCompletionLogDetails(true);
		final int utilExecParticipantCount = 1;
		final int desiredIteration = 2;
		final CountDownLatch latch = new CountDownLatch(utilExecParticipantCount);
		final UtilityExecutor utilExec1 = new UtilityExecutorAdapter(new UniqueId());
		final UtilityCompletionListener listener1 = new SimpleBarrierCompletionListener(utilExec1.id(), utilityContainer, desiredIteration, latch);
		utilityContainer.scheduleAtFixedRate(utilExec1, Frequency.SECONDS_05, listener1);

		Util.quietSecondsSleep(6); // waits for job to execute
		utilityContainer.requestCancellation(utilExec1.id());
		Util.quietSecondsSleep(1); // reclaim

		try {
			latch.await();
		} catch (final InterruptedException e) { printThrowableAndFail(e); }
		Util.quietSecondsSleep(2); // allow time for any reclamation

		final int totalCompletions = desiredIteration * utilExecParticipantCount;
		final UtilityContainerSnapshot snapshot1 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot1.getActiveRequestLog().size()); // all are done
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestsCount());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestRepeatingFixedScheduled());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletionLog().size());
		Assert.assertEquals(1, snapshot1.getCancelLog().size());
		Assert.assertEquals(0, snapshot1.getScheduledQueueSize()); // queue size
		Assert.assertEquals(0, snapshot1.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(totalCompletions + 1, snapshot1.getThreadPoolExecutorPoolSize());
		Assert.assertEquals(totalCompletions + 1, snapshot1.getThreadPoolExecutorLargestPoolSize());

		utilityContainer.flushCompletionLogDetails();
		final UtilityContainerSnapshot snapshot2 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot2.getCompletionLog().size());

		utilityContainer.purgeStats();
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void utilityContainer_exceptionThrown()
	{
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		utilityContainer.captureCompletionLogDetails(true);
		final int utilExecParticipantCount = 1;
		final int desiredIteration = 1;
		final CountDownLatch latch = new CountDownLatch(utilExecParticipantCount);
		final UtilityExecutor utilExec1 = new ExceptionThrowerFrameworkExecutor(new UniqueId(), 1);
		final UtilityCompletionListener listener1 = new SimpleBarrierCompletionListener(utilExec1.id(), utilityContainer, desiredIteration, latch);
		utilityContainer.scheduleAtFixedRate(utilExec1, Frequency.SECONDS_05, listener1);

		try {
			latch.await();
		} catch (final InterruptedException e) { printThrowableAndFail(e); }
		Util.quietSecondsSleep(2); // allow time for any reclamation

		final int totalCompletions = desiredIteration * utilExecParticipantCount;
		final UtilityContainerSnapshot snapshot1 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot1.getActiveRequestLog().size()); // all are done
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestsCount());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletedRequestRepeatingFixedScheduled());
		Assert.assertEquals(1, snapshot1.getExceptionLog().size());
		Assert.assertEquals(totalCompletions, snapshot1.getCompletionLog().size());
		Assert.assertEquals(0, snapshot1.getScheduledQueueSize()); // queue size
		Assert.assertEquals(0, snapshot1.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(totalCompletions, snapshot1.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(totalCompletions + 1, snapshot1.getThreadPoolExecutorPoolSize());
		Assert.assertEquals(totalCompletions + 1, snapshot1.getThreadPoolExecutorLargestPoolSize());

		utilityContainer.flushCompletionLogDetails();
		final UtilityContainerSnapshot snapshot2 = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot2.getCompletionLog().size());

		utilityContainer.purgeStats();
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void utilityContainer_load()
	{
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);


		utilityContainer.captureCompletionLogDetails(true);
		final int loadCount = 50000; // this is plenty; should take ~15 seconds to run
		final CountDownLatch latch = new CountDownLatch(loadCount);
		for (int i = 0; i < loadCount; i++) {
			final ResultUtilityExecutor<Integer> executor = new LoadFrameworkExecutor(new UniqueId(), latch, i);
			utilityContainer.queueExecution(executor);
		}
		try {
			latch.await();
		} catch (final InterruptedException e) { printThrowableAndFail(e); }
		Util.quietSecondsSleep(1);

		final UtilityContainerSnapshot snapshot = utilityContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(loadCount, snapshot.getTotalRequestsMade());
		Assert.assertEquals(0, snapshot.getActiveRequestLog().size()); // all are done
		Assert.assertEquals(loadCount, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(loadCount, snapshot.getCompletedRequestSingleImmediate());
		Assert.assertEquals(loadCount, snapshot.getCompletionLog().size());
		Assert.assertEquals(0, snapshot.getRejectLog().size());
		Assert.assertEquals(0, snapshot.getScheduledQueueSize()); // queue size
		Assert.assertEquals(0, snapshot.getThreadPoolExecutorActiveCount());
		Assert.assertEquals(loadCount, snapshot.getThreadPoolExecutorTaskCount());
		Assert.assertEquals(loadCount, snapshot.getThreadPoolExecutorCompletedTaskCount());
		Assert.assertEquals(10, snapshot.getThreadPoolExecutorPoolSize());
		Assert.assertEquals(10, snapshot.getThreadPoolExecutorLargestPoolSize());

		utilityContainer.purgeStats();
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	private class LoadFrameworkExecutor extends ResultUtilityExecutorAdapter<Integer>
	{
		CountDownLatch latch;
		public LoadFrameworkExecutor(final UniqueId id, final CountDownLatch latch, final Integer count) {
			super(id, count);
			this.latch = latch;
		}
		@Override public void execute() {
			latch.countDown();
		}
	}

	private final class SimpleBarrierCompletionListener implements UtilityCompletionListener
	{
		private final UniqueId utilityId;
		private final UtilityContainer utilityContainer;
		private final int desiredIteration;
		private int iterationCount;
		private final CountDownLatch latch;

		SimpleBarrierCompletionListener(final UniqueId utilityId, final UtilityContainer utilityContainer, final int desiredIteration, final CountDownLatch latch) {
			this.utilityId = utilityId;
			this.utilityContainer = utilityContainer;
			this.desiredIteration = desiredIteration;
			this.latch = latch;
		}
		@Override public void utilityComplete(final ContainerExecutionResult result) {
			// here desired iteration is minus one since by the time this method is invoked, the previous scheduing
			//		would have occurred, subtract one to compensate
			final int barrier = desiredIteration - 1;
			if (iterationCount++ == barrier) {
				utilityContainer.removeFurtherExecutions(utilityId);
				latch.countDown();
			}
		}
	}

	private class FrameworkExecutorSleeper extends UtilityExecutorAdapter
	{
		final int seconds;
		public FrameworkExecutorSleeper(final UniqueId id, final int seconds) {
			super(id);
			this.seconds = seconds;
		}

		@Override public void execute() {
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
			} catch (final InterruptedException swallow) { swallow.printStackTrace(); System.out.println("AAAAAhhhh!!");}
		}
	}

	private class ExceptionThrowerFrameworkExecutor extends FrameworkExecutorSleeper
	{
		public ExceptionThrowerFrameworkExecutor(final UniqueId id, final int seconds) {
			super(id, seconds);
		}

		@Override public void execute() {
			super.execute();
			throwIntentionalException();
		}
	}
}