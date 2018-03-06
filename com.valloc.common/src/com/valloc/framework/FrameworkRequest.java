/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.Date;
import java.util.List;

import com.valloc.Priority;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.session.Session;
import com.valloc.transaction.Transaction;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class FrameworkRequest extends AbstractFrameworkObject
{
	private final Request request;
	private Session session;
	private Transaction transaction;
	private Conversation conversation;
	private InterruptTracker interruptTracker;

	public FrameworkRequest(final UniqueId requestId,
								final NodeType nodeType,
								final String serviceName,
								final String commandName,
								final CallType callType,
								final Priority priority,
								final Date startTimestamp) {
		request = new Request(requestId,
								nodeType,
								serviceName,
								commandName,
								callType,
								priority,
								startTimestamp);
		setSource(request);
	}

	public FrameworkRequest(final Request request) {
		this.request = request;
		setSource(request);
	}

	public void addCommandArgLast(final Object arg) {
		request.addCommandArgLast(arg);
	}

	public void setEndTimestamp(final Date endTimestamp) {
		request.setEndTimestamp(endTimestamp);
	}

	public NodeType getNodeType() {
		return request.getNodeType();
	}

	public String getServiceName() {
		return request.getServiceName();
	}

	public String getCommandName() {
		return request.getCommandName();
	}

	public CallType getCallType() {
		return request.getCallType();
	}

	public Priority getPriority() {
		return request.getPriority();
	}

	public List<Object> getCommandArgs() {
		return request.getCommandArgs();
	}

	public Date getStartTimestamp() {
		return request.getStartTimestamp();
	}

	public Date getEndTimestamp() {
		return request.getEndTimestamp();
	}

	public Session getSession() {
		return session;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public Request getRequest() {
		return request;
	}

	public InterruptTracker getInterruptTracker() {
		return interruptTracker;
	}

	public void setSession(final Session session) {
		this.session = session;
	}

	public void setTransaction(final Transaction transaction) {
		this.transaction = transaction;
	}

	public void setConversation(final Conversation conversation) {
		this.conversation = conversation;
	}

	public void setInterruptTracker(final InterruptTracker interruptTracker) {
		this.interruptTracker = interruptTracker;
	}
}
