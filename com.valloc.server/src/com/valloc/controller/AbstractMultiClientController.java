/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.MessageSummary;
import com.valloc.domain.system.ClientNode;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.framework.MultiClientRequest;


/**
 *
 *
 * @author wstevens
 */
abstract class AbstractMultiClientController<C extends ClientNode> extends AbstractClientController implements MultiClientController<C>
{
	private MultiClientRequest<C> multiClientRequest;

	@Override
	public MultiClientRequest<C> getMultiClientRequest() {
		return multiClientRequest;
	}

	@Override
	public void setMultiClientRequest(final MultiClientRequest<C> multiClientRequest) {
		this.multiClientRequest = multiClientRequest;
	}

	@Override
	public void addArgLast(final C client, final Object arg) {
		multiClientRequest.getClientFrameworkRequest(client).addCommandArgLast(arg);
	}

	@Override
	public boolean hasReturnValue(final C client) {
		return multiClientRequest.getClientFrameworkResponse(client).hasReturnValue();
	}

	@Override
	public Object getReturnValue(final C client) {
		return multiClientRequest.getClientFrameworkResponse(client).getReturnValue();
	}

	@Override
	public MessageSummary getMessageSummary(final C client) {
		return multiClientRequest.getClientFrameworkResponse(client).getMessageSummary();
	}

	@Override
	public FrameworkRequest getClientFrameworkRequest(final C client) {
		return multiClientRequest.getClientFrameworkRequest(client);
	}

	@Override
	public FrameworkResponse getFrameworkResponse(final C client) {
		return multiClientRequest.getClientFrameworkResponse(client);
	}


}
