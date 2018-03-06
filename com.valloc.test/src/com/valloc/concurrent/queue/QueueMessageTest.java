/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.queue;

import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.concurrent.queue.NonBlockingQueueMessageContainer;
import com.valloc.concurrent.queue.QueueMessageConsumer;
import com.valloc.concurrent.queue.QueueMessageContainer;
import com.valloc.concurrent.queue.QueueMessageProducer;
import com.valloc.concurrent.spinner.ConditionTriggerSpinner;
import com.valloc.concurrent.spinner.SpinHandler;
import com.valloc.concurrent.spinner.Spinner;
import com.valloc.concurrent.spinner.TriggerSpinner;
import com.valloc.thread.BaseThreadManager;
import com.valloc.thread.ThreadManager;

/**
 * 
 *
 *
 * @author wstevens
 */
public class QueueMessageTest extends AbstractTest
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

	/**
	 * Basic usage of the queue container structure and interaction.
	 */
	@Test
	public void queueMessage_basic()
	{
		final String expected = "Hello this is queue message.";
		final BasicQueueMessageProducer<String> producer = new BasicQueueMessageProducer<String>();
		final BasicQueueMessageContainer<String> container = new BasicQueueMessageContainer<String>();
		final BasicQueueMessageConsumer<String> consumer = new BasicQueueMessageConsumer<String>();
		producer.setQueueMessageContainer(container);
		container.setQueueMessageConsumer(consumer);
		
		producer.publish(expected);
		final String actual = consumer.captured;
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Tests usage of the non-blocking queue container implementation.
	 */
	@Test
	public void queueMessage_nonBlocking()
	{
		final TriggerSpinner spinner = new ConditionTriggerSpinner(new SpinHandler() {
			@Override public void handleSpinIteration(final Spinner spinner) {
				spinner.stop();
			}
		});
		
		final String expected = "Hello this is queue message.";
		final BasicQueueMessageProducer<String> producer = new BasicQueueMessageProducer<String>();
		final QueueMessageContainer<String> container = new NonBlockingQueueMessageContainer<String>("Nonblocking Queue Message Container", threadManager);
		final BasicNonBlockingQueueMessageConsumer<String> consumer = new BasicNonBlockingQueueMessageConsumer<String>(spinner);
		producer.setQueueMessageContainer(container);
		container.setQueueMessageConsumer(consumer);
		container.setStopSignal("STOP");
		container.start();		
		
		producer.publish(expected);		
		spinner.spinAndWait();
		
		final String actual = consumer.captured;
		Assert.assertEquals(expected, actual);
		container.stop();
	}
	
	/**
	 * Tests heavy load of the non-blocking queue message container. 
	 */
	@Test
	public void queueMessage_load()
	{
		final TriggerSpinner spinner = new ConditionTriggerSpinner(new SpinHandler() {
			@Override public void handleSpinIteration(final Spinner spinner) {
				spinner.stop();
			}
		});
		
		final int loadCount = 10000;
		final BasicQueueMessageProducer<String> producer = new BasicQueueMessageProducer<String>();
		final QueueMessageContainer<String> container = new NonBlockingQueueMessageContainer<String>("Nonblocking Queue Message Container", threadManager);
		final HeavyLoadNonBlockingQueueMessageConsumer<String> consumer = new HeavyLoadNonBlockingQueueMessageConsumer<String>(spinner, loadCount);
		producer.setQueueMessageContainer(container);
		container.setQueueMessageConsumer(consumer);
		container.setStopSignal("STOP");
		container.start();

		final String message = "Hello this is queue message.";
		for (int i = 0; i < loadCount; i++) {
			producer.publish(message);
		}	
		spinner.spinAndWait();
		
		final int actual = consumer.hitCount.get();
		Assert.assertEquals(loadCount, actual);
		container.stop();
	}
	
	private static class BasicQueueMessageProducer<T> implements QueueMessageProducer<T>
	{
		private QueueMessageContainer<T> container;
		private void publish(final T message) {
			container.produce(message);
		}
		@Override public void setQueueMessageContainer(final QueueMessageContainer<T> container) {
			this.container = container;
		}
	}
	
	private static class BasicQueueMessageContainer<T> implements QueueMessageContainer<T>
	{
		private QueueMessageConsumer<T> consumer;
		@Override public boolean produce(final T message) {
			consumer.consume(message);
			return true;
		}
		@Override public void setQueueMessageConsumer(final QueueMessageConsumer<T> consumer) {
			this.consumer = consumer;
		}
		@Override public void start() {}
		@Override public void stop() {}
		@Override public void setStopSignal(final T stopSignal) {}
	}
	
	private static class BasicQueueMessageConsumer<T> implements QueueMessageConsumer<T>
	{
		T captured;
		@Override public void consume(final T message) {
			captured = message;
		}
	}

	private static class BasicNonBlockingQueueMessageConsumer<T> implements QueueMessageConsumer<T>
	{
		private final TriggerSpinner spinner;
		private BasicNonBlockingQueueMessageConsumer(final TriggerSpinner spinner){
			this.spinner = spinner;
		}

		T captured;
		@Override public void consume(final T message) {
			captured = message;
			spinner.pullTrigger();
		}
	}

	private static class HeavyLoadNonBlockingQueueMessageConsumer<T> implements QueueMessageConsumer<T>
	{
		private final AtomicInteger hitCount = new AtomicInteger();
		private final TriggerSpinner spinner;
		private final int threshold;
		private HeavyLoadNonBlockingQueueMessageConsumer(final TriggerSpinner spinner, final int threshold){
			this.threshold = threshold;
			this.spinner = spinner;
		}

		@Override public void consume(final T message) {
			if (hitCount.incrementAndGet() == threshold) {
				spinner.pullTrigger();
			}
		}
	}
}