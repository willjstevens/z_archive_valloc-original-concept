/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import static com.valloc.MessageSummaryStatus.CANCEL;
import static com.valloc.MessageSummaryStatus.INCOMPLETE;
import static com.valloc.MessageSummaryStatus.INVALID;
import static com.valloc.MessageSummaryStatus.REATTEMPT;
import static com.valloc.MessageSummaryStatus.ROLLBACK;
import static com.valloc.MessageSummaryStatus.WARNING;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author wstevens
 */
public final class MessageSummary
{
	// canned singletons for use
	public final static MessageSummary SUCCESS = new MessageSummary(MessageSummaryStatus.SUCCESS);
	public final static MessageSummary ERROR = new MessageSummary(MessageSummaryStatus.ERROR);
	public final static MessageSummary INTERRUPT = new MessageSummary(MessageSummaryStatus.INTERRUPT);
	public final static MessageSummary UNINITIALIZED = new MessageSummary(MessageSummaryStatus.UNINITIALIZED);
	private final MessageSummaryStatus summaryStatus;
	private final Map<String, Message> messages = new HashMap<String, Message>();

	public MessageSummary(final MessageSummaryStatus messageSummaryStatus) {
		this.summaryStatus = messageSummaryStatus;
	}

	public MessageSummary addMessage(final String id, final MessageType type, final String messageStr) {
		return addMessage(id, type, messageStr, true);
	}

	public MessageSummary addMessage(final String id, final MessageType type, final String messageStr, final boolean displayToUser) {
		final Message message = new Message(id, type, messageStr);
		messages.put(id, message);
		return this;
	}

	public MessageSummary mergeMessages(final MessageSummary messageSummary) {
		final MessageSummaryStatus mergingStatus = messageSummary.summaryStatus;
		if (mergingStatus != summaryStatus) {
			throw new IllegalArgumentException("Cannot merge messages from conflicting summaries of different status.");
		}

		for (final Message message : messageSummary.getAllMessages()) {
			messages.put(message.getId(), message);
		}
		return this;
	}

	public boolean hasSingleMessage() {
		return messages.size() == 1;
	}

	public boolean hasMultipleMessages() {
		return messages.size() > 1;
	}

	public Message getMessage(final String id) {
		return messages.get(id);
	}

	public Collection<Message> getAllMessages() {
		return messages.values();
	}

	public MessageSummaryStatus getMessageSummaryStatus() {
		return summaryStatus;
	}

	public boolean isSuccess() {
		return summaryStatus == MessageSummaryStatus.SUCCESS;
	}

	public boolean isWarning() {
		return summaryStatus == WARNING;
	}

	public boolean isError() {
		return summaryStatus == MessageSummaryStatus.ERROR;
	}

	public boolean isIncomplete() {
		return summaryStatus == INCOMPLETE;
	}

	public boolean isInvalid() {
		return summaryStatus == INVALID;
	}

	public boolean isReattempt() {
		return summaryStatus == REATTEMPT;
	}

	public boolean isCancel() {
		return summaryStatus == CANCEL;
	}

	public boolean isRollback() {
		return summaryStatus == ROLLBACK;
	}

	public boolean isInterrupt() {
		return summaryStatus == MessageSummaryStatus.INTERRUPT;
	}

	public boolean isUninitialized() {
		return summaryStatus == MessageSummaryStatus.UNINITIALIZED;
	}

	@Override
	public String toString() {
		return summaryStatus.toString();
	}
}