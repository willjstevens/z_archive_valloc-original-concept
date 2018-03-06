/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import javax.transaction.xa.Xid;

import com.valloc.framework.ConversationId;
import com.valloc.framework.Request;
import com.valloc.framework.Response;
import com.valloc.session.SessionId;

/**
 * 
 * 
 * 
 * @author wstevens
 */
public class WireMessage
{
	MessageType messageType;
	Request request;
	Response response;
	SessionId sessionId;
	ConversationId conversationId;
	Xid transactionId;
}
