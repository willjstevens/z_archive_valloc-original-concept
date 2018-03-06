/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.controller.SingleClientController;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractSingleControllerPipelineHandler extends AbstractControllerPipelineHandler implements SingleControllerPipelineHandler
{
	private SingleClientController controller;
	private FrameworkRequest request;
	private FrameworkResponse response;

	@Override
	public void initialize() {
		// override me if necessary
	}

	@Override
	public void destroy() {
		// override me if necessary
	}

	public void forward() {
		controller.getControllerPipelineProcessor().forward();
	}

	@Override
	public SingleClientController getController() {
		return controller;
	}

	@Override
	public void setController(final SingleClientController controller) {
		this.controller = controller;
	}

	@Override
	public FrameworkRequest getFrameworkRequest() {
		return request;
	}

	@Override
	public FrameworkResponse getFrameworkResponse() {
		return response;
	}

	@Override
	public void setFrameworkRequest(final FrameworkRequest request) {
		this.request = request;
	}

	@Override
	public void setFrameworkResponse(final FrameworkResponse response) {
		this.response = response;
	}
}
