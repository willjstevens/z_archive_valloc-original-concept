/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.MessageSummary;
import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.FrameworkRequest;
import com.valloc.interrupt.InterruptTracker;


/**
 *
 *
 * @author wstevens
 */
//public class FrameworkControllerPipelineHandler extends AbstractControllerPipelineHandler
//public class FrameworkControllerPipelineHandler
//<FM extends FrameworkManager>
//extends AbstractControllerPipelineHandler<SingleClientController, FM>
public class FrameworkControllerPipelineHandler<FM extends FrameworkManager> extends AbstractSingleControllerPipelineHandler<FM>
{
	@Override
	public String name() {
		return FrameworkControllerPipelineHandler.class.getSimpleName();
	}

	@Override
	public void execute(final PipelineState pipelineState) {
		final FrameworkManager frameworkManager = getFrameworkComponentFactory().getFrameworkManager();
		final FrameworkRequest frameworkRequest = getFrameworkRequest();

		frameworkManager.activateRequest(frameworkRequest);
		
		forward();

		// analyze interrupt status ONLY for SYNC invocations
		final CallType callType = frameworkRequest.getCallType();
		final boolean isSyncCall = callType == CallType.LOCAL_SYNCHRONOUS || callType == CallType.REMOTE_SYNCHRONOUS;
		if (isSyncCall) {
			final MessageSummary messageSummary = getFrameworkResponse().getMessageSummary();
			final InterruptTracker interruptTracker = getInterruptTracker();
			if (messageSummary.isInterrupt() && interruptTracker.isJoinableInterrupt()) {
				// hold up and wait for 'real' interrupt request to return fully and signal, so the
				//		target invoking service has escape exception thrown
				interruptTracker.blockForInterruptJoin();
			}
		}

		System.out.println("Falling out of framework controller.");
	}

	@Override
	public void destroy() {
		final FrameworkManager frameworkManager = getFrameworkComponentFactory().getFrameworkManager();
		frameworkManager.deactivateRequest(getFrameworkRequest());
	}

	/* (non-Javadoc)
	 * @see com.valloc.controller.ControllerPipelineHandler#handleException(java.lang.Exception)
	 */
	@Override
	public void handleException(final Exception e) {
		destroy();
	}
}
