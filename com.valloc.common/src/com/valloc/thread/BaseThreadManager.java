/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.valloc.CategoryType;
import com.valloc.Constants;
import com.valloc.Priority;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.util.Builder;
import com.valloc.util.Util;

/**
 * 
 * 
 * @author wstevens
 */
public final class BaseThreadManager implements ThreadManager
{
	private static final Logger logger = LogManager.manager().getLogger(BaseThreadManager.class, CategoryType.CONCURRENT);

	private static final boolean DEFAULT_DAEMON_THREAD_POLICY = false;
	private static final String THREAD_NAME_PREFIX = Constants.VALLOC.toUpperCase() + " Thread: ";
	private static final float DEFAULT_LOAD_FACTOR_FRACTION = 1.33f; // maintains .75 load factor on expected thread count
	private static final ThreadManager threadManager = new BaseThreadManager();

	private final EnumMap<ThreadCategory, Map<Long, ManagedThread>> managedThreads = new EnumMap<ThreadCategory, Map<Long, ManagedThread>>(
			ThreadCategory.class);
	private final Set<ManagedThread> interruptedThreads = new HashSet<ManagedThread>();
	private final List<InterruptionSummary> interruptionLog = Collections.synchronizedList(new ArrayList<InterruptionSummary>());
	private final List<ExceptionSummary> exceptionLog = Collections.synchronizedList(new ArrayList<ExceptionSummary>());
	private final Lock threadsLock = new ReentrantLock(true);
	private final Lock interruptedThreadsLock = new ReentrantLock(true);

	private BaseThreadManager() {
	}

	public static ThreadManager getInstance() {
		return threadManager;
	}

