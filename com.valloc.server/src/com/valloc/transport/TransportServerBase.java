/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.valloc.CategoryType;
import com.valloc.MessageSummary;
import com.valloc.domain.system.ServerHostConfig;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * 
 * 
 * @author wstevens
 */
final class TransportServerBase implements TransportServer
{
	private static final Logger logger = LogManager.manager().getLogger(TransportServerBase.class, CategoryType.TRANSPORT_SERVER);

	private ServerHostConfig serverHostConfig;
	private Channel serverChannel;
	private ServerSocketChannelFactory channelFactory;
	private ExecutorService bossExecutorService;
	private ExecutorService workerExecutorService;
	private ServerTransportComponentFactory serverTransportComponentFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.lifecycle.Bootstrapable#bootstrap()
	 */
	@Override
	// public void bootstrap()
	public MessageSummary start() {
		final MessageSummary messageSummary = MessageSummary.SUCCESS;

		// this might be later wrapped with custom executor services
		bossExecutorService = Executors.newCachedThreadPool();
		workerExecutorService = Executors.newCachedThreadPool();
		channelFactory = new NioServerSocketChannelFactory(bossExecutorService, workerExecutorService);

		// load primary server thread and worker thread support
		final ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

		// load application handler and pipeline serverTransportComponentFactory
		final ChannelPipelineFactory pipelineFactory = serverTransportComponentFactory.newChannelPipelineFactory();
		bootstrap.setPipelineFactory(pipelineFactory);

		// bind and serve
		// non-blocking; this will fire Netty boss thread and return from bootstrap()
		final InetSocketAddress serverSocketAddress = serverHostConfig.getInetSocketAddress();

		/*
		 * HERE WE BIND - THIS FORKS A NEW LISTENER THREAD
		 */
		serverChannel = bootstrap.bind(serverSocketAddress);

		if (logger.isFine()) {
			logger.fine("Transport server is bound and listening on port %d.", serverSocketAddress.getPort());
		}

		return messageSummary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.lifecycle.Destroyable#destroy()
	 */
	@Override
	// public void destroy()
	public MessageSummary destroy() {
		final MessageSummary messageSummary = MessageSummary.SUCCESS;

		// close this first to stop accepting new connections
		// NOTE: calling close here also unbinds the server from the port
		ChannelFuture serverCloseFuture = serverChannel.close();

		// continue on to initiating close on connector channels, while waiting for the above
		final ServerTransportManager transportManager = serverTransportComponentFactory.getTransportManager();
		for (final ServerConnector connector : transportManager.getAllConnectors()) {
			connector.disconnect(); // internally here the channel close is initiated
		}

		// now back to server closing, if not done then wait..
		if (!serverCloseFuture.isDone()) {
			try { // reassign on await
				serverCloseFuture = serverCloseFuture.await();
			} catch (final InterruptedException e) {
				logger.error("Transport server was interrupted while attempting to shutdown.", e);
			}
		}
		if (!serverCloseFuture.isSuccess()) {
			final Throwable thrown = serverCloseFuture.getCause();
			if (thrown != null) {
				logger.error("Transport server was not shutdown cleanly due to this throwable: " + thrown.toString(), thrown);
			} else {
				logger.warn("Transport server was not shutdown cleanly due to throwable for unknown reason.");
			}
		}
		if (logger.isFine()) {
			final boolean isDone = serverCloseFuture.isDone();
			final String msg = "Transport server with done flag as %b was left in state of: %s.";
			if (serverCloseFuture.isSuccess()) {
				logger.fine(String.format(msg, isDone, "successful shutdown."));
			} else if (serverCloseFuture.isCancelled()) {
				logger.fine(String.format(msg, isDone, "cancelled shutdown."));
			} else if (serverCloseFuture.getCause() != null) {
				logger.fine(String.format(msg, isDone, "an errored shutdown."));
			}
		}

		channelFactory.releaseExternalResources();

		if (logger.isFine()) {
			final String bound = serverChannel.isBound() ? "bound on" : "unbound from";
			final int port = serverHostConfig.getPort();
			logger.fine(String.format("Transport server is %s port %d.", bound, port));
		}

		return messageSummary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.transport.TransportServer#isBound()
	 */
	@Override
	public boolean isBound() {
		boolean isBound = false;
		if (serverChannel != null) {
			isBound = serverChannel.isBound();
		}
		return isBound;
	}

	@Override
	public void setServerTransportComponentFactory(final ServerTransportComponentFactory factory) {
		this.serverTransportComponentFactory = factory;
	}

	@Override
	public void setServerHostConfig(final ServerHostConfig serverHostConfig) {
		this.serverHostConfig = serverHostConfig;
	}
}