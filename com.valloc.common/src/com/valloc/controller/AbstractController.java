/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.controller.pipeline.ControllerPipelineProcessor;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.transaction.Transaction;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractController implements Controller
{
	private ControllerPipelineProcessor pipelineProcessor;
	private Transaction transaction;
	private InterruptTracker interruptTracker;

	@Override
	public InterruptTracker getInterruptTracker() {
		return interruptTracker;
	}

	@Override
	public void setInterruptTracker(final InterruptTracker interruptTracker) {
		this.interruptTracker = interruptTracker;
	}

	@Override
	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public void setTransaction(final Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public boolean isTransact() {
		return transaction != null;
	}

	@Override
	public ControllerPipelineProcessor getControllerPipelineProcessor() {
		return pipelineProcessor;
	}

	@Override
	public void setControllerPipelineProcessor(final ControllerPipelineProcessor pipelineProcessor) {
		this.pipelineProcessor = pipelineProcessor;
//		pipelineProcessor.setController(this);
	}

}
