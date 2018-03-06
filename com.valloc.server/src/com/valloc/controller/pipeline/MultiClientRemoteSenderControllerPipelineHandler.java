/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.domain.system.ClientNode;
import com.valloc.framework.FrameworkManager;

/**
 *
 *
 * @author wstevens
 */
//public class MultiClientRemoteSenderControllerPipelineHandler extends AbstractControllerPipelineHandler
//public class MultiClientRemoteSenderControllerPipelineHandler extends AbstractControllerPipelineHandler<SingleClientController>
//public class MultiClientRemoteSenderControllerPipelineHandler extends AbstractControllerPipelineHandler implements ControllerParticipator<SingleClientController>
public class MultiClientRemoteSenderControllerPipelineHandler<FM extends FrameworkManager, CN extends ClientNode>
extends AbstractMultiControllerPipelineHandler<FM, CN>
{
	@Override
	public String name() {
		return MultiClientRemoteSenderControllerPipelineHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute(final PipelineState pipelineState) {
		
//		getMultiClientRequest().getClientFrameworkRequest(null).getss
		
//		final FrameworkRequest request = getFrameworkRequest();
//		final FrameworkResponse response = getFrameworkResponse();
//		final Session session = request.getSession();
//		final CallType callType = request.getCallType();
//		if (callType == CallType.REMOTE_SYNCHRONOUS) {
//			session.submitSync(request, response);
////			getController().setFrameworkResponse(response);
//			getController().getMultiClientRequest();
//		} else if (callType == CallType.REMOTE_ASYNCHRONOUS) {
//			final AsynchronousClientController asyncController = (AsynchronousClientController) getController();
//			session.submitAsync(request, response, asyncController);
//		}
	}

	@Override
	public void handleException(final Exception e) {
	}

	
}
