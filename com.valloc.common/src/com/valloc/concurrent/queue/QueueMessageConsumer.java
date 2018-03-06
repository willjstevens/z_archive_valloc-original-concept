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
public interface QueueMessageConsumer<T> 
{
	public void consume(T message);
}
