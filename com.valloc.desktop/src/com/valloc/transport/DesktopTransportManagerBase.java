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
public class DesktopTransportManagerBase implements DesktopTransportManager
{
	private DesktopClientConnector connector;

	@Override
	public DesktopClientConnector getDesktopConnector() {
		return connector;
	}

	@Override
	public void removeServerConnector() {
		connector = null;
	}

	@Override
	public void setDesktopClientConnector(final DesktopClientConnector connector) {
		this.connector = connector;
	}

}
