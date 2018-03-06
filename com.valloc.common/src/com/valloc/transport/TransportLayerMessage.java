/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.framework.FrameworkResponse;
import com.valloc.framework.RemoteFrameworkRequest;

/**
 *
 *
 *
 * @author wstevens
 */
class TransportLayerMessage
{
	MessageType messageType;
	RemoteFrameworkRequest remoteFrameworkRequest;
	FrameworkResponse frameworkResponse;

	// these necessary?
//	SessionId sessionId;
//	ConversationId conversationId;
//	Xid transactionId;
}
