/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.Priority;
import com.valloc.controller.pipeline.ControllerPipelineHandler;
import com.valloc.controller.pipeline.ControllerPipelineProcessor;
import com.valloc.controller.pipeline.SingleControllerPipelineHandler;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.interrupt.InterruptType;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class ServiceControllerBase extends AbstractController implements ServiceController
{
	private FrameworkRequest request;
	private FrameworkResponse response;

	/* (non-Javadoc)
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute() {
		final ControllerPipelineProcessor pipelineProcessor = getControllerPipelineProcessor();
		// submit this doing the one-way through the pipeline handlers
		pipelineProcessor.execute();
		// now destroy each..
		pipelineProcessor.destroy();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Identifiable#id()
	 */
	@Override
	public UniqueId id() {
		return getFrameworkRequest().id();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Prioritizable#getPriority()
	 */
	@Override
	public Priority getPriority() {
		return getFrameworkRequest().getPriority();
	}

	@Override
	public void handleInterrupt(final InterruptType interruptType, final Result result) {
	}

	// TODO: Throw this into utility class
	private SingleControllerPipelineHandler toSingleControllerPipelineHandler(final ControllerPipelineHandler handler) {
		final Class<? extends SingleControllerPipelineHandler> singClazz = handler.getClass().asSubclass(SingleControllerPipelineHandler.class);
		final SingleControllerPipelineHandler singleControllerHandler = singClazz.cast(handler);
		return singleControllerHandler;
	}

	@Override
	public FrameworkRequest getFrameworkRequest() {
		return request;
	}

	@Override
	public void setFrameworkRequest(final FrameworkRequest request) {
		this.request = request;
		for (final ControllerPipelineHandler handler : getControllerPipelineProcessor().getPipelineHandlers()) {
			final SingleControllerPipelineHandler controllerHandler = toSingleControllerPipelineHandler(handler);
			controllerHandler.setFrameworkRequest(request);
		}
	}

	@Override
	public FrameworkResponse getFrameworkResponse() {
		return response;
	}

	@Override
	public void setFrameworkResponse(final FrameworkResponse response) {
		this.response = response;
		for (final ControllerPipelineHandler handler : getControllerPipelineProcessor().getPipelineHandlers()) {
			final SingleControllerPipelineHandler controllerHandler = toSingleControllerPipelineHandler(handler);
			controllerHandler.setFrameworkResponse(response);
		}
	}
}
