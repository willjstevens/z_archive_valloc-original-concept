/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.queue;

/**
 * 
 *
 *
 * @author wstevens
 */
public interface QueueMessageProducer<T>
{
	public void setQueueMessageContainer(QueueMessageContainer<T> container);
}
