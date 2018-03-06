/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import com.valloc.MessageSummary;
import com.valloc.MessageSummaryStatus;
import com.valloc.MessageType;

/**
 *
 *
 * @author wstevens
 */
public class Result
{
	private final MessageSummary messageSummary;
	private Object returnValue;

	public Result(final MessageSummary messageSummary) {
		this.messageSummary = messageSummary;
	}

	public Result(final MessageSummary messageSummary, final Object returnValue) {
		this(messageSummary);
		this.returnValue = returnValue;
	}

	public Result(final MessageSummaryStatus messageSummaryStatus) {
		this.messageSummary = new MessageSummary(messageSummaryStatus);
	}

	public Result(final MessageSummaryStatus messageSummaryStatus, final Object returnValue) {
		this(messageSummaryStatus);
		this.returnValue = returnValue;
	}

	public MessageSummary getMessageSummary() {
		return messageSummary;
	}

	public Result addResultMessage(final String id, final MessageType type, final String messageStr) {
		messageSummary.addMessage(id, type, messageStr, false);
		return this;
	}

	public Result addResultMessage(final String id, final MessageType type, final String messageStr, final boolean displayToUser) {
		messageSummary.addMessage(id, type, messageStr, displayToUser);
		return this;
	}

	public boolean hasReturnValue() {
		return returnValue != null;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(final Object returnValue) {
		this.returnValue = returnValue;
	}
}
