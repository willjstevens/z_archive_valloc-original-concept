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
public interface SingleControllerPipelineHandler extends ControllerPipelineHandler
{
	public SingleClientController getController();
	public void setController(SingleClientController controller);
	
	public FrameworkRequest getFrameworkRequest();
	public FrameworkResponse getFrameworkResponse();
	public void setFrameworkRequest(FrameworkRequest request);
	public void setFrameworkResponse(FrameworkResponse response);
	
}
