/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
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
import com.valloc.Priority;
import com.valloc.config.ChangeEvent;
import com.valloc.framework.FrameworkExecutor;
import com.valloc.framework.FrameworkManager;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.interrupt.InterruptType;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.thread.ThreadCategory;
import com.valloc.thread.ThreadManager;
import com.valloc.util.BoundedPriorityBlockingQueue;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;

/**
 * 
 * 
 * @author wstevens
 */
public class BaseRequestContainer implements RequestContainer
{
	private static final Logger logger = LogManager.manager().getLogger(BaseRequestContainer.class, CategoryType.CONCURRENT_CONTAINER_REQUEST);

	// standard
	private ThreadPoolExecutor threadPoolExecutor;
	private BoundedPriorityBlockingQueue<PrioritizableRunnable> backlogQueue;
	private LifecycleState currentState;
	private RequestContainerConfiguration configuration;
	/* A long here should do, shan't exceed the positive bound within a single JVM process's uptime: */
	private long requestCount;
	private final ThreadManager threadManager;
	private final Lock fairAccessLock = new ReentrantLock(true);
	private final Map<UniqueId, RequestTrackingWrapper> queuedRequests = new HashMap<UniqueId, RequestTrackingWrapper>();
	private final Map<UniqueId, RequestTrackingWrapper> activeRequests = new HashMap<UniqueId, RequestTrackingWrapper>();
	private final Lock queuedRequestsLock = new ReentrantLock(false);
	private final Lock activeRequestsLock = new ReentrantLock(false);
	private final RequestContainerStatistician statistician = new RequestContainerStatistician();
	private FrameworkManager frameworkManager;

