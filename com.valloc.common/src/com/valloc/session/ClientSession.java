/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.MessageSummary;
import com.valloc.domain.system.ServerHostConfig;
import com.valloc.transport.ClientConnector;

/**
 *
 *
 * @author wstevens
 */
public interface ClientSession extends Session
{
	public MessageSummary connect(ServerHostConfig serverHostConfig);

	public ServerHostConfig getServerHostConfig();

	public void setClientConnector(ClientConnector connector);

}
