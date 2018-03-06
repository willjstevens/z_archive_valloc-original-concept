/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework.service;

import com.valloc.Command;
import com.valloc.concurrent.request.RequestContainer;
import com.valloc.interrupt.InterruptFuture;
import com.valloc.interrupt.InterruptType;
import com.valloc.service.AbstractCommonService;
import com.valloc.service.CommandDirectory;
import com.valloc.service.ServiceDirectory;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class FrameworkControlServiceBase extends AbstractCommonService implements FrameworkControlService
{
	private RequestContainer requestContainer;

	@Override
	public String name() {
		return ServiceDirectory.FRAMEWORK_CONTROL;
	}

	/* (non-Javadoc)
	 * @see com.valloc.framework.service.FrameworkControlService#interruptRequest(com.valloc.framework.RequestId, com.valloc.framework.InterruptType)
	 */
	@Override
	@Command(name=CommandDirectory.INTERRUPT_REQUEST)
	public Result interruptRequest(final UniqueId requestId, final InterruptType interruptType) {

		final InterruptFuture future = frameworkManager().requestInterrupt(requestId, interruptType);
		final Result result = future.blockForResult();

		// TODO: This should be blocking, correct ??
		//		requestContainer.requestCancellation(requestId, interruptType);

		//		final MessageSummary resSum = result.getMessageSummary();
		//		final MessageSummary servSum = getMessageSummary(); // NPE
		//		servSum.mergeMessages(resSum);

		return result;
	}

	@Override
	public void setRequestContainer(final RequestContainer requestContainer) {
		this.requestContainer = requestContainer;
	}
}
