/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import java.util.LinkedList;
import java.util.ListIterator;

import com.valloc.CategoryType;
import com.valloc.Executor;
import com.valloc.interrupt.InterruptParticipator;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.lifecycle.Destroyable;
import com.valloc.lifecycle.Initializable;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 *
 *
 * @author wstevens
 */
public class ControllerPipelineProcessor implements Executor, Initializable, Destroyable, InterruptParticipator
{
	private static final Logger logger = LogManager.manager().getLogger(ControllerPipelineProcessor.class, CategoryType.CONTROLLER);
	private final LinkedList<ControllerPipelineHandler> pipelineHandlers = new LinkedList<ControllerPipelineHandler>();
	private ListIterator<ControllerPipelineHandler> pipelineHandlerIterator;
	private final PipelineState pipelineState = new PipelineState();
	private InterruptTracker interruptTracker;


	@Override
	public InterruptTracker getInterruptTracker() {
		return interruptTracker;
	}

	@Override
	public void setInterruptTracker(final InterruptTracker interruptTracker) {
		this.interruptTracker = interruptTracker;
	}


	public void addHandlerLast(final ControllerPipelineHandler handler) {
		pipelineHandlers.add(handler);
	}

	@Override
	public void initialize() {
		// do all befores first
		for (int i = 0, count = pipelineHandlers.size(); i < count; i++) {
			final ControllerPipelineHandler handler = pipelineHandlers.get(i);
			try {
				handler.initialize();
			} catch (final Exception e) {
				handleException(e, handler);
			}
		}
	}

	@Override
	public void execute() {
		pipelineHandlerIterator = pipelineHandlers.listIterator();
		if (pipelineHandlerIterator.hasNext()) {
			final ControllerPipelineHandler firstHandler = pipelineHandlerIterator.next();
			try {
				firstHandler.execute(pipelineState);
			} catch (final Exception e) {
				handleException(e, firstHandler);
			}
		}
	}

	public void forward() {
		if (pipelineHandlerIterator.hasNext()) {
			final ControllerPipelineHandler nextHandler = pipelineHandlerIterator.next();
			try {
				nextHandler.execute(pipelineState);
			} catch (final Exception e) {
				handleException(e, nextHandler);
			}
		}
	}

	@Override
	public void destroy() {
		// do these calls to cleanup, in reverse
		for (int i = pipelineHandlers.size()-1; 0 <= i; i--) {
			final ControllerPipelineHandler handler = pipelineHandlers.get(i);
			try {
				handler.destroy();
			} catch (final Exception e) {
				handleException(e, handler);
			}
		}
	}

	private void handleException(final Exception e, ControllerPipelineHandler handler) {
		final String msg = "An exception escaped the controller pipeline with error: " + e.toString();
		logger.error(msg, e);
		handler.handleException(e); // for current handler that blew up
		// iterate backwards over handlers to backout changes and execution
		while (pipelineHandlerIterator.hasPrevious()) {
			handler = pipelineHandlerIterator.previous();
			handler.handleException(e);
		}

		destroy();
	}

	public PipelineState getPipelineState() {
		return pipelineState;
	}

	public LinkedList<ControllerPipelineHandler> getPipelineHandlers() {
		return pipelineHandlers;
	}
}
