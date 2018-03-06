/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.MessageSummary;
import com.valloc.domain.system.ServerHostConfig;

/**
 *
 *
 * @author wstevens
 */
public interface ClientConnector extends Connector, NettyClientConnector
{
	public MessageSummary connect(ServerHostConfig serverHostConfig);

	public boolean isConnected();

	public ServerHostConfig getServerHostConfig();
}
