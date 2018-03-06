/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.controller.MultiClientController;
import com.valloc.domain.system.ClientNode;
import com.valloc.framework.MultiClientRequest;

/**
 *
 *
 * @author wstevens
 */
public interface MultiControllerPipelineHandler<CN extends ClientNode> extends ControllerPipelineHandler
{
	public MultiClientController<CN> getController();
	public void setController(MultiClientController<CN> controller);
	public MultiClientRequest<CN> getMultiClientRequest();
	public void setMultiClientRequest(MultiClientRequest<CN> multiClientRequest);
}
