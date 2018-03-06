/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system;

import java.net.InetSocketAddress;

import com.valloc.util.PairContainer;

/**
 * 
 *
 * @author wstevens
 */
public class HostConfig
{
	private final InetSocketAddress inetSocketAddress;
	private ProxyHostConfig proxyHostConfig;
	private byte[] macAddress;
		
	/**
	 * @param inetSocketAddress
	 * @param macAddress
	 */
	public HostConfig(final InetSocketAddress inetSocketAddress) {
		this.inetSocketAddress = inetSocketAddress;
	}


	/**
	 * @param inetSocketAddress
	 * @param macAddress
	 * @param proxyHostConfig
	 */
	public HostConfig(final InetSocketAddress inetSocketAddress, final ProxyHostConfig proxyHostConfig) {
		this(inetSocketAddress);
		this.proxyHostConfig = proxyHostConfig;
	}
	
	/**
	 * @return the inetSocketAddress
	 */
	public InetSocketAddress getInetSocketAddress() {
		return inetSocketAddress;
	}
	
	/**
	 * @return the macAddress
	 */
	public byte[] getMacAddress() {
		return macAddress;
	}

	/**
	 * @return the proxyHostConfig
	 */
	public ProxyHostConfig getProxyHostConfig() {
		return proxyHostConfig;
	}

	/**
	 * @param proxyHostConfig the proxyHostConfig to set
	 */
	public void setProxyHostConfig(final ProxyHostConfig proxyHostConfig) {
		this.proxyHostConfig = proxyHostConfig;
	}

	/**
	 * @param macAddress the macAddress to set
	 */
	public void setMacAddress(final byte[] macAddress) {
		this.macAddress = macAddress;
	}
	

	@Override
	public String toString() {
		PairContainer<String, Object> pairCont = new PairContainer<String, Object>();
		pairCont.addPair("host", getInetSocketAddress().getHostName());
		pairCont.addPair("port", getInetSocketAddress().getPort());
		pairCont.addPair("mac", getMacAddress());
		return "HostConfig: " + pairCont.toString();
	}
}
