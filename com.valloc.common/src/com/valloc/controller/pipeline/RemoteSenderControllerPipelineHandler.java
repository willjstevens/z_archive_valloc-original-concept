/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.controller.AsynchronousClientController;
import com.valloc.controller.ControllerParticipator;
import com.valloc.controller.SingleClientController;
import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.session.Session;

/**
 *
 *
 * @author wstevens
 */
//public class RemoteSenderControllerPipelineHandler extends AbstractControllerPipelineHandler
//public class RemoteSenderControllerPipelineHandler extends AbstractControllerPipelineHandler<SingleClientController>
public class RemoteSenderControllerPipelineHandler extends AbstractControllerPipelineHandler implements ControllerParticipator<SingleClientController>
{
	@Override
	public String name() {
		return RemoteSenderControllerPipelineHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute(final PipelineState pipelineState) {
		final FrameworkRequest request = getFrameworkRequest();
		final FrameworkResponse response = getFrameworkResponse();
		final Session session = request.getSession();
		final CallType callType = request.getCallType();
		if (callType == CallType.REMOTE_SYNCHRONOUS) {
			session.submitSync(request, response);
			getController().setFrameworkResponse(response);
		} else if (callType == CallType.REMOTE_ASYNCHRONOUS) {
			final AsynchronousClientController asyncController = (AsynchronousClientController) getController();
			session.submitAsync(request, response, asyncController);
		}
	}


}
