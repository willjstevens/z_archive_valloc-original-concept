/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.valloc.CategoryType;
import com.valloc.Constants;
import com.valloc.MessageSummary;
import com.valloc.MessageSummaryStatus;
import com.valloc.MessageType;
import com.valloc.config.ChangeEvent;
import com.valloc.interrupt.InterruptType;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.thread.Frequency;
import com.valloc.thread.ThreadCategory;
import com.valloc.thread.ThreadManager;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;

/**
 * 
 * 
 * @author wstevens
 */
public class BaseUtilityContainer implements UtilityContainer
{
	private static final Logger logger = LogManager.manager().getLogger(BaseUtilityContainer.class, CategoryType.CONCURRENT_CONTAINER_UTILITY);

	// standard
	private ScheduledThreadPoolExecutor schedulingThreadPoolExecutor;
	private LifecycleState currentState;
	private UtilityContainerConfiguration configuration;
	private long totalRequestsMade;
	private long singleExecutionImmediateCount;
	private long singleExecutionScheduledCount;
	private long repeatingScheduleDelayCount;
	private long repeatingScheduleRateCount;

	private final ThreadManager threadManager;
	private final Lock fairAccessLock = new ReentrantLock(true);
	private final Map<UniqueId, RequestTracker> activeRequests = new HashMap<UniqueId, RequestTracker>();
	private final Lock activeRequestsLock = new ReentrantLock(false);
	private final Map<UniqueId, RepeatingExecutionTracker> repeatingSchedules = new HashMap<UniqueId, RepeatingExecutionTracker>();
	private final UtilityContainerStatistician statistician = new UtilityContainerStatistician(this);

	BaseUtilityContainer(final UtilityContainerConfiguration configuration, final ThreadManager threadManager) {
		this.configuration = configuration;
		this.threadManager = threadManager;
	}

