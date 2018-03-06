/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.util.Collection;

import com.valloc.session.SessionId;

/**
 * 
 * 
 * @author wstevens
 */
public interface ServerTransportManager extends TransportManager
{

	// access for public core/services to access to start/stop server
	public TransportServerStateMachine getTransportServerStateMachine();

	// access for core container to set initial entry point to all other internal transport pieces
	public void setServerTransportComponentFactory(ServerTransportComponentFactory factory);

	// session ID here since that is readily available from the WireMessage object
	public ServerConnector getConnector(SessionId session);

	public Collection<ServerConnector> getAllConnectors();

	void addConnector(ServerConnector transportClient);

	void removeConnector(ServerConnector transportClient);
}
