/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.MessageSummary;
import com.valloc.domain.system.ServerHostConfig;
import com.valloc.transport.ClientConnector;
import com.valloc.transport.Connector;

/**
 *
 *
 * @author wstevens
 */
abstract class AbstractClientSession extends AbstractSession implements ClientSession
{
	private ClientConnector connector;

	@Override
	public MessageSummary connect(final ServerHostConfig serverHostConfig) {
		return connector.connect(serverHostConfig);
	}

	@Override
	public ServerHostConfig getServerHostConfig() {
		return connector.getServerHostConfig();
	}

	@Override
	public void setClientConnector(final ClientConnector connector) {
		this.connector = connector;
	}

	@Override
	Connector getConnector() {
		return connector;
	}
}
