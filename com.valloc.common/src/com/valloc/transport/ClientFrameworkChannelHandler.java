/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 
 * 
 * @author wstevens
 */
class ClientFrameworkChannelHandler extends SimpleChannelHandler
{

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

	// @Override
	// public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
	// super.channelConnected(ctx, e);
	//
	// // fire handshake process on connect
	// // final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
	// // sslHandler.handshake();
	// }
}
