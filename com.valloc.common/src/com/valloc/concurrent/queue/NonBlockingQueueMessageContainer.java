/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.thread.ManagedThread;
import com.valloc.thread.ThreadCategory;
import com.valloc.thread.ThreadManager;

/**
 * 
 * 
 * 
 * @author wstevens
 */
public class NonBlockingQueueMessageContainer<T> implements QueueMessageContainer<T>
{
	private static final Logger logger = LogManager.manager().getLogger(NonBlockingQueueMessageContainer.class, CategoryType.UTILITY);
	private final ThreadManager threadManager;
	private final String name;
	private QueueMessageConsumer<T> consumer;
	private final BlockingQueue<T> queue = new LinkedBlockingQueue<T>();
	private boolean isSpinning;
	private T stopSignal;
	private ManagedThread spinThread;

	/**
	 * @param name
	 */
	public NonBlockingQueueMessageContainer(final String name, final ThreadManager threadManager) {
		this.name = name;
		this.threadManager = threadManager;
	}

	private final Runnable spinRunner = new Runnable() {
		@Override
		public void run() {
			while (isSpinning) {
				try {
					final T message = queue.take();
					if (!stopSignal.equals(message)) {
						consumer.consume(message);
						if (logger.isFiner()) {
							logger.finer("Consumer was sent message " + message);
						}
					}
				} catch (final InterruptedException e) { // log and spin again
					logger.error("Interupt exception while attempting to take a message from the queue.", e);
				}
			}
			if (logger.isFiner()) {
				logger.finer("Found stop signal for queue message container; exiting spin loop.");
				logger.finer("Stopped nonblocking queue message container: " + toString());
			}
		}
	};

	private void spinThread() {
		spinThread = threadManager.newThread(spinRunner, ThreadCategory.QUEUE_MESSAGE_SPINNER, name);
		spinThread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.queue.QueueMessageContainer#produce(java.lang.Object)
	 */
	@Override
	public boolean produce(final T message) {
		boolean retval = false;

		if (isSpinning) {
			try {
				queue.put(message);
				retval = true;
			} catch (final InterruptedException e) {
				logger.error("Interupt exception while attempting to add message to queue.", e);
			}
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.queue.QueueMessageContainer#setQueueMessageConsumer(com.valloc.concurrent.queue.QueueMessageConsumer)
	 */
	@Override
	public void setQueueMessageConsumer(final QueueMessageConsumer<T> consumer) {
		this.consumer = consumer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.lifecycle.Startable#start()
	 */
	@Override
	public void start() {
		isSpinning = true;
		spinThread();
		if (logger.isFiner()) {
			logger.finer("Started nonblocking queue message container: " + toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.lifecycle.Stoppable#stop()
	 */
	@Override
	public void stop() {
		isSpinning = false;
		try {
			queue.put(stopSignal);
		} catch (final InterruptedException e) { // log and swallow
			logger.error("Interrupt exception while attempting to stop queue.", e);
		}
	}

	public void setStopSignal(final T stopSignal) {
		this.stopSignal = stopSignal;
	}

	@Override
	public String toString() {
		return name;
	}
}