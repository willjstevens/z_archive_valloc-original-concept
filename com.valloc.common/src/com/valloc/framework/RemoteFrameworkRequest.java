/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.Date;

import javax.transaction.xa.Xid;

import com.valloc.Priority;
import com.valloc.session.SessionId;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class RemoteFrameworkRequest extends FrameworkRequest
{
	private SessionId sessionId;
	private ConversationId conversationId;
	private Xid transactionId;

	public RemoteFrameworkRequest(final Request request) {
		super(request);
	}

	public RemoteFrameworkRequest(final UniqueId requestId, final NodeType nodeType, final String serviceName, final String commandName, final CallType callType, final Priority priority, final Date startTimestamp) {
		super(requestId, nodeType, serviceName, commandName, callType, priority, startTimestamp);
	}

	public SessionId getSessionId() {
		return sessionId;
	}

	public void setSessionId(final SessionId sessionId) {
		this.sessionId = sessionId;
	}

	public ConversationId getConversationId() {
		return conversationId;
	}

	public void setConversationId(final ConversationId conversationId) {
		this.conversationId = conversationId;
	}

	public Xid getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(final Xid transactionId) {
		this.transactionId = transactionId;
	}
}
