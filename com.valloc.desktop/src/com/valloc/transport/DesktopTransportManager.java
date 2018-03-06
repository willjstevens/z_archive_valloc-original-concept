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
public interface DesktopTransportManager extends TransportManager
{
	public DesktopClientConnector getDesktopConnector();

	public void removeServerConnector();

	void setDesktopClientConnector(DesktopClientConnector connector);
}
