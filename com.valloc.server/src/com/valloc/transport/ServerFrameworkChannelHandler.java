/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.ssl.SslHandler;

import com.valloc.framework.NodeType;
import com.valloc.session.SessionId;

/**
 *
 *
 * @author wstevens
 */
final class ServerFrameworkChannelHandler extends SimpleChannelHandler
{
	private ServerTransportManager transportManager;
	private ServerTransportComponentFactory serverTransportComponentFactory;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.jboss.netty.channel.SimpleChannelHandler#exceptionCaught(org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ExceptionEvent)
	 */
	@Override
	public void exceptionCaught(final ChannelHandlerContext channelHandlerContext, final ExceptionEvent e) throws Exception {
		super.exceptionCaught(channelHandlerContext, e);
	}

	@Override
	public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
		// super.channelConnected(ctx, e);

		final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
		sslHandler.handshake(); // Begin handshake.
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(final ChannelHandlerContext channelHandlerContext, final MessageEvent event) throws Exception {
		final TransportLayerMessage transportMessage = (TransportLayerMessage) event.getMessage();

		// establish connector
		final SessionId sessionId = transportMessage.remoteFrameworkRequest.getSessionId();
		ServerConnector connector = transportManager.getConnector(sessionId);
		if (connector == null) { // this is a new connector so we bootstrap the session and connector...
			final NodeType nodeType = transportMessage.remoteFrameworkRequest.getNodeType();
			final Channel channel = channelHandlerContext.getChannel();
			connector = serverTransportComponentFactory.newServerConnector(nodeType, sessionId, channel);
		}

		// pass upwards into framework
		if (transportMessage.messageType == MessageType.REQUEST) {
			connector.requestReceived(transportMessage.remoteFrameworkRequest, transportMessage.frameworkResponse);
		} else if (transportMessage.messageType == MessageType.RESPONSE) {
			connector.responseReceived(transportMessage.frameworkResponse);
		}
	}

	void setTransportManager(final ServerTransportManager transportManager) {
		this.transportManager = transportManager;
	}

	void setServerTransportComponentFactory(final ServerTransportComponentFactory serverTransportComponentFactory) {
		this.serverTransportComponentFactory = serverTransportComponentFactory;
	}
}
