/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.session.Session;

/**
 *
 *
 * @author wstevens
 */
public class RemoteReceiverControllerPipelineHandler extends AbstractControllerPipelineHandler
{
	@Override
	public String name() {
		return RemoteReceiverControllerPipelineHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute(final PipelineState pipelineState) {
		forward();

		// now finalize and return back to invoking local/remote client
		final FrameworkRequest frameworkRequest = getFrameworkRequest();
		final FrameworkResponse frameworkResponse = getFrameworkResponse();
		final CallType callType = getFrameworkRequest().getCallType();
		if (callType == CallType.REMOTE_SYNCHRONOUS || callType == CallType.REMOTE_ASYNCHRONOUS) {
			final Session session = frameworkRequest.getSession();
			session.returnResponse(frameworkResponse);
		}
	}
}
