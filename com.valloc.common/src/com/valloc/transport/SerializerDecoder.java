/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.valloc.domain.Serializer;

/**
 * 
 * 
 * @author wstevens
 */
class SerializerDecoder extends FrameDecoder
{
	private final Serializer serializer;

	SerializerDecoder(final Serializer serializer) {
		this.serializer = serializer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel,
	 * org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected Object decode(final ChannelHandlerContext context, final Channel channel, final ChannelBuffer buf) throws Exception {
		// first we need to determine the XML length with the write-ahead int total
		// length, and abort if not satisfied with 4 bytes
		if (buf.readableBytes() < 4) {
			return null;
		}

		buf.markReaderIndex();
		final int xmlLen = buf.readInt();

		if (buf.readableBytes() < xmlLen) {
			buf.resetReaderIndex();
			return null; // still not enough data for full XML data stream
		}

		// final byte[] xmlBytes = buf.array();
		// assert xmlBytes.length == xmlLen;
		final byte[] xmlBytes = new byte[xmlLen];
		buf.readBytes(xmlBytes);

		final WireMessage wireMessage = (WireMessage) serializer.deserialize(xmlBytes);

		return wireMessage;
	}
}
