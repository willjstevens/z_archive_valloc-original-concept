/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.Priority;
import com.valloc.framework.AbstractFrameworkManager;
import com.valloc.framework.FrameworkExecutor;
import com.valloc.framework.FrameworkExecutorAdapter;
import com.valloc.framework.FrameworkManager;
import com.valloc.interrupt.InterruptCompletionListener;
import com.valloc.interrupt.InterruptFuture;
import com.valloc.interrupt.InterruptType;
import com.valloc.lifecycle.LifecycleChange;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.state.MockStateChangeRequester;
import com.valloc.state.StateChangeRequest;
import com.valloc.state.StateChangeResponse;
import com.valloc.thread.BaseThreadManager;
import com.valloc.thread.ThreadManager;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;

/**
 *
 *
 *
 * @author wstevens
 */
public class RequestContainerTest extends AbstractTest
{
	private ThreadManager threadManager;
	private FrameworkManager frameworkManager;
	private RequestContainerConfiguration config;
	private RequestContainer requestContainer;
	private RequestContainerStateMachine stateMachine;
	private MockStateChangeRequester requester;

	private void init() {
		threadManager = BaseThreadManager.getInstance();
		threadManager.purge();
		frameworkManager = new BaseFrameworkManager();
		config = new DesktopRequestContainerConfiguration();
		requestContainer = new BaseRequestContainer(config, threadManager);
		requestContainer.setFrameworkManager(frameworkManager);
		stateMachine = new RequestContainerStateMachine(requestContainer);
		requester = new MockStateChangeRequester();
	}

