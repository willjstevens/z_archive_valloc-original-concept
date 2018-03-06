/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.valloc.session.SessionId;

/**
 *
 *
 * @author wstevens
 */
public class ServerTransportManagerBase implements ServerTransportManager
{
	private ServerTransportComponentFactory factory;
	private final Map<SessionId, ServerConnector> activeConnectors = new HashMap<SessionId, ServerConnector>();

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.ServerTransportManager#addConnector(com.valloc.transport.ServerConnector)
	 */
	@Override
	public void addConnector(final ServerConnector transportClient) {
		final SessionId sessionId = transportClient.getSession().id();
		activeConnectors.put(sessionId, transportClient);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.ServerTransportManager#getAllConnectors()
	 */
	@Override
	public Collection<ServerConnector> getAllConnectors() {
		return activeConnectors.values();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.ServerTransportManager#getConnector(com.valloc.session.SessionId)
	 */
	@Override
	public ServerConnector getConnector(final SessionId sessionId) {
		return activeConnectors.get(sessionId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.ServerTransportManager#removeConnector(com.valloc.transport.ServerConnector)
	 */
	@Override
	public void removeConnector(final ServerConnector transportClient) {
		activeConnectors.remove(transportClient);
	}

	@Override
	public TransportServerStateMachine getTransportServerStateMachine() {
		return factory.getServerStateMachine();
	}

	@Override
	public void setServerTransportComponentFactory(final ServerTransportComponentFactory factory) {
		this.factory = factory;
	}

}