	@Override
	public ThreadBuilder newThreadBuilder(final Runnable implementation, final ThreadCategory threadCategory, final String name) {
		return new ThreadBuilder(implementation, threadCategory, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.util.concurrent.ThreadManager#newThread(java.lang.Runnable, com.valloc.util.concurrent.ThreadCategory, java.lang.String, int)
	 */
	@Override
	public ManagedThread newThread(final Runnable implementation, final ThreadCategory threadCategory, final String name) {
		ManagedThread retval = null;

		final String fullName = THREAD_NAME_PREFIX + name;
		retval = new ManagedThread(implementation, fullName, threadCategory, this);
		retval.setUncaughtExceptionHandler(this); // set by default to be manager

		try {
			threadsLock.lock();
			Map<Long, ManagedThread> threadCategoryThreads = managedThreads.get(threadCategory);
			if (threadCategoryThreads == null) {
				final int initialCapacity = (int) (threadCategory.expectedThreadCount() * DEFAULT_LOAD_FACTOR_FRACTION); // truncate float
				threadCategoryThreads = new ConcurrentHashMap<Long, ManagedThread>(initialCapacity);
				managedThreads.put(threadCategory, threadCategoryThreads);
				if (logger.isFiner()) {
					logger.finer("Adding thread category map for category " + threadCategory);
				}
			}

			threadCategoryThreads.put(retval.getId(), retval);
			if (logger.isFiner()) {
				logger.finer("Added managed thread: " + retval);
			}
		} finally {
			threadsLock.unlock();
		}

		return retval;
	}

	public final class ThreadBuilder implements Builder<ManagedThread>
	{
		private final ManagedThread retval;
		private Priority priority = Priority.USER_STANDARD;
		private boolean isDaemon = DEFAULT_DAEMON_THREAD_POLICY;
		private UncaughtExceptionHandler handler;

		private ThreadBuilder(final Runnable implementation, final ThreadCategory threadCategory, final String name) {
			retval = newThread(implementation, threadCategory, name);
		}

		@Override
		public ManagedThread build() {
			if (handler != null) {
				retval.setUncaughtExceptionHandler(handler);
			}
			if (priority != null) {
				retval.setPriority(priority.level());
			}
			retval.setDaemon(isDaemon);

			return retval;
		}

		public ThreadBuilder setPriority(final Priority priority) {
			this.priority = priority;
			return this;
		}

		public ThreadBuilder setDaemon(final boolean isDaemon) {
			this.isDaemon = isDaemon;
			return this;
		}

		public ThreadBuilder setUncaughtExceptionHandler(final UncaughtExceptionHandler handler) {
			this.handler = handler;
			return this;
		}
	}

	@Override
	public void removeThread(final ManagedThread managedThread) {
		try {
			threadsLock.lock();
			final ThreadCategory threadCategory = managedThread.getThreadCategory();
			final long id = managedThread.getId();
			final Map<Long, ManagedThread> threadCategoryMap = managedThreads.get(threadCategory);
			final ManagedThread removed = threadCategoryMap.remove(id);
			if (logger.isFiner()) {
				logger.finer("Removed managed thread: " + removed);
			}

			if (threadCategoryMap.isEmpty()) {
				managedThreads.remove(threadCategory);
				if (logger.isFiner()) {
					logger.finer("Removed thread category map for category " + threadCategory);
				}
			}

			boolean wasInterrupted = false;
			interruptedThreadsLock.lock();
			try {
				wasInterrupted = interruptedThreads.remove(managedThread);
			} finally {
				interruptedThreadsLock.unlock();
			}
			if (logger.isFiner() && wasInterrupted) {
				logger.finer("Removed interrupted thread: " + managedThread);
			}
		} finally {
			threadsLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.ThreadManager#getThreadManagerSnapshot()
	 */
	@Override
	public ThreadManagerSnapshot takeThreadManagerSnapshot() {
		final ThreadManagerSnapshot retval = new ThreadManagerSnapshot(Util.nowTimestamp());

		threadsLock.lock();
		try {
			for (final ThreadCategory category : managedThreads.keySet()) {
				retval.addThreadCategory(category);
				final Map<Long, ManagedThread> categoryThreads = managedThreads.get(category);
				for (final ManagedThread managedThread : categoryThreads.values()) {
					final long tid = managedThread.getId();
					final String name = managedThread.getName();
					final int priority = managedThread.getPriority();
					final boolean isAlive = managedThread.isAlive();
					final boolean isInterrupted = managedThread.isInterrupted();
					final Thread.State state = managedThread.getState();
					final Date creationTimestamp = managedThread.getCreationTimestamp();
					final ThreadSummary threadSummary = new ThreadSummary(tid, name, priority, isAlive, isInterrupted, state, creationTimestamp);
					threadSummary.setStartTimestamp(managedThread.getStartTimestamp()); // could be null
					threadSummary.setRunCompleteTimestamp(managedThread.getRunCompleteTimestamp()); // could be null
					threadSummary.setInterruptTimestamp(managedThread.getInterruptTimestamp()); // could be null
					retval.addCategoryThread(category, threadSummary);
				}
			}
			retval.addAllInterruptionSummaries(interruptionLog);
			retval.addAllExceptionSummaries(exceptionLog);
		} finally {
			threadsLock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(final Thread thread, final Throwable t) {
		if (!(thread instanceof ManagedThread)) {
			throw new IllegalArgumentException("Invalide thread reported for our exception handler."); // how'd this get in here !?!?
		}

		logger.error(String.format("Found uncaught throwable in thread %s with reason %s.", thread, t.getMessage()), t);

		reportThrowable(((ManagedThread) thread), t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.ThreadManager#reportNonHandledThrowable(com.valloc.concurrent.ManagedThread, java.lang.Throwable)
	 */
	@Override
	public synchronized void reportThrowable(final ManagedThread thread, final Throwable t) {
		try {
			final long tid = thread.getId();
			final String threadName = thread.getName();
			final String message = t.getMessage();
			final String toString = t.toString();
			final Date caughtTimestamp = Util.nowTimestamp();
			final ExceptionSummary exceptionSummery = new ExceptionSummary(tid, threadName, message, toString, caughtTimestamp);

			threadsLock.lock();
			logger.error("Thread manager reporting exception for managed thread: " + thread, t);
			exceptionLog.add(exceptionSummery);
		} finally {
			threadsLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.ThreadManager#reportInterrupted(com.valloc.concurrent.ManagedThread)
	 */
	@Override
	public void reportInterrupted(final ManagedThread thread) {
		final long tid = thread.getId();
		final String threadName = thread.getName();
		final Date interruptTimestamp = Util.nowTimestamp();
		final InterruptionSummary interruptionSummary = new InterruptionSummary(tid, threadName, interruptTimestamp);

		try {
			interruptedThreadsLock.lock();
			logger.finer("Thread manager reporting interruption for managed thread: " + thread);
			interruptedThreads.add(thread);
			interruptionLog.add(interruptionSummary);
		} finally {
			interruptedThreadsLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.ThreadManager#purge()
	 */
	@Override
	public void purge() {
		threadsLock.lock();
		try {
			managedThreads.clear();
		} finally {
			threadsLock.unlock();
		}

		interruptedThreadsLock.lock();
		try {
			interruptedThreads.clear();
		} finally {
			interruptedThreadsLock.unlock();
		}

		interruptionLog.clear();
		exceptionLog.clear();
	}
}
