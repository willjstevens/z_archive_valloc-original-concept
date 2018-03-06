/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.valloc.MessageSummary;
import com.valloc.controller.pipeline.ControllerPipelineProcessor;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.interrupt.InterruptType;
import com.valloc.util.Result;


/**
 *
 *
 * @author wstevens
 */
public class AsynchronousClientControllerBase extends AbstractSingleClientController implements AsynchronousClientController
{
	private Lock lock;
	private Condition condition;
	private boolean isWaitingForResponse;

	/* (non-Javadoc)
	 * @see com.valloc.controller.AsynchronousClientController#submitAsync()
	 */
	@Override
	public void submitAsync() {
		// setup resources for timing aspect
		lock = new ReentrantLock(true);
		condition = lock.newCondition();

		// submit standard execute which does the one-way through each pipeline handler
		final ControllerPipelineProcessor pipelineProcessor = getControllerPipelineProcessor();
		// this allows the full request write to occur and returns before any response is received
		//		and exiting back out to client code...
		pipelineProcessor.execute();

		isWaitingForResponse = true;
	}

	/* (non-Javadoc)
	 * @see com.valloc.controller.AsynchronousClientController#blockForCompletion()
	 */
	@Override
	public void blockForCompletion() {
		if (isWaitingForResponse) {
			try {
				lock.lock();
				while (isWaitingForResponse) {
					condition.awaitUninterruptibly();
				}
			} finally {
				lock.unlock();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.valloc.controller.AsynchronousClientController#isBlocking()
	 */
	@Override
	public boolean isBlocking() {
		return isWaitingForResponse;
	}

	/*
	 * (non-Javadoc)
	 * @see com.valloc.controller.AsynchronousClientController#signalCompletion()
	 */
	@Override
	public void signalCompletion() {
		// check first and block for if this is an interrupt and to be joined with interrupt initiater
		final MessageSummary messageSummary = getFrameworkResponse().getMessageSummary();
		final InterruptTracker interruptTracker = getInterruptTracker();
		if (messageSummary.isInterrupt() && interruptTracker.isJoinableInterrupt()) {
			// hold up and wait for 'real' interrupt request to return fully and signal, so the
			//		target invoking service has escape exception thrown
			interruptTracker.blockForInterruptJoin();
		}

		// regardless of interrupt or not, destroy handlers and resources
		getControllerPipelineProcessor().destroy();
		// now proceed with setting state and notification back to client code
		try {
			lock.lock();
			isWaitingForResponse = false;
			// this will allow client service code to progress, but should do diligence to check
			//		interrupt status immediately after
			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void handleInterrupt(final InterruptType interruptType, final Result result) {
	}
}