	BaseRequestContainer(final RequestContainerConfiguration configuration, final ThreadManager threadManager) {
		this.configuration = configuration;
		this.threadManager = threadManager;
		statistician.setThreadContainer(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.container.ThreadContainer#queueForExecution(com.valloc.FrameworkExecutor)
	 */
	@SuppressWarnings(Constants.UNCHECKED)
	@Override
	public void queueForExecution(final FrameworkExecutor requestExecutor) {
		try {
			fairAccessLock.lock();

			if (!currentState.equals(LifecycleState.ACTIVE)) {
				throw new IllegalStateException("Invalid state for thread container to submit a request.");
			}
			
			// setup and submit internal executor wrapper
			final UniqueId requestId = requestExecutor.id();
//			final InterruptTracker interruptTracker = frameworkManager.createAndRegisterInterruptTracker(requestId);
			final InterruptTracker interruptTracker = requestExecutor.getInterruptTracker();
			final Date inceptionTimestamp = Util.nowTimestamp();
			final long containerId = ++requestCount;
			final ExecutorWrapper callableExecutor = new ExecutorWrapper(requestExecutor, containerId);
			callableExecutor.setTimestamp(inceptionTimestamp);
			callableExecutor.setInterruptTracker(interruptTracker);
			
			try { // Here we detect whether or not request has already been queued:
				activeRequestsLock.lock();
				// for efficiency, first try active requests first as most often all submitted requests will immediately
				// become active and stay active throughout processing
				final boolean requestIdNotActive = !activeRequests.containsKey(requestId);
				if (requestIdNotActive) {
					try {
						queuedRequestsLock.lock();
						final boolean requestIdQueued = queuedRequests.containsKey(requestId);
						if (requestIdQueued) {
							throw new IllegalStateException(String.format(
									"Request %s is already in queue for execution; cannot resubmit same request.", requestId));
						}
					} finally {
						queuedRequestsLock.unlock();
					}
				} else {
					throw new IllegalStateException(String.format("Request %s is already active; cannot resubmit same request.", requestId));
				}
			} finally {
				activeRequestsLock.unlock();
			}

			// For tracking setup and log queued request
			RequestTrackingWrapper wrapper = new RequestTrackingWrapper(requestId, callableExecutor, inceptionTimestamp);
//			RequestTrackingWrapper wrapper = new RequestTrackingWrapper(requestId, inceptionTimestamp);
			try {
				queuedRequestsLock.lock();
				queuedRequests.put(requestId, wrapper);
				logger.finer("Stored reference to queued request %s.", requestId);
			} finally {
				queuedRequestsLock.unlock();
			}

			// Finally, fire request!
			final Future<UniqueId> future = threadPoolExecutor.submit(callableExecutor);
			logger.finer("Submitted request %s into thread pool executor.", requestId);

			try { // In case executionBeginning() which sets task reference and moves from queued bucket into active
				// bucket has not been called first, we retrieve reference here if available and set task ref.
				queuedRequestsLock.lock();
				wrapper = queuedRequests.get(requestId);
				if (wrapper != null) { // could already be null if race condition of executionBeginning occurred first
					wrapper.task = ManagedFutureTask.class.cast(future);
					logger.finer("Found queued request %s after submitting to executor and now assigning future runnable task object.", requestId);
				}
			} finally {
				queuedRequestsLock.unlock();
			}
		} finally {
			fairAccessLock.unlock();
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
	public void requestCancellation(final UniqueId requestId, final InterruptType interruptType) {
		final RequestTrackingWrapper wrapper = activeRequests.get(requestId);

		if (wrapper != null) {
			final ManagedFutureTask<UniqueId> managedFutureTask = wrapper.task;
			final ExecutorWrapper executorWrapper = wrapper.executorWrapper;
			executorWrapper.setCancelType(interruptType);

			logger.finer("Attempting to cancel request %s.", requestId);
			final boolean wasCancelled = managedFutureTask.cancel(true); // always interrupt if running
			if (wasCancelled) {
				logger.finer("Cancel request made for %s.", requestId);
			} else {
				logger.finer("Failed to cancel request %s.", requestId);
			}
		} else {
			logger.finer("Could not cancel request %s since it was not found.", requestId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#takeThreadContainerSnapshot()
	 */
	@Override
	public RequestContainerSnapshot takeThreadContainerSnapshot() {
		return statistician.takeThreadContainerSnapshot();
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
	 * @see com.valloc.concurrent.request.RequestContainer#executionBeginning(com.valloc.UniqueId, com.valloc.concurrent.request.ManagedFutureTask)
	 */
	@Override
	public void executionBeginning(final UniqueId requestId, final ManagedFutureTask<UniqueId> task) {
		try {
			logger.finer("About to promote request %s from queued state into active state; waiting for lock.", requestId);
			activeRequestsLock.lock(); // active goes first since it contains elements which are typically longer lasting and more interative
			queuedRequestsLock.lock();

			// move element from queued bucket into active bucket
			RequestTrackingWrapper wrapper = queuedRequests.remove(requestId);
			if (wrapper != null) {
				logger.finer("Found request tracking wrapper object for request %s.", requestId);
			} else {
				wrapper = new RequestTrackingWrapper(requestId, task.getExecutorWrapper(), null);
//				wrapper = new RequestTrackingWrapper(requestId, null);
				logger.finer("Did not find request tracking wrapper object for request %s.", requestId);
			}
			wrapper.task = task;
			wrapper.markedTimestamp = Util.nowTimestamp(); // this is the made-active timestamp
			activeRequests.put(wrapper.requestId, wrapper);
			logger.finer("Promoted request tracking wrapper object to active state for request %s.", requestId);

		} finally {
			queuedRequestsLock.unlock(); // backout in reverse order to not hold up other threads after first release
			activeRequestsLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.container.ThreadContainer#executionComplete(java.util.UUID)
	 */
	@Override
	public void executionComplete(final UniqueId requestId) {
		try {
			logger.finer("Complete request %s is about to be removed from active state; waiting for lock.", requestId);
			activeRequestsLock.lock();
			activeRequests.remove(requestId);
			logger.finer("Done removing request %s from active state.", requestId);
		} finally {
			activeRequestsLock.unlock();
		}
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
		final String name = String.format("%s Request Thread", getClass().getSimpleName());
		newThread = threadManager.newThread(runnable, category, name);

		return newThread;
	}

	private final class RequestTrackingWrapper {
		UniqueId requestId;
		Date markedTimestamp;
		ExecutorWrapper executorWrapper;
		ManagedFutureTask<UniqueId> task;

		RequestTrackingWrapper(final UniqueId requestId, final ExecutorWrapper executorWrapper, final Date markedTimestamp) {
			this.requestId = requestId;
			this.executorWrapper = executorWrapper;
			this.markedTimestamp = markedTimestamp;
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
		if (!(runnableTask instanceof ManagedFutureTask)) {
			throw new IllegalArgumentException("Runnable argument must be type " + ManagedFutureTask.class.getSimpleName());
		}

		final ManagedFutureTask<UniqueId> managedTask = (ManagedFutureTask<UniqueId>) runnableTask;
		final UniqueId requestId = managedTask.id();
		logger.finer("Found rejected request %s.", requestId);

		try { // remove if queued
			queuedRequestsLock.lock();
			final RequestTrackingWrapper wrapper = queuedRequests.remove(requestId);
			if (logger.isFiner()) {
				if (wrapper != null) {
					logger.finer("Successfully removed queued request %s.", requestId);
				} else {
					logger.finer("Could not remove queued request %s.", requestId);
				}
			}
		} finally {
			queuedRequestsLock.unlock();
		}

		// report to statistician
		final Date rejectTimestamp = Util.nowTimestamp();
		final RejectSummary summary = new RejectSummary(requestId, rejectTimestamp);
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
			final int maximumPoolSize = configuration.maxPoolSize;
			final long excessThreadTimeout = configuration.excessThreadTimeoutInSeconds;
			final TimeUnit timeUnit = TimeUnit.SECONDS;
			final int queueBoundSize = configuration.queueBoundSize;
			backlogQueue = new BoundedPriorityBlockingQueue<PrioritizableRunnable>(queueBoundSize);
			final RequestContainer requestContainer = this;
			final ThreadFactory threadFactory = this;
			final RejectedExecutionHandler rejectHandler = this;
			if (logger.isFine()) {
				final Map<String, Object> args = Util.newParameterizedStringEntryMap();
				args.put("core-pool-size", corePoolSize);
				args.put("max-pool-size", maximumPoolSize);
				args.put("excess-thread-timeout-seconds", excessThreadTimeout);
				args.put("queue-bound-size", queueBoundSize);
				final String paramsStr = Util.createParameterizedStringEntry(args);
				logger.fine("Request thread pool executor about to be instantiated with configuration: %s.", paramsStr);
			}
			threadPoolExecutor = 
				new RequestContainerThreadPoolExecutor(
							requestContainer,
							corePoolSize, 
							maximumPoolSize, 
							excessThreadTimeout,
							timeUnit, 
							backlogQueue, 
							threadFactory, 
							rejectHandler, 
							statistician);
							threadPoolExecutor.prestartCoreThread();
			((RequestContainerThreadPoolExecutor)threadPoolExecutor).setFrameworkManager(frameworkManager);
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
		threadPoolExecutor.shutdown();
		if (configuration.doAwaitShutdown) {
			logger
					.fine("About to wait for request thread pool executor to gracefully shutdown for %d seconds.",
							configuration.shutdownAwaitInSeconds);
			try {
				final boolean successfullyWaitedAndTerminated = threadPoolExecutor.awaitTermination(configuration.shutdownAwaitInSeconds,
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
				logger.warn("Thread pool executor was interrupted while awaiting a graceful termination due to the following exception: %s.", e
						.getMessage());
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
		threadPoolExecutor.shutdownNow();
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
	public void configurationChanged(final ChangeEvent<RequestContainerConfiguration> changeEvent) {
		final RequestContainerConfiguration newConfig = changeEvent.getConfiguration();

		// thread pool executor: core pool size
		final int candidateCorePoolSize = newConfig.corePoolSize;
		final int originalCorePoolSize = configuration.corePoolSize;
		if (candidateCorePoolSize != originalCorePoolSize) {
			threadPoolExecutor.setCorePoolSize(candidateCorePoolSize);
			logger.fine("Changed thread pool executor core pool size from %d to %d.", originalCorePoolSize, candidateCorePoolSize);
		}

		// thread pool executor: max pool size
		final int candidateMaxPoolSize = newConfig.maxPoolSize;
		final int originalMaxPoolSize = configuration.maxPoolSize;
		if (candidateMaxPoolSize != originalMaxPoolSize) {
			threadPoolExecutor.setMaximumPoolSize(candidateMaxPoolSize);
			logger.fine("Changed thread pool executor max pool size from %d to %d.", originalMaxPoolSize, candidateMaxPoolSize);
		}

		// thread pool executor: excess thread timeout
		final int candidateExcessThreadTimeoutInSeconds = newConfig.excessThreadTimeoutInSeconds;
		final int originalExcessThreadTimeoutInSeconds = configuration.excessThreadTimeoutInSeconds;
		if (candidateExcessThreadTimeoutInSeconds != originalExcessThreadTimeoutInSeconds) {
			threadPoolExecutor.setKeepAliveTime(candidateExcessThreadTimeoutInSeconds, TimeUnit.SECONDS);
		}

		// thread pool executor: backlog queue bound capacity
		final int candidateQueueBoundSize = newConfig.queueBoundSize;
		final int originalQueueBoundSize = configuration.queueBoundSize;
		if (candidateQueueBoundSize != originalQueueBoundSize) {
			backlogQueue.setCapacity(candidateQueueBoundSize);
			logger.fine("Changed thread pool executor backlog queue capacity from %d to %d.", originalQueueBoundSize, candidateQueueBoundSize);
		}

		configuration = newConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getActiveRequests()
	 */
	@Override
	public List<ProcessingRequestSummary> getQueuedRequestLog() {
		final List<ProcessingRequestSummary> queuedRequestLog = new ArrayList<ProcessingRequestSummary>();

		try {
			queuedRequestsLock.lock();
			for (final UniqueId id : queuedRequests.keySet()) {
				final RequestTrackingWrapper wrapper = queuedRequests.get(id);
				final ManagedFutureTask<UniqueId> task = wrapper.task;
				final Priority priority = task.getPriority();
				final Date inceptionTimestamp = wrapper.markedTimestamp;
				final ProcessingRequestSummary summary = new ProcessingRequestSummary(id, priority, inceptionTimestamp);
				queuedRequestLog.add(summary);
			}
		} finally {
			queuedRequestsLock.unlock();
		}

		return queuedRequestLog;
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
				final RequestTrackingWrapper wrapper = activeRequests.get(id);
				final ManagedFutureTask<UniqueId> task = wrapper.task;
				final Priority priority = task.getPriority();
				final Date madeActiveTimestamp = wrapper.markedTimestamp;
				final ProcessingRequestSummary summary = new ProcessingRequestSummary(id, priority, madeActiveTimestamp);
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
		return requestCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getBacklogQueueSize()
	 */
	@Override
	public int getBacklogQueueSize() {
		return threadPoolExecutor.getQueue().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorActiveCount()
	 */
	@Override
	public int getThreadPoolExecutorActiveCount() {
		return threadPoolExecutor.getActiveCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorCompletedTaskCount()
	 */
	@Override
	public long getThreadPoolExecutorCompletedTaskCount() {
		return threadPoolExecutor.getCompletedTaskCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorCorePoolSize()
	 */
	@Override
	public int getThreadPoolExecutorCorePoolSize() {
		return threadPoolExecutor.getCorePoolSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorKeepAliveTime()
	 */
	@Override
	public long getThreadPoolExecutorKeepAliveTime() {
		return threadPoolExecutor.getKeepAliveTime(TimeUnit.SECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorLargestPoolSize()
	 */
	@Override
	public int getThreadPoolExecutorLargestPoolSize() {
		return threadPoolExecutor.getLargestPoolSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorMaximumPoolSize()
	 */
	@Override
	public int getThreadPoolExecutorMaximumPoolSize() {
		return threadPoolExecutor.getMaximumPoolSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorPoolSize()
	 */
	@Override
	public int getThreadPoolExecutorPoolSize() {
		return threadPoolExecutor.getPoolSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.request.RequestContainer#getThreadPoolExecutorTaskCount()
	 */
	@Override
	public long getThreadPoolExecutorTaskCount() {
		return threadPoolExecutor.getTaskCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.config.ConfigurationChangeListener#getConfigurationCategoryType()
	 */
	@Override
	public CategoryType getConfigurationCategoryType() {
		return CategoryType.CONCURRENT_CONTAINER_REQUEST;
	}

	@Override
	public void setFrameworkManager(final FrameworkManager frameworkManager) {
		this.frameworkManager = frameworkManager;
	}
}