	@Override
	public <R> FutureResult<R> queueExecution(final ResultUtilityExecutor<R> utilityExecutor) {
		FutureResult<R> futureResult = null;

		try {
			fairAccessLock.lock();

			if (!currentState.equals(LifecycleState.ACTIVE)) {
				throw new IllegalStateException("Invalid state for thread container to submit a request.");
			}

			totalRequestsMade++;
			singleExecutionImmediateCount++;
			final UniqueId utilityId = utilityExecutor.id();
			final UtilityType utilityType = UtilityType.SINGLE_IMMEDIATE;
			final Date inceptionTimestamp = Util.nowTimestamp();
			final SingleExecutionWrapper<R> callableExecutor = new SingleExecutionWrapper<R>(utilityExecutor, utilityType, inceptionTimestamp);

			final Future<R> future = schedulingThreadPoolExecutor.submit(callableExecutor);
			logger.finer("Submitted request %s into thread pool executor.", utilityId);

			@SuppressWarnings(Constants.UNCHECKED)
			final ManagedRunnableScheduledFuture<R> task = ManagedRunnableScheduledFuture.class.cast(future);
			futureResult = new FutureResult<R>(task);
		} finally {
			fairAccessLock.unlock();
		}

		return futureResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#scheduleExecution(com.valloc.framework.UtilityExecutor, com.valloc.concurrent.Frequency)
	 */
	@Override
	public <R> FutureResult<R> scheduleExecution(final ResultUtilityExecutor<R> utilityExecutor, final Frequency delay) {
		FutureResult<R> futureResult = null;

		try {
			fairAccessLock.lock();

			if (!currentState.equals(LifecycleState.ACTIVE)) {
				throw new IllegalStateException("Invalid state for thread container to submit a request.");
			}

			totalRequestsMade++;
			singleExecutionScheduledCount++;
			final UniqueId utilityId = utilityExecutor.id();
			final UtilityType utilityType = UtilityType.SINGLE_SCHEDULED;
			final Date inceptionTimestamp = Util.nowTimestamp();
			final int delayInSeconds = delay.frequencyInSeconds();
			final SingleExecutionWrapper<R> callableExecutor = new SingleExecutionWrapper<R>(utilityExecutor, utilityType, inceptionTimestamp);

			final Future<R> future = schedulingThreadPoolExecutor.schedule(callableExecutor, delayInSeconds, TimeUnit.SECONDS);
			logger.finer("Submitted request %s into thread pool executor.", utilityId);

			@SuppressWarnings(Constants.UNCHECKED)
			final ManagedRunnableScheduledFuture<R> task = ManagedRunnableScheduledFuture.class.cast(future);
			futureResult = new FutureResult<R>(task);
		} finally {
			fairAccessLock.unlock();
		}

		return futureResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#scheduleFixedDelay(com.valloc.framework.UtilityExecutor, com.valloc.concurrent.Frequency)
	 */
	@Override
	public void scheduleAtFixedDelay(final UtilityExecutor executor, final Frequency frequency) {
		scheduleAtFixedDelay(executor, frequency, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#scheduleFixedDelay(com.valloc.framework.UtilityExecutor, com.valloc.concurrent.Frequency,
	 * com.valloc.concurrent.utility.UtilityCompletionListener)
	 */
	@Override
	public void scheduleAtFixedDelay(final UtilityExecutor utilityExecutor, final Frequency frequency, final UtilityCompletionListener completionListener) {
		try {
			fairAccessLock.lock();

			if (!currentState.equals(LifecycleState.ACTIVE)) {
				throw new IllegalStateException("Invalid state for thread container to submit a request.");
			}

			totalRequestsMade++;
			// setup and submit internal executor wrapper
			repeatingScheduleDelayCount++;
			final UniqueId utilityId = utilityExecutor.id();
			final UtilityType utilityType = UtilityType.REPEATING_DELAYED_SCHEDULED;
			final Date inceptionTimestamp = Util.nowTimestamp();
			final RepeatingExecutionWrapper runnableExecutor = new RepeatingExecutionWrapper(utilityExecutor, frequency, utilityType,
					inceptionTimestamp, completionListener, this);
			final int delayInSeconds = frequency.frequencyInSeconds();
			schedulingThreadPoolExecutor.schedule(runnableExecutor, delayInSeconds, TimeUnit.SECONDS);
			logger.finer("Submitted request %s into thread pool executor.", utilityId);
		} finally {
			fairAccessLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#scheduleFixedDelay(com.valloc.framework.UtilityExecutor, com.valloc.concurrent.Frequency)
	 */
	@Override
	public void scheduleAtFixedRate(final UtilityExecutor executor, final Frequency frequency) {
		scheduleAtFixedDelay(executor, frequency, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#scheduleFixedDelay(com.valloc.framework.UtilityExecutor, com.valloc.concurrent.Frequency,
	 * com.valloc.concurrent.utility.UtilityCompletionListener)
	 */
	@Override
	public void scheduleAtFixedRate(final UtilityExecutor utilityExecutor, final Frequency frequency, final UtilityCompletionListener completionListener) {
		try {
			fairAccessLock.lock();

			if (!currentState.equals(LifecycleState.ACTIVE)) {
				throw new IllegalStateException("Invalid state for thread container to submit a request.");
			}

			totalRequestsMade++;
			// setup and submit internal executor wrapper
			repeatingScheduleRateCount++;
			final UniqueId utilityId = utilityExecutor.id();
			final UtilityType utilityType = UtilityType.REPEATING_FIXED_SCHEDULED;
			final Date inceptionTimestamp = Util.nowTimestamp();
			final RepeatingExecutionWrapper runnableExecutor = new RepeatingExecutionWrapper(utilityExecutor, frequency, utilityType,
					inceptionTimestamp, completionListener, this);
			final int delayInSeconds = frequency.frequencyInSeconds();
			schedulingThreadPoolExecutor.schedule(runnableExecutor, delayInSeconds, TimeUnit.SECONDS);
			logger.finer("Submitted request %s into thread pool executor.", utilityId);
		} finally {
			fairAccessLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#schedule(java.lang.Runnable, int)
	 */
	@Override
	public ScheduledFuture<?> schedule(final Runnable utilityExecutor, final int secondsDelay) {
		totalRequestsMade++;
		return schedulingThreadPoolExecutor.schedule(utilityExecutor, secondsDelay, TimeUnit.SECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#removeFurtherExecutions(com.valloc.UniqueId)
	 */
	@Override
	public void removeFurtherExecutions(final UniqueId utilityId) {
		final RepeatingExecutionTracker wrapper = repeatingSchedules.remove(utilityId);
		if (wrapper == null) {
			throw new IllegalStateException(String.format(
					"Found null wrapper object when attempting to remove utility request %s from further execution.", utilityId));
		}

		final boolean wasRemoved = schedulingThreadPoolExecutor.remove(wrapper.getRunnableFuture());
		logger.finer("Was able to remove utility runnable from executor: %s.", wasRemoved);
		wrapper.getExecutorWrapper().setMarkedForRemoval(true);
		wrapper.getRunnableFuture().cancel(false); // never interrupt, just let finish...
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#executionBeginning(com.valloc.UniqueId,
	 * com.valloc.concurrent.utility.ManagedRunnableScheduledFuture)
	 */
	@Override
	public <R> void executionBeginning(final ManagedRunnableScheduledFuture<R> runnableFuture) {
		final UniqueId utilityId = runnableFuture.id();
		final UtilityType utilityType = runnableFuture.getUtilityType();
		final BaseExecutorWrapper executorWrapper = runnableFuture.getExecutorWrapper();
		RequestTracker wrapper = null;
		if (executorWrapper instanceof SingleExecutionWrapper<?>) {
			@SuppressWarnings(Constants.UNCHECKED)
			final SingleExecutionWrapper<R> callableExecutorWrapper = (SingleExecutionWrapper<R>) executorWrapper;
			wrapper = new SingleExecutionTracker<R>(utilityId, utilityType, callableExecutorWrapper, runnableFuture);
		} else if (executorWrapper instanceof RepeatingExecutionWrapper) {
			final RepeatingExecutionWrapper runnableExecutorWrapper = (RepeatingExecutionWrapper) executorWrapper;
			wrapper = new RepeatingExecutionTracker(utilityId, utilityType, runnableExecutorWrapper, runnableFuture);
			if (!repeatingSchedules.containsKey(utilityId)) {
				repeatingSchedules.put(utilityId, (RepeatingExecutionTracker) wrapper);
			}
		}

		try {
			activeRequestsLock.lock();
			activeRequests.put(utilityId, wrapper);
			logger.finer("Stored reference to queued immediate request %s.", wrapper.utilityId);
		} finally {
			activeRequestsLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.container.ThreadContainer#executionComplete(java.util.UUID)
	 */
	@Override
	public void executionComplete(final UniqueId utilityId) {
		try {
			logger.finer("Complete request %s is about to be removed from active state; waiting for lock.", utilityId);
			activeRequestsLock.lock();
			activeRequests.remove(utilityId);
			logger.finer("Done removing request %s from active state.", utilityId);
		} finally {
			activeRequestsLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#requestCancellation(com.valloc.UniqueId)
	 */
	@Override
	public void requestCancellation(final UniqueId requestId) {
		requestCancellation(requestId, InterruptType.USER_CANCELLED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#requestCancellation(com.valloc.UniqueId, com.valloc.InterruptionType)
	 */
	@Override
	public void requestCancellation(final UniqueId utilityId, final InterruptType interruptType) {
		RequestTracker requestTracker = null;

		if (activeRequests.containsKey(utilityId)) {
			requestTracker = activeRequests.get(utilityId);
		} else if (repeatingSchedules.containsKey(utilityId)) {
			requestTracker = repeatingSchedules.get(utilityId);
		}

		if (requestTracker != null) { // could be null if not submitted or in delay queue
			final BaseExecutorWrapper executorWrapper = requestTracker.getExecutorWrapper();
			executorWrapper.setPassthroughCancelType(interruptType);
			schedulingThreadPoolExecutor.remove(requestTracker.getRunnableFuture());
			final Future<?> cancellable = requestTracker.getRunnableFuture();
			final boolean wasCancelled = cancellable.cancel(true); // always interrupt if running

			if (requestTracker instanceof RepeatingExecutionTracker) {
				final RepeatingExecutionWrapper repeatingWrapper = (RepeatingExecutionWrapper) executorWrapper;
				repeatingWrapper.setMarkedForRemoval(true);
			}
			if (logger.isFiner()) {
				if (wasCancelled) {
					logger.finer("Cancel request made for %s.", utilityId);
				} else {
					logger.finer("Failed to cancel request %s.", utilityId);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#takeThreadContainerSnapshot()
	 */
	@Override
	public UtilityContainerSnapshot takeThreadContainerSnapshot() {
		return statistician.takeThreadContainerSnapshot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#captureCompletionLogDetails(boolean)
	 */
	@Override
	public void captureCompletionLogDetails(final boolean doCapture) {
		statistician.setDoCaptureCompletionLogDetails(doCapture);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.utility.UtilityContainer#flushCompletionLogDetails()
	 */
	@Override
	public void flushCompletionLogDetails() {
		statistician.flushCompletionLogDetails();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#purgeStats()
	 */
	@Override
	public void purgeStats() {
		statistician.purge();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(final Runnable runnable) {
		Thread newThread = null;

		final ThreadCategory category = ThreadCategory.FRAMEWORK_REQUEST;
		final String name = String.format("%s Utility Thread", getClass().getSimpleName());
		newThread = threadManager.newThread(runnable, category, name);

		return newThread;
	}

	private abstract class RequestTracker
	{
		UniqueId utilityId;
		UtilityType type;
		ManagedRunnableScheduledFuture<?> runnableFuture;

		private RequestTracker(final UniqueId utilityId, final UtilityType type, final ManagedRunnableScheduledFuture<?> cancellable) {
			this.utilityId = utilityId;
			this.type = type;
			this.runnableFuture = cancellable;
		}

		ManagedRunnableScheduledFuture<?> getRunnableFuture() {
			return runnableFuture;
		}

		abstract BaseExecutorWrapper getExecutorWrapper();
	}

	private final class SingleExecutionTracker<R> extends RequestTracker
	{
		SingleExecutionWrapper<R> executor;

		private SingleExecutionTracker(final UniqueId utilityId, final UtilityType type, final SingleExecutionWrapper<R> executor, final ManagedRunnableScheduledFuture<R> future) {
			super(utilityId, type, future);
			this.executor = executor;
		}

		@Override
		SingleExecutionWrapper<R> getExecutorWrapper() {
			return executor;
		}
	}

	private final class RepeatingExecutionTracker extends RequestTracker
	{
		RepeatingExecutionWrapper executor;

		private RepeatingExecutionTracker(final UniqueId utilityId, final UtilityType type, final RepeatingExecutionWrapper executor, final ManagedRunnableScheduledFuture<?> future) {
			super(utilityId, type, future);
			this.executor = executor;
		}

		@Override
		RepeatingExecutionWrapper getExecutorWrapper() {
			return executor;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
	 */
	@SuppressWarnings(Constants.UNCHECKED)
	@Override
	public void rejectedExecution(final Runnable runnableTask, final ThreadPoolExecutor executor) {
		if (!(runnableTask instanceof ManagedRunnableScheduledFuture)) {
			throw new IllegalArgumentException("Runnable argument must be type " + ManagedRunnableScheduledFuture.class.getSimpleName());
		}

		final ManagedRunnableScheduledFuture<UniqueId> managedTask = (ManagedRunnableScheduledFuture<UniqueId>) runnableTask;
		final UniqueId utilityId = managedTask.id();
		logger.finer("Found rejected request %s.", utilityId);

		executionComplete(utilityId);

		// report to statistician
		final Date rejectTimestamp = Util.nowTimestamp();
		final RejectSummary summary = new RejectSummary(utilityId, rejectTimestamp);
		statistician.reportRejectedRequest(summary);

		// TODO: Add this later when Control and ServiceDef componets have been built.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.lifecycle.Startable#start()
	 */
	@Override
	public MessageSummary start() {
		final MessageSummary summary = MessageSummary.SUCCESS; // being optimistic

		try {
			// setup and initialize executor
			final int corePoolSize = configuration.corePoolSize;
			final UtilityContainer utilityContainer = this;
			final ThreadFactory threadFactory = this;
			final RejectedExecutionHandler rejectHandler = this;
			if (logger.isFine()) {
				final Map<String, Object> args = Util.newParameterizedStringEntryMap();
				args.put("core-pool-size", corePoolSize);
				final String paramsStr = Util.createParameterizedStringEntry(args);
				logger.fine("Request thread pool executor about to be instantiated with configuration: %s.", paramsStr);
			}
			schedulingThreadPoolExecutor = new UtilityContainerThreadPoolExecutor(utilityContainer, corePoolSize, threadFactory, rejectHandler,
					statistician);
			schedulingThreadPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false); // this is nonsense
			schedulingThreadPoolExecutor.prestartCoreThread();
			logger.fine("Loaded request thread pool executor and started all %d core threads.", corePoolSize);

			logger.fine("Successfully started request thread container.");
		} catch (final Exception e) {
			final String msg = "Exception occurred while trying to start the request thread container. Message is %s.";
			logger.error(msg, e, e.getMessage());
			// TODO: localize this error string as it will be presented to administrator user
			final MessageSummary messageSummary = new MessageSummary(MessageSummaryStatus.ERROR);
			messageSummary.addMessage(CategoryType.CONCURRENT_CONTAINER_REQUEST.id(), MessageType.SYSTEM_ERROR, msg);
		}

		return summary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.lifecycle.Destroyable#destroy()
	 */
	@Override
	public MessageSummary destroy() {
		final MessageSummary summary = MessageSummary.SUCCESS; // being optimistic

		logger.fine("About to gracefully shutdown request thread pool executor.");
		schedulingThreadPoolExecutor.shutdown();
		if (configuration.doAwaitShutdown) {
			logger.fine("About to wait for request thread pool executor to gracefully shutdown for %d seconds.", configuration.shutdownAwaitInSeconds);
			try {
				final boolean successfullyWaitedAndTerminated = schedulingThreadPoolExecutor.awaitTermination(configuration.shutdownAwaitInSeconds,
						TimeUnit.SECONDS);
				if (successfullyWaitedAndTerminated) {
					logger.fine("Completed waiting for graceful shutdown of thread pool executor for %d seconds.",
							configuration.shutdownAwaitInSeconds);
				} else {
					logger.fine("Timed out while waiting for graceful shutdown of thread pool executor for %d seconds.",
							configuration.shutdownAwaitInSeconds);
					try {
						activeRequestsLock.lock();
						for (final UniqueId activeRequestId : activeRequests.keySet()) {
							requestCancellation(activeRequestId, InterruptType.SHUTDOWN);
						}
					} finally {
						activeRequestsLock.unlock();
					}
				}
			} catch (final InterruptedException e) {
				logger.warn("Thread pool executor was interrupted while awaiting a graceful termination due to the following exception: %s.",
						e.getMessage());
			}
		}

		return summary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.lifecycle.Killable#kill()
	 */
	@Override
	public MessageSummary kill() {
		final MessageSummary summary = MessageSummary.SUCCESS; // being optimistic

		final List<UniqueId> activeRequestsToCleanup = new ArrayList<UniqueId>();
		try {
			activeRequestsLock.lock();
			for (final UniqueId activeRequestId : activeRequests.keySet()) {
				requestCancellation(activeRequestId, InterruptType.KILL);
				activeRequestsToCleanup.add(activeRequestId);
			}
		} finally {
			activeRequestsLock.unlock();
		}

		logger.fine("About to abruptly shutdown request thread pool executor.");
		schedulingThreadPoolExecutor.shutdownNow();
		logger.fine("Completed abruptly calling shutdownNow on thread pool executor.");

		// ThreadPoolExecutor.afterExecute() is not invoked when shutdownNow (a bit strange)
		// is called; therefore we do this manually to cleanup active requests.
		for (final UniqueId activeRequestId : activeRequestsToCleanup) {
			executionComplete(activeRequestId);
		}

		return summary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.state.StateChangeListener#stateChanged(com.valloc.lifecycle.LifecycleState)
	 */
	@Override
	public void stateChanged(final LifecycleState newState) {
		currentState = newState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.config.ConfigurationChangeListener#configurationChanged(com.valloc.config.ChangeEvent)
	 */
	@Override
	public void configurationChanged(final ChangeEvent<UtilityContainerConfiguration> changeEvent) {
		final UtilityContainerConfiguration newConfig = changeEvent.getConfiguration();

		// thread pool executor: core pool size
		final int candidateCorePoolSize = newConfig.corePoolSize;
		final int originalCorePoolSize = configuration.corePoolSize;
		if (candidateCorePoolSize != originalCorePoolSize) {
			schedulingThreadPoolExecutor.setCorePoolSize(candidateCorePoolSize);
			logger.fine("Changed thread pool executor core pool size from %d to %d.", originalCorePoolSize, candidateCorePoolSize);
		}

		configuration = newConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getActiveRequestLog()
	 */
	@Override
	public List<ProcessingRequestSummary> getActiveRequestLog() {
		final List<ProcessingRequestSummary> activeRequestLog = new ArrayList<ProcessingRequestSummary>();

		try {
			activeRequestsLock.lock();
			for (final UniqueId id : activeRequests.keySet()) {
				final RequestTracker wrapper = activeRequests.get(id);
				final UtilityType type = wrapper.type;
				final Date inceptionTimestamp = wrapper.getExecutorWrapper().getInceptionTimestamp();
				final ProcessingRequestSummary summary = new ProcessingRequestSummary(id, type, inceptionTimestamp);
				activeRequestLog.add(summary);
			}
		} finally {
			activeRequestsLock.unlock();
		}

		return activeRequestLog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getTotalRequestsMade()
	 */
	@Override
	public long getTotalRequestsMade() {
		return totalRequestsMade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getBacklogQueueSize()
	 */
	@Override
	public int getScheduledQueueSize() {
		return schedulingThreadPoolExecutor.getQueue().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorActiveCount()
	 */
	@Override
	public int getThreadPoolExecutorActiveCount() {
		return schedulingThreadPoolExecutor.getActiveCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorCompletedTaskCount()
	 */
	@Override
	public long getThreadPoolExecutorCompletedTaskCount() {
		return schedulingThreadPoolExecutor.getCompletedTaskCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorCorePoolSize()
	 */
	@Override
	public int getThreadPoolExecutorCorePoolSize() {
		return schedulingThreadPoolExecutor.getCorePoolSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorLargestPoolSize()
	 */
	@Override
	public int getThreadPoolExecutorLargestPoolSize() {
		return schedulingThreadPoolExecutor.getLargestPoolSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorMaximumPoolSize()
	 */
	@Override
	public int getThreadPoolExecutorMaximumPoolSize() {
		return schedulingThreadPoolExecutor.getMaximumPoolSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorPoolSize()
	 */
	@Override
	public int getThreadPoolExecutorPoolSize() {
		return schedulingThreadPoolExecutor.getPoolSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorTaskCount()
	 */
	@Override
	public long getThreadPoolExecutorTaskCount() {
		return schedulingThreadPoolExecutor.getTaskCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.config.ConfigurationChangeListener#getConfigurationCategoryType()
	 */
	@Override
	public CategoryType getConfigurationCategoryType() {
		return CategoryType.CONCURRENT_CONTAINER_UTILITY;
	}
}
