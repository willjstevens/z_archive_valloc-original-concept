/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.controller.AsynchronousClientController;
import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;

/**
 *
 *
 * @author wstevens
 */
public class LocalSenderControllerPipelineHandler extends AbstractControllerPipelineHandler
{

	@Override
	public String name() {
		return LocalSenderControllerPipelineHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute(final PipelineState pipelineState) {
		final FrameworkRequest request = getFrameworkRequest();
		final CallType callType = request.getCallType();
		if (callType == CallType.LOCAL_ASYNCHRONOUS) { // call type now should ONLY be this one!
			final FrameworkResponse response = getFrameworkResponse();
			final AsynchronousClientController controller = (AsynchronousClientController) getController();
			final FrameworkManager frameworkManager = getFrameworkComponentFactory().getFrameworkManager();
			frameworkManager.addAsyncController(controller);
			frameworkManager.fireRequestReceived(request, response);
		}
	}
}
