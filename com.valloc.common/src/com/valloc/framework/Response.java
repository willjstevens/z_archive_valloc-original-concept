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
public class Response extends Message
{
	private MessageSummary messageSummary;
	private Object returnValue;

	public Response(final UniqueId requestId) {
		super(requestId);
	}

	MessageSummary getMessageSummary() {
		return messageSummary;
	}

	void setMessageSummary(final MessageSummary messageSummary) {
		this.messageSummary = messageSummary;
	}

	boolean hasReturnValue() {
		return returnValue != null;
	}

	Object getReturnValue() {
		return returnValue;
	}

	void setReturnValue(final Object returnValue) {
		this.returnValue = returnValue;
	}
}
