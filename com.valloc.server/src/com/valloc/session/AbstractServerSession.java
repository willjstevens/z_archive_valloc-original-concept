/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.domain.system.ClientNode;
import com.valloc.transport.Connector;
import com.valloc.transport.ServerConnector;

/**
 *
 *
 * @author wstevens
 */
abstract class AbstractServerSession<C extends ClientNode> extends AbstractSession implements ServerSession<C>
{
	private ServerConnector serverConnector;
	private C clientNode;

	@Override
	public C getClientNode() {
		return clientNode;
	}

	@Override
	public void setClientNode(final C clientNode) {
		this.clientNode = clientNode;
	}

	@Override
	Connector getConnector() {
		return serverConnector;
	}
}
