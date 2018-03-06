/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.controller.MultiClientController;
import com.valloc.domain.system.ClientNode;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.MultiClientRequest;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractMultiControllerPipelineHandler
<FM extends FrameworkManager, CN extends ClientNode> 
extends AbstractControllerPipelineHandler<FM> 
implements MultiControllerPipelineHandler<CN>
{
	private MultiClientController<CN> controller;
	private MultiClientRequest<CN> multiClientRequest;
	
	@Override
	public void initialize() {
		// override me if necessary
	}

	@Override
	public void destroy() {
		// override me if necessary
	}

	public void forward() {
//		getController().getControllerPipelineProcessor().forward();
		controller.getControllerPipelineProcessor().forward();
	}

	@Override
	public MultiClientController<CN> getController() {
		return controller;
	}

	@Override
	public void setController(final MultiClientController<CN> controller) {
		this.controller = controller;
	}

	@Override
	public MultiClientRequest<CN> getMultiClientRequest() {
		return multiClientRequest;
	}

	@Override
	public void setMultiClientRequest(final MultiClientRequest<CN> multiClientRequest) {
		this.multiClientRequest = multiClientRequest;
	}
}
