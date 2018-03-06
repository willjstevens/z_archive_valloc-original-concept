/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.valloc.CategoryType;
import com.valloc.MessageSummary;
import com.valloc.MessageSummaryStatus;
import com.valloc.domain.system.ServerHostConfig;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 *
 *
 * @author wstevens
 */
abstract class AbstractClientConnector extends AbstractConnector implements ClientConnector
{
	private static final Logger logger = LogManager.manager().getLogger(AbstractClientConnector.class, CategoryType.TRANSPORT_CONNECTOR);

	private ServerHostConfig serverHostConfig;
	private ExecutorService bossExecutorService;
	private ExecutorService workerExecutorService;
	private NettyPipelineFactory pipelineFactory;
	private ChannelFactory channelFactory;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.ClientConnector#connect(com.valloc.domain.system.ServerHostConfig)
	 */
	@Override
	public MessageSummary connect(final ServerHostConfig serverHostConfig) {
		MessageSummary messageSummary = null;
		this.serverHostConfig = serverHostConfig;

		// this might be later wrapped with custom executor services
		bossExecutorService = Executors.newCachedThreadPool();
		workerExecutorService = Executors.newCachedThreadPool();
		channelFactory = new NioClientSocketChannelFactory(bossExecutorService, workerExecutorService);

		// load application handler and pipeline factory
		final ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);
		bootstrap.setPipelineFactory(pipelineFactory);

		// start the connection attempt
		final ChannelFuture future = bootstrap.connect(serverHostConfig.getInetSocketAddress());

		try {
			future.await();
			if (future.isDone()) {
				if (future.isSuccess()) {
					final Channel channel = future.getChannel();
					setChannel(channel);
					messageSummary = new MessageSummary(MessageSummaryStatus.SUCCESS);
					if (logger.isFine()) {
						final boolean isConnected = channel.isConnected();
						final String localHost = ((InetSocketAddress)channel.getLocalAddress()).getHostName();
						final int localPort = ((InetSocketAddress)channel.getLocalAddress()).getPort();
						final String serverHost = serverHostConfig.getInetSocketAddress().getHostName();
						final int serverPort = serverHostConfig.getPort();
						String msg = "Local transport connector (%s:%d) is connected (marked by transport as %b,) to server located at %s:%d.";
						msg = String.format(msg, localHost, localPort, isConnected, serverHost, serverPort);
						logger.fine(msg);
					}
				} else if (future.getCause() != null) {
					final Throwable thrown = future.getCause();
					final String msg = "Problem occurred while attempting to connect to server located at %s:%d: %s.";
					logger.error(msg, thrown, serverHostConfig.getInetSocketAddress().getHostName(), serverHostConfig.getPort());
					messageSummary = new MessageSummary(MessageSummaryStatus.ERROR);
				}
			} else {
				logger.warn("Connect prematurely finished while waiting.");
			}
		} catch (final InterruptedException e) {
			final String msg = "Transport connector was interrupted while attempting to connect to server located at %s:%d.";
			logger.error(msg, e, serverHostConfig.getInetSocketAddress().getHostName(), serverHostConfig.getPort());
			messageSummary = new MessageSummary(MessageSummaryStatus.ERROR);
		}

		return messageSummary;
	}

	@Override
	public boolean isConnected() {
		boolean isConnected = false;
		final Channel connectorChannel = getChannel();
		if (connectorChannel != null) {
			isConnected = connectorChannel.isConnected();
		}
		return isConnected;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.AbstractConnector#disconnect()
	 */
	@Override
	public MessageSummary disconnect() {
		final MessageSummary messageSummary = super.disconnect();

		// do this to release executors on this client only
		channelFactory.releaseExternalResources();

		if (logger.isFine()) {
			final String connected = getChannel().isConnected() ? "disconnected from" : "still connected to";
			final String host = serverHostConfig.getInetSocketAddress().getHostName();
			final int port = serverHostConfig.getPort();
			logger.fine(String.format("Transport server is %s %s:%d.", connected, host, port));
		}

		return messageSummary;
	}

	@Override
	public void setPipelineFactory(final NettyPipelineFactory pipelineFactory) {
		this.pipelineFactory = pipelineFactory;
	}

	@Override
	public ServerHostConfig getServerHostConfig() {
		return serverHostConfig;
	}
}
