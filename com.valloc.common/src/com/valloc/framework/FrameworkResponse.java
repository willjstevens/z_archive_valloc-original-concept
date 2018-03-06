/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.MessageSummary;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class FrameworkResponse extends AbstractFrameworkObject
{
	private Response response;

	public FrameworkResponse(final UniqueId requestId) {
		this(new Response(requestId));
		// reset summary to uninit'd since this constructor is called from
		//	same time of setting up request
		setMessageSummary(MessageSummary.UNINITIALIZED);
	}

	public FrameworkResponse(final Response response) {
		setResponse(response);
		// no need to initialize message summary since it should be inherited
		//		from existing Response object
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(final Response response) {
		this.response = response;
		setSource(response);
	}

	public MessageSummary getMessageSummary() {
		final MessageSummary messageSummary = response.getMessageSummary();
		if (messageSummary == MessageSummary.UNINITIALIZED) {
			// this should be a development-time issue, but throw anyway to complain
			throw new IllegalStateException("Message summary object has not yet been initialized.");
		}
		return messageSummary;
	}

	public void setMessageSummary(final MessageSummary messageSummary) {
		response.setMessageSummary(messageSummary);
	}

	public boolean hasReturnValue() {
		return response.hasReturnValue();
	}

	public Object getReturnValue() {
		return response.getReturnValue();
	}

	public void setReturnValue(final Object returnValue) {
		response.setReturnValue(returnValue);
	}
}
