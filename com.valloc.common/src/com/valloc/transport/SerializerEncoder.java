/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;

import com.valloc.domain.Serializer;

/**
 * 
 * 
 * @author wstevens
 */
final class SerializerEncoder implements ChannelDownstreamHandler
{
	private final Serializer serializer;

	SerializerEncoder(final Serializer serializer) {
		this.serializer = serializer;
	}

	@Override
	public void handleDownstream(final ChannelHandlerContext ctx, final ChannelEvent channelEvent) throws Exception {
		if (channelEvent instanceof MessageEvent) {
			final MessageEvent event = (MessageEvent) channelEvent;
			final WireMessage wireMessage = (WireMessage) event.getMessage();
			final byte[] xmlBytes = serializer.serialize(wireMessage);
			final int xmlLen = xmlBytes.length;

			final ChannelBuffer buf = ChannelBuffers.buffer(xmlLen + 4);
			buf.writeInt(xmlLen);
			buf.writeBytes(xmlBytes);

			Channels.write(ctx, event.getFuture(), buf);
		} else {
			ctx.sendDownstream(channelEvent);
		}
	}
}
