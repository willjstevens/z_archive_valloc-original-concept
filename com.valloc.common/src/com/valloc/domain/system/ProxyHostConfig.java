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
public class ProxyHostConfig extends ServerHostConfig
{
	private final String username;
	private final char[] password;

	/**
	 * @param inetAddress
	 * @param macAddress
	 * @param port
	 * @param username
	 * @param password
	 */
	public ProxyHostConfig(final InetSocketAddress inetAddress, final String username, final char[] password) {
		super(inetAddress);
		this.username = username;
		this.password = password;
	}

	/**
	 * @return the username
	 */
	public final String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public final char[] getPassword() {
		return password;
	}
}
