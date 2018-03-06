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
public interface AgentTransportManager extends TransportManager
{
	public AgentClientConnector getDesktopConnector();

	public void removeServerConnector();

	void setDesktopClientConnector(AgentClientConnector connector);
}
