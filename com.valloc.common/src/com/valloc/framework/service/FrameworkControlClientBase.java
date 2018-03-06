/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework.service;

import com.valloc.controller.SynchronousClientController;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.interrupt.InterruptType;
import com.valloc.service.AbstractCommonService;
import com.valloc.service.ClientDirectory;
import com.valloc.service.CommandDirectory;
import com.valloc.service.ServiceDirectory;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class FrameworkControlClientBase extends AbstractCommonService implements FrameworkControlClient
{

	@Override
	public String name() {
		return ClientDirectory.FRAMEWORK_CONTROL;
	}

	@Override
	public Result interruptLocalRequest(final UniqueId requestId, final InterruptType interruptType) {
		// here we just submit through via controller and the FrameworkControlServiceBase will take care of
		//		locally interrupting the service through the local framework manager
		final SynchronousClientController controller =
			controllerFactory().newClientLocalSyncController(ServiceDirectory.FRAMEWORK_CONTROL, CommandDirectory.INTERRUPT_REQUEST, this);
		return interruptRequest(controller, requestId, interruptType);
	}

	@Override
	public Result interruptRemoteRequest(final UniqueId requestId, final InterruptType interruptType) {

		final InterruptTracker interruptTracker = frameworkManager().getInterruptTracker(requestId);

		// here we first need to issue the remote interrupt request and block for completion
		final SynchronousClientController controller =
			controllerFactory().newClientRemoteSyncController(ServiceDirectory.FRAMEWORK_CONTROL, CommandDirectory.INTERRUPT_REQUEST, this);
		final Result remoteResult = interruptRequest(controller, requestId, interruptType);
		//		final MessageSummary remoteMessageSummary = remoteResult.getMessageSummary();
		remoteResult.getMessageSummary();

		// now that remote side is done, issue the same for any local pending blocking requests
		//		final Result localResult = interruptLocalRequest(requestId, interruptType);


		// just issue request to interrupt and return out
		frameworkManager().requestInterrupt(requestId, interruptType);
		//		final InterruptFuture future = getFrameworkManager().requestInterrupt(requestId, interruptType);

		// now merge local and remote messages
		//		final MessageSummary cumulativeSummary = localMessageSummary.mergeMessages(remoteMessageSummary);
		//		final Result cumulativeInterruptResult = new Result(cumulativeSummary);

		//		final InterruptTracker interruptTracker = getFrameworkManager().getInterruptTracker(requestId);
		//		interruptTracker.interruptJoinRealizedAndRelease(cumulativeInterruptResult);
		//		return cumulativeInterruptResult;

		interruptTracker.interruptJoinRealizedAndRelease(remoteResult);
		return remoteResult;

	}

	private Result interruptRequest(final SynchronousClientController controller, final UniqueId requestId, final InterruptType interruptType) {
		controller.addArgLast(requestId);
		controller.addArgLast(interruptType);

		controller.submitSync();

		return controller.summarizeToResult();
	}
}
