/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.queue;

import com.valloc.lifecycle.Startable;
import com.valloc.lifecycle.Stoppable;

/**
 * 
 *
 *
 * @author wstevens
 */
public interface QueueMessageContainer<T> extends Startable, Stoppable 
{
	public boolean produce(T message);
	public void setStopSignal(T stopSignal); 
	public void setQueueMessageConsumer(QueueMessageConsumer<T> consumer);
}
