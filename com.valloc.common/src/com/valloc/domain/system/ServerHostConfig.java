/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system;

import java.net.InetSocketAddress;

/**
 * 
 * 
 * @author wstevens
 */
public class ServerHostConfig extends HostConfig
{
	/**
	 * @param inetAddress
	 * @param macAddress
	 */
	public ServerHostConfig(final InetSocketAddress inetAddress) {
		super(inetAddress);
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return getInetSocketAddress().getPort();
	}
}
