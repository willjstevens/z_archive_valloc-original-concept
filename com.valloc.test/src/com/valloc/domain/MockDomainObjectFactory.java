/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain;

import java.net.InetSocketAddress;

import com.valloc.domain.system.Agent;
import com.valloc.domain.system.Desktop;
import com.valloc.domain.system.HostConfig;
import com.valloc.domain.system.ProxyHostConfig;
import com.valloc.domain.system.Server;
import com.valloc.domain.system.ServerHostConfig;

/**
 * 
 * 
 * @author wstevens
 */
public final class MockDomainObjectFactory
{
	// commonly used canned values
	private static final int DEFAULT_PARTICIPANT_PORT = 5959;
	private static final InetSocketAddress inetSocketAddress = new InetSocketAddress(DEFAULT_PARTICIPANT_PORT);

	public Desktop newDesktop() {
		final String name = "desktop.wstevens";
		final Desktop retval = new Desktop(name, newHostConfig());
		return retval;
	}

	public Server newServer() {
		final String name = "server.primary";
		final Server retval = new Server(name, newServerHostConfig());
		return retval;
	}

	public Agent newAgent() {
		final String name = "agent.wstevens";
		final Agent retval = new Agent(name, newHostConfig());
		return retval;
	}

	public HostConfig newHostConfig() {
		final HostConfig retval = new HostConfig(inetSocketAddress);
		retval.setMacAddress(new byte[] { 0x1, 0x2 });
		return retval;
	}

	public ServerHostConfig newServerHostConfig() {
		final InetSocketAddress inetSocketAddress = new InetSocketAddress(DEFAULT_PARTICIPANT_PORT);
		final ServerHostConfig retval = new ServerHostConfig(inetSocketAddress);
		retval.setMacAddress(new byte[] { 0x1, 0x2 });
		return retval;
	}

	public ProxyHostConfig newProxyHostConfig() {
		final InetSocketAddress inetSocketAddress = new InetSocketAddress(DEFAULT_PARTICIPANT_PORT);
		final String username = "wstevens";
		final char[] password = { 'a', 'd', 'm', 'i', 'n' };
		final ProxyHostConfig retval = new ProxyHostConfig(inetSocketAddress, username, password);
		return retval;
	}
}
