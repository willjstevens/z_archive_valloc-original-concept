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
interface TransportServer
{
	MessageSummary start();

	MessageSummary destroy();

	boolean isBound();

	void setServerTransportComponentFactory(ServerTransportComponentFactory factory);

	void setServerHostConfig(ServerHostConfig serverHostConfig);
}
