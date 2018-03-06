/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import org.jboss.netty.channel.Channel;


/**
 *  
 *
 * @author wstevens
 */
interface NettyConnector
{
	public int getTransportId();
	
	Channel getChannel();
	void setChannel(Channel channel);
}
