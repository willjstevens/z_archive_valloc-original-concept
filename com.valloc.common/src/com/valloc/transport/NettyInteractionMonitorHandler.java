/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.ssl.SslHandler;

/**
 * 
 * 
 * @author wstevens
 */
class NettyInteractionMonitorHandler extends SimpleChannelHandler
{
	private final boolean useSslClientMode;

	private final String prefix;

	NettyInteractionMonitorHandler(final boolean useSslClientMode) {
		this.useSslClientMode = useSslClientMode;
		prefix = useSslClientMode ? "Client " : "Server ";
	}

	@Override
	public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
		// super.channelConnected(ctx, e);

		if (useSslClientMode) {
			System.out.println("**** Client beginning handshake...");
			final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
			sslHandler.handshake();
		}
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception {
		System.err.println(e.getCause());
		e.getCause().printStackTrace();
		super.exceptionCaught(ctx, e);
	}

	@Override
	public void handleUpstream(final ChannelHandlerContext ctx, final ChannelEvent event) throws Exception {
		super.handleUpstream(ctx, event);
		System.out.println(prefix + "Upstream: " + event);
	}

	@Override
	public void handleDownstream(final ChannelHandlerContext ctx, final ChannelEvent event) throws Exception {
		super.handleDownstream(ctx, event);
		System.out.println(prefix + "Downstream: " + event);
	}

}
