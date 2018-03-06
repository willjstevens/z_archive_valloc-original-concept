/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system;

import com.valloc.framework.NodeType;
import com.valloc.util.PairContainer;

/**
 *
 *
 * @author wstevens
 */
public class Desktop extends ClientNode
{
	/**
	 * @param name
	 * @param nodeType
	 */
	public Desktop(final String name, final HostConfig hostConfig) {
		super(name, NodeType.DESKTOP, hostConfig);
	}

	@Override
	public String toString() {
		final PairContainer<String, Object> pairCont = new PairContainer<String, Object>();
		pairCont.addPair("participant-type", getNodeType());
		pairCont.addPair("name", getName());
		pairCont.addPair("host-config.host", getHostConfig().getInetSocketAddress().getHostName());
		pairCont.addPair("host-config.port", getHostConfig().getInetSocketAddress().getPort());
		pairCont.addPair("host-config.mac", getHostConfig().getMacAddress());
		return "Desktop: " + pairCont.toString();
	}
}
