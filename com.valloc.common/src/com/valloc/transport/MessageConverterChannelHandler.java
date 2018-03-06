/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.net.SocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.UpstreamMessageEvent;

import com.valloc.framework.FrameworkResponse;
import com.valloc.framework.RemoteFrameworkRequest;
import com.valloc.framework.Request;

/**
 *
 *
 * @author wstevens
 */
final class MessageConverterChannelHandler implements ChannelUpstreamHandler, ChannelDownstreamHandler
{
	/*
	 * (non-Javadoc)
	 *
	 * @see org.jboss.netty.channel.ChannelDownstreamHandler#handleDownstream(org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelEvent)
	 */
	@Override
	public void handleDownstream(final ChannelHandlerContext context, ChannelEvent event) throws Exception {
		if (event instanceof MessageEvent) {
			final TransportLayerMessage transportMsg = (TransportLayerMessage) ((MessageEvent) event).getMessage();
			final WireMessage wireMessage = new WireMessage();
			// always set the response object, which even travels with the original request
			wireMessage.response = transportMsg.frameworkResponse.getResponse();
			wireMessage.messageType = transportMsg.messageType;
			if (transportMsg.messageType == MessageType.REQUEST) {
				final RemoteFrameworkRequest remoteFrameworkRequest = transportMsg.remoteFrameworkRequest;
				wireMessage.request = remoteFrameworkRequest.getRequest();
				wireMessage.sessionId = remoteFrameworkRequest.getSessionId();
				// these may be present or may be null, but set anyway
				wireMessage.conversationId = remoteFrameworkRequest.getConversationId();
				wireMessage.transactionId = remoteFrameworkRequest.getTransactionId();
			} else if (transportMsg.messageType == MessageType.RESPONSE) {
				// nothing in here... yet
			}

			// rebuild event object
			final Channel channel = event.getChannel();
			final ChannelFuture eventFuture = event.getFuture();
			final SocketAddress remoteAddress = channel.getRemoteAddress();
			event = new DownstreamMessageEvent(channel, eventFuture, wireMessage, remoteAddress);
		}

		context.sendDownstream(event);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.jboss.netty.channel.ChannelUpstreamHandler#handleUpstream(org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelEvent)
	 */
	@Override
	public void handleUpstream(final ChannelHandlerContext context, ChannelEvent event) throws Exception {
		if (event instanceof MessageEvent) {
			final WireMessage wireMessage = (WireMessage) ((MessageEvent) event).getMessage();
			final TransportLayerMessage transportMsg = new TransportLayerMessage();
			transportMsg.messageType = wireMessage.messageType;
			// the response should always travel, even with the initial request
			transportMsg.frameworkResponse = new FrameworkResponse(wireMessage.response);
			if (wireMessage.messageType == MessageType.REQUEST) {
				final Request request = wireMessage.request;
				final RemoteFrameworkRequest remoteFrameworkRequest = new RemoteFrameworkRequest(request);
				remoteFrameworkRequest.setSessionId(wireMessage.sessionId);
				// these values could be null but set anyway
				remoteFrameworkRequest.setConversationId(wireMessage.conversationId);
				remoteFrameworkRequest.setTransactionId(wireMessage.transactionId);
				transportMsg.remoteFrameworkRequest = remoteFrameworkRequest;
			} else if (wireMessage.messageType == MessageType.RESPONSE) {
				// nothing here... yet
			}

			// rebuild event object
			final Channel channel = event.getChannel();
			final SocketAddress remoteAddress = channel.getRemoteAddress();
			// final MessageEvent frameworkObjectEvent = new UpstreamMessageEvent(channel, transportMsg, remoteAddress);
			event = new UpstreamMessageEvent(channel, transportMsg, remoteAddress);
		}

		context.sendUpstream(event);
	}
}
