/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.MessageSummary;
import com.valloc.MessageSummaryStatus;
import com.valloc.MessageType;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.transaction.Transaction;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractService implements Service
{
	private UniqueId id;
	private MessageSummary messageSummary;
	private Transaction transaction;
	private InterruptTracker interruptTracker;

	@Override
	public UniqueId id() {
		return id;
	}

	@Override
	public void setId(final UniqueId id) {
		this.id = id;
	}

	public void initializeMessageSummary(final MessageSummaryStatus summaryStatus) {
		messageSummary = new MessageSummary(summaryStatus);
	}

	@Override
	public boolean isMessageSummaryInitialized() {
		return messageSummary != null;
	}

	@Override
	public MessageSummary getMessageSummary() {
		if (messageSummary == null) {
			throw new IllegalStateException("Message summary has not been initialized.");
		}
		return messageSummary;
	}

	public void addSummaryMessage(final String id, final MessageType type, final String messageStr) {
		addSummaryMessage(id, type, messageStr, false);
	}

	public void addSummaryMessage(final String id, final MessageType type, final String messageStr, final boolean displayToUser) {
		if (messageSummary == null) {
			throw new IllegalStateException("Message summary has not been initialized.");
		}
		messageSummary.addMessage(id, type, messageStr, displayToUser);
	}

	@Override
	public boolean isTransact() {
		return transaction != null;
	}

	@Override
	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public void setTransaction(final Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public InterruptTracker getInterruptTracker() {
		return interruptTracker;
	}

	@Override
	public void setInterruptTracker(final InterruptTracker interruptTracker) {
		this.interruptTracker = interruptTracker;
	}


}
