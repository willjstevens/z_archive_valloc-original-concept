/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.FrameworkResponse;

/**
 *
 *
 * @author wstevens
 */
public class LocalReceiverControllerPipelineHandler extends AbstractControllerPipelineHandler
{

	@Override
	public String name() {
		return LocalReceiverControllerPipelineHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute(final PipelineState pipelineState) {
		forward();

		final FrameworkResponse response = getFrameworkResponse();
		final CallType callType = getFrameworkRequest().getCallType();
		if (callType == CallType.LOCAL_ASYNCHRONOUS) { // call type now should ONLY be this one!
			final FrameworkManager frameworkManager = getFrameworkComponentFactory().getFrameworkManager();
			frameworkManager.signalAsyncControllerOfResponse(response);
		}
	}
}