	@Test
	public void requestContainer_stateMachine_basic() {
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
	public void requestContainer_stateMachine_kill() {
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
	public void requestContainer_basic() {
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		// test simple 4 requests and test basic results
		requestContainer.queueForExecution(new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD));
		requestContainer.queueForExecution(new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD));
		requestContainer.queueForExecution(new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD));
		requestContainer.queueForExecution(new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD));
		Util.quietSecondsSleep(2); // delay for 4 simple requests to finish
		RequestContainerSnapshot snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(4, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshot.getBacklogQueueSize());
		Assert.assertEquals(0, snapshot.getCancelLog().size());
		Assert.assertEquals(0, snapshot.getExceptionLog().size());
		requestContainer.purgeStats();

		// test sleeper request to mimic in-process request
		requestContainer.queueForExecution(new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 2));
		requestContainer.queueForExecution(new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 2));
		requestContainer.queueForExecution(new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 2));
		Util.quietSecondsSleep(1); // delay for request to be in-process
		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(3, snapshot.getActiveRequestLog().size());
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Util.quietSecondsSleep(2); // delay again enough for 2 second sleeper to be done
		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getActiveRequestLog().size()); // should be done now
		Assert.assertEquals(3, snapshot.getCompletedRequestsCount());
		requestContainer.purgeStats();

		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void requestContainer_basic_finishWithinDestroy() {
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		// test multiple cancellations
		final FrameworkExecutor executor1 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 5);
		final FrameworkExecutor executor2 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 5);
		final FrameworkExecutor executor3 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 5);
		requestContainer.queueForExecution(executor1);
		requestContainer.queueForExecution(executor2);
		requestContainer.queueForExecution(executor3);
		Util.quietSecondsSleep(1);
		RequestContainerSnapshot snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshot.getCancelLog().size());

		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);

		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(3, snapshot.getCompletedRequestsCount()); // the rest were cancelled
		Assert.assertEquals(0, snapshot.getCancelLog().size()); // the rest were cancelled
		Assert.assertEquals(0, snapshot.getActiveRequestLog().size()); // cancelled requests should have been removed

	}

	@Test
	public void requestContainer_cancelled() {
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		// test simple request submit, with seconds very high knowing they'll be cancelled
		final FrameworkExecutor executor = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 60);
		requestContainer.queueForExecution(executor);
		Util.quietSecondsSleep(1); // delay for simple request to be fired
		RequestContainerSnapshot snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshot.getCancelLog().size());
		requestContainer.requestCancellation(executor.id());
		Util.quietSecondsSleep(1); // enough for cancellation to functionally process
		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(1, snapshot.getCancelLog().size());
		requestContainer.purgeStats();

		// test multiple cancellations
		final FrameworkExecutor executor1 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 60);
		final FrameworkExecutor executor2 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 60);
		final FrameworkExecutor executor3 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 60);
		final FrameworkExecutor executor4 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 60);
		final FrameworkExecutor executor5 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 3);
		requestContainer.queueForExecution(executor1);
		requestContainer.queueForExecution(executor2);
		requestContainer.queueForExecution(executor3);
		requestContainer.queueForExecution(executor4);
		requestContainer.queueForExecution(executor5);
		Util.quietSecondsSleep(1);
		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshot.getCancelLog().size());
		requestContainer.requestCancellation(executor1.id());
		requestContainer.requestCancellation(executor2.id());
		requestContainer.requestCancellation(executor3.id());
		requestContainer.requestCancellation(executor4.id());
		Util.quietSecondsSleep(3);
		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(1, snapshot.getCompletedRequestsCount()); // executor5 should have finished, since short and not cancelled
		Assert.assertEquals(4, snapshot.getCancelLog().size()); // the rest were cancelled
		Assert.assertEquals(0, snapshot.getActiveRequestLog().size()); // cancelled requests should have been removed

		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void requestContainer_cancelled_withinKill() {
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		// test multiple cancellations
		final FrameworkExecutor executor1 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 60);
		final FrameworkExecutor executor2 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 60);
		final FrameworkExecutor executor3 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 60);

		requestContainer.queueForExecution(executor1);
		requestContainer.queueForExecution(executor2);
		requestContainer.queueForExecution(executor3);
		Util.quietSecondsSleep(1);
		RequestContainerSnapshot snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshot.getCancelLog().size());

		request = new StateChangeRequest(new UniqueId(), LifecycleChange.KILL, requester);
		stateMachine.requestStateChange(request);
		Util.quietSecondsSleep(3); // allow time for cancellations

		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(3, snapshot.getCancelLog().size()); // the rest were cancelled
		Assert.assertEquals(0, snapshot.getActiveRequestLog().size()); // cancelled requests should have been removed
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
	}

	@Test
	public void requestContainer_exceptionThrown() {
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		// test simple request submit, with seconds very high knowing they'll be cancelled
		final FrameworkExecutor executor = new ExceptionThrowerFrameworkExecutor(new UniqueId(), Priority.USER_STANDARD, 3);
		requestContainer.queueForExecution(executor);
		Util.quietSecondsSleep(1); // delay for simple request to be fired
		RequestContainerSnapshot snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshot.getExceptionLog().size());
		Util.quietSecondsSleep(4); // enough for exception to functionally process
		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(1, snapshot.getExceptionLog().size());
		requestContainer.purgeStats();

		// test multiple exception throwers
		final FrameworkExecutor executor1 = new ExceptionThrowerFrameworkExecutor(new UniqueId(), Priority.USER_STANDARD, 6); // exception
		final FrameworkExecutor executor2 = new ExceptionThrowerFrameworkExecutor(new UniqueId(), Priority.USER_STANDARD, 5); // exception
		final FrameworkExecutor executor3 = new ExceptionThrowerFrameworkExecutor(new UniqueId(), Priority.USER_STANDARD, 4); // exception
		final FrameworkExecutor executor4 = new ExceptionThrowerFrameworkExecutor(new UniqueId(), Priority.USER_STANDARD, 3); // exception
		final FrameworkExecutor executor5 = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 3); // complete
		requestContainer.queueForExecution(executor1);
		requestContainer.queueForExecution(executor2);
		requestContainer.queueForExecution(executor3);
		requestContainer.queueForExecution(executor4);
		requestContainer.queueForExecution(executor5);
		Util.quietSecondsSleep(1);
		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshot.getExceptionLog().size());
		Util.quietSecondsSleep(7);
		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(1, snapshot.getCompletedRequestsCount()); // executor5 should have finished, since short and not cancelled
		Assert.assertEquals(4, snapshot.getExceptionLog().size()); // the rest were cancelled
		Assert.assertEquals(0, snapshot.getActiveRequestLog().size()); // exception requests should have been removed

		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void requestContainer_backloggedAndPrioritized() {
		threadManager = BaseThreadManager.getInstance();
		threadManager.purge();
		config = new DesktopRequestContainerConfiguration();
		config.corePoolSize = 1;
		config.maxPoolSize = 1;
		config.queueBoundSize = 50; // super high so we don't test rejection handler in this test
		requestContainer = new BaseRequestContainer(config, threadManager);
		requestContainer.setFrameworkManager(frameworkManager);
		stateMachine = new RequestContainerStateMachine(requestContainer);
		requester = new MockStateChangeRequester();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		// This executor is set to 5 minutes so it intentionally consumes the only one thread in the executor;
		// this will push all subsequent executors onto the backlog queue.
		final FrameworkExecutorSleeper threadTimeHogger = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 300);
		requestContainer.queueForExecution(threadTimeHogger);
		Util.quietSecondsSleep(1); // delay for thread time hogger to get established in running state
		// now setup our prioritizable queue elements
		final List<Integer> assertList = new ArrayList<Integer>(); // the list reference to tally the exepected order
		final BacklogQueueTrackingFrameworkExecutor exec0 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(), Priority.USER_HIGH,
				assertList, 6);
		final BacklogQueueTrackingFrameworkExecutor exec1 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(),
				Priority.USER_STANDARD, assertList, 8);
		final BacklogQueueTrackingFrameworkExecutor exec2 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(),
				Priority.ADMIN_STANDARD, assertList, 3);
		final BacklogQueueTrackingFrameworkExecutor exec3 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(),
				Priority.SYSTEM_STANDARD, assertList, 5);
		final BacklogQueueTrackingFrameworkExecutor exec4 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(), Priority.USER_HIGH,
				assertList, 7);
		final BacklogQueueTrackingFrameworkExecutor exec5 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(), Priority.SYSTEM_HIGH,
				assertList, 4);
		final BacklogQueueTrackingFrameworkExecutor exec6 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(),
				Priority.USER_STANDARD, assertList, 9);
		final BacklogQueueTrackingFrameworkExecutor exec7 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(), Priority.ADMIN_HIGH,
				assertList, 1);
		final BacklogQueueTrackingFrameworkExecutor exec8 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(),
				Priority.USER_STANDARD, assertList, 10);
		final BacklogQueueTrackingFrameworkExecutor exec9 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(), Priority.ADMIN_HIGH,
				assertList, 2);
		final BacklogQueueTrackingFrameworkExecutor exec10 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(),
				Priority.USER_STANDARD, assertList, 11);
		final BacklogQueueTrackingFrameworkExecutor exec11 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(),
				Priority.USER_STANDARD, assertList, 12);
		final BacklogQueueTrackingFrameworkExecutor exec12 = new BacklogQueueTrackingFrameworkExecutor(new UniqueId(),
				Priority.USER_STANDARD, assertList, 13);
		// now submit all requests in order
		requestContainer.queueForExecution(exec0);
		requestContainer.queueForExecution(exec1);
		requestContainer.queueForExecution(exec2);
		requestContainer.queueForExecution(exec3);
		requestContainer.queueForExecution(exec4);
		requestContainer.queueForExecution(exec5);
		requestContainer.queueForExecution(exec6);
		requestContainer.queueForExecution(exec7);
		requestContainer.queueForExecution(exec8);
		requestContainer.queueForExecution(exec9);
		requestContainer.queueForExecution(exec10);
		requestContainer.queueForExecution(exec11);
		requestContainer.queueForExecution(exec12);
		Util.quietSecondsSleep(1); // allows for all to be properly queued
		RequestContainerSnapshot snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(13, snapshot.getQueuedRequestLog().size());
		Assert.assertEquals(13, snapshot.getBacklogQueueSize());
		requestContainer.requestCancellation(threadTimeHogger.id()); // kills hogger so queued elements can be processed
		Util.quietSecondsSleep(2); // allows all elements to process

		Assert.assertEquals(13, assertList.size());
		System.out.println(assertList);
		for (int i = 0; i < 13; i++) {
			final int expected = i + 1;
			final int actual = assertList.get(i);
			Assert.assertEquals(expected, actual); // verify order is as expected
		}

		snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(1, snapshot.getCancelLog().size()); // the cancelled hogger thread
		Assert.assertEquals(13, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(0, snapshot.getQueuedRequestLog().size());

		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void requestContainer_rejected() {
		threadManager = BaseThreadManager.getInstance();
		threadManager.purge();
		config = new DesktopRequestContainerConfiguration();
		config.corePoolSize = 1;
		config.maxPoolSize = 1;
		config.queueBoundSize = 1; // super low to trigger rejects
		requestContainer = new BaseRequestContainer(config, threadManager);
		requestContainer.setFrameworkManager(frameworkManager);
		stateMachine = new RequestContainerStateMachine(requestContainer);
		requester = new MockStateChangeRequester();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		// This executor is set to 5 minutes so it intentionally consumes the only one thread in the executor;
		// this will push all subsequent executors onto the backlog queue.
		final FrameworkExecutorSleeper threadTimeHogger = new FrameworkExecutorSleeper(new UniqueId(), Priority.USER_STANDARD, 300);
		requestContainer.queueForExecution(threadTimeHogger);
		Util.quietSecondsSleep(1); // delay for thread time hogger to get established in running state
		// now setup our prioritizable queue elements
		final FrameworkExecutorAdapter exec0 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		final FrameworkExecutorAdapter exec1 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		final FrameworkExecutorAdapter exec2 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		final FrameworkExecutorAdapter exec3 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		final FrameworkExecutorAdapter exec4 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		final FrameworkExecutorAdapter exec5 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		final FrameworkExecutorAdapter exec6 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		final FrameworkExecutorAdapter exec7 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		final FrameworkExecutorAdapter exec8 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		final FrameworkExecutorAdapter exec9 = new FrameworkExecutorAdapter(new UniqueId(), Priority.USER_STANDARD);
		// now submit all requests in order
		requestContainer.queueForExecution(exec0);
		requestContainer.queueForExecution(exec1);
		requestContainer.queueForExecution(exec2);
		requestContainer.queueForExecution(exec3);
		requestContainer.queueForExecution(exec4);
		requestContainer.queueForExecution(exec5);
		requestContainer.queueForExecution(exec6);
		requestContainer.queueForExecution(exec7);
		requestContainer.queueForExecution(exec8);
		requestContainer.queueForExecution(exec9);
		Util.quietSecondsSleep(1); // allows for all to be properly queued
		final RequestContainerSnapshot snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(0, snapshot.getCompletedRequestsCount());
		Assert.assertEquals(9, snapshot.getRejectLog().size());
		requestContainer.requestCancellation(threadTimeHogger.id()); // kills hogger so queued elements can be processed
		Util.quietSecondsSleep(2); // allows all elements to process

		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void requestContainer_load_executorImmediateFinish() {
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		final int loadCount = 10000; // this is plenty; should take ~15 seconds to run
		final AtomicInteger counter = new AtomicInteger();
		for (int i = 0; i < loadCount; i++) {
			final FrameworkExecutor executor = new LoadFrameworkExecutor(new UniqueId(), Priority.USER_STANDARD, counter);
			requestContainer.queueForExecution(executor);
		}
		Util.quietSecondsSleep(1);

		final RequestContainerSnapshot snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(loadCount, snapshot.getTotalRequestsMade());
		Assert.assertEquals(loadCount, counter.get());

		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	@Test
	public void requestContainer_load_executorDelayedFinish() {
		init();
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);

		final int secondDelay = 2;
		final int loadCount = 100; // this is plenty; most of these will be rejects
		final AtomicInteger counter = new AtomicInteger();
		for (int i = 0; i < loadCount; i++) {
			final FrameworkExecutor executor = new LoadDelayingFrameworkExecutor(new UniqueId(), Priority.USER_STANDARD, counter,
					secondDelay);
			requestContainer.queueForExecution(executor);
		}

		final RequestContainerSnapshot snapshot = requestContainer.takeThreadContainerSnapshot();
		Assert.assertEquals(loadCount, snapshot.getTotalRequestsMade());

		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
	}

	private class LoadFrameworkExecutor extends FrameworkExecutorAdapter
	{
		AtomicInteger counter;

		public LoadFrameworkExecutor(final UniqueId id, final Priority priority, final AtomicInteger counter) {
			super(id, priority);
			this.counter = counter;
		}

		@Override
		public void execute() {
			counter.incrementAndGet();
		}
	}

	private class LoadDelayingFrameworkExecutor extends FrameworkExecutorAdapter
	{
		AtomicInteger counter;
		int seconds;

		public LoadDelayingFrameworkExecutor(final UniqueId id, final Priority priority, final AtomicInteger counter, final int seconds) {
			super(id, priority);
			this.counter = counter;
			this.seconds = seconds;
		}

		@Override
		public void execute() {
			Util.quietSecondsSleep(seconds);
			counter.incrementAndGet();
		}
	}

	private class BacklogQueueTrackingFrameworkExecutor extends FrameworkExecutorAdapter
	{
		List<Integer> assertList;
		Integer execNum;

		public BacklogQueueTrackingFrameworkExecutor(final UniqueId id, final Priority priority, final List<Integer> assertList, final Integer execNum) {
			super(id, priority);
			this.assertList = assertList;
			this.execNum = execNum;
		}

		@Override
		public void execute() {
			assertList.add(execNum);
		}
	}

	private class FrameworkExecutorSleeper extends FrameworkExecutorAdapter
	{
		final int seconds;

		public FrameworkExecutorSleeper(final UniqueId id, final Priority priority, final int seconds) {
			super(id, priority);
			this.seconds = seconds;
		}

		@Override
		public void execute() {
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
			} catch (final InterruptedException swallow) {
			}
		}
	}

	private class ExceptionThrowerFrameworkExecutor extends FrameworkExecutorSleeper
	{
		public ExceptionThrowerFrameworkExecutor(final UniqueId id, final Priority priority, final int seconds) {
			super(id, priority, seconds);
		}

		@Override
		public void execute() {
			super.execute();
			throwIntentionalException();
		}
	}

	public class BaseFrameworkManager extends AbstractFrameworkManager
	{
		@Override
		public InterruptFuture requestInterrupt(final UniqueId id, final InterruptType interruptType) {
			return super.requestInterrupt(id, interruptType);
		}

		@Override
		public InterruptFuture requestInterrupt(final UniqueId id, final InterruptType interruptType, final InterruptCompletionListener completionListener) {
			return super.requestInterrupt(id, interruptType, completionListener);
		}

	}
}