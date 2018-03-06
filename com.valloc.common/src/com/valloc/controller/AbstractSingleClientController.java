/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.MessageSummary;
import com.valloc.controller.pipeline.ControllerPipelineHandler;
import com.valloc.controller.pipeline.SingleControllerPipelineHandler;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.util.Result;


/**
 *
 *
 * @author wstevens
 */
abstract class AbstractSingleClientController extends AbstractClientController implements SingleClientController
{
	private FrameworkRequest request;
	private FrameworkResponse response;

	@Override
	public void addArgLast(final Object arg) {
		getFrameworkRequest().addCommandArgLast(arg);
	}

	@Override
	public boolean hasReturnValue() {
		return response.hasReturnValue();
	}

	@Override
	public Object getReturnValue() {
		return response.getReturnValue();
	}

	@Override
	public MessageSummary getMessageSummary() {
		return response.getMessageSummary();
	}

	@Override
	public Result summarizeToResult() {
		return new Result(response.getMessageSummary(), response.getReturnValue());
	}
	
	@Override
	public FrameworkRequest getFrameworkRequest() {
		return request;
	}

	private SingleControllerPipelineHandler toSingleControllerPipelineHandler(final ControllerPipelineHandler handler) {
		final Class<? extends SingleControllerPipelineHandler> singClazz = handler.getClass().asSubclass(SingleControllerPipelineHandler.class);
		final SingleControllerPipelineHandler singleControllerHandler = singClazz.cast(handler);
		return singleControllerHandler;
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
