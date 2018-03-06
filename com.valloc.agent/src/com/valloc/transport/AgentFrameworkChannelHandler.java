/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

/**
 * 
 * 
 * @author wstevens
 */
final class AgentFrameworkChannelHandler extends ClientFrameworkChannelHandler
{
	private AgentClientConnector transportConnector;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(final ChannelHandlerContext channelHandlerContext, final MessageEvent event) throws Exception {
		// fire request upwards
		final TransportLayerMessage transportMessage = (TransportLayerMessage) event.getMessage();
		if (transportMessage.messageType == MessageType.REQUEST) {
			transportConnector.requestReceived(transportMessage.remoteFrameworkRequest, transportMessage.frameworkResponse);
		} else if (transportMessage.messageType == MessageType.RESPONSE) {
			transportConnector.responseReceived(transportMessage.frameworkResponse);
		}
	}

	public void setTransportConnector(final AgentClientConnector transportConnector) {
		this.transportConnector = transportConnector;
	}
}
