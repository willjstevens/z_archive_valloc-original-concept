/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

/**
 * 
 * 
 * @author wstevens
 */
public class AgentTransportManagerBase implements AgentTransportManager
{
	private AgentClientConnector connector;

	@Override
	public AgentClientConnector getDesktopConnector() {
		return connector;
	}

	@Override
	public void removeServerConnector() {
		connector = null;
	}

	@Override
	public void setDesktopClientConnector(final AgentClientConnector connector) {
		this.connector = connector;
	}

}
