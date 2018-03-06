/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.domain.system.ClientNode;

/**
 *
 *
 * @author wstevens
 */
public abstract interface ServerSession<C extends ClientNode> extends Session
{
	public C getClientNode();
	public void setClientNode(C clientNode);
}
