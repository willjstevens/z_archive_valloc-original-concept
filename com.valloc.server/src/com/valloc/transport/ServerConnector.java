/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.domain.system.ClientNode;

/**
 *
 *
 * @author wstevens
 */
public interface ServerConnector<C extends ClientNode> extends Connector
{
	public C getConnectedClientNode();
	public void setConnectedClientNode(C clientNode);
}
