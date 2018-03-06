/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system;

import com.valloc.framework.NodeType;

/**
 *
 *
 * @author wstevens
 */
public class Server extends Participant
{
	private final ServerHostConfig serverHostConfig;
	
	/**
	 * @param name
	 * @param nodeType
	 */
	public Server(final String name, final ServerHostConfig serverHostConfig)
	{
		super(name, NodeType.SERVER);
		this.serverHostConfig = serverHostConfig;
	}

	/**
	 * @return the hostConfig
	 */
	public ServerHostConfig getServerHostConfig()
	{
		return serverHostConfig;
	}	
}
