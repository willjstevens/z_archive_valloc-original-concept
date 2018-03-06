/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.domain.system.ClientNode;

/**
 *
 *
 * @author wstevens
 */
public class AbstractServerConnector<C extends ClientNode> extends AbstractConnector implements ServerConnector<C>
{
	private C clientNode;

	@Override
	public C getConnectedClientNode() {
		return clientNode;
	}

	@Override
	public void setConnectedClientNode(final C clientNode) {
		this.clientNode = clientNode;
	}

}
