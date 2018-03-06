/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipelineFactory;

import com.valloc.domain.system.HostConfig;
import com.valloc.framework.NodeType;
import com.valloc.session.ServerSession;
import com.valloc.session.ServerSessionComponentFactory;
import com.valloc.session.SessionId;

/**
 *
 *
 * @author wstevens
 */
public class ServerTransportComponentFactory extends AbstractTransportComponentFactory
{
	private ServerSessionComponentFactory serverSessionComponentFactory;
	private ServerTransportManager transportManager;
	private TransportServer transportServer;
	private TransportServerStateMachine serverStateMachine;

	/* public for outside/core/service access as entry point into transport framework */
	@Override
	public void initialize() {
		transportManager = newServerTransportManager();
		transportServer = newTransportServer();
		serverStateMachine = newTransportServerStateMachine();

		serverStateMachine.setServerTransportComponentFactory(this);
		transportManager.setServerTransportComponentFactory(this);
		transportServer.setServerTransportComponentFactory(this);
		transportServer.setServerHostConfig(getServerHostConfig());

		setInitialized(true);
	}

	public ServerTransportManager getTransportManager() {
		return transportManager;
	}

	TransportServer getTransportServer() {
		return transportServer;
	}

	TransportServerStateMachine getServerStateMachine() {
		return serverStateMachine;
	}

	ServerConnector newServerConnector(final NodeType nodeType, final SessionId sessionId, final Channel channel) {
		validate();
		ServerConnector connector = null;
		if (nodeType == NodeType.DESKTOP) {
			connector = newDesktopServerConnector();
		} else if (nodeType == NodeType.AGENT) {
			connector = newAgentServerConnector();
		}

		// add connector refs
		final InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.getRemoteAddress();
		final HostConfig hostConfig = new HostConfig(inetSocketAddress);
		connector.setHostConfig(hostConfig);
		connector.setChannel(channel);
		connector.setFrameworkManager(getFrameworkComponentFactory().getFrameworkManager());
		
		// now trigger the initial setting of a session
		final ServerSession session = serverSessionComponentFactory.newServerSession(sessionId, nodeType, connector);
		connector.setSession(session);

		transportManager.addConnector(connector);

		return connector;
	}

	ChannelPipelineFactory newChannelPipelineFactory() {
		validate();
		final NettyPipelineFactory pipelineFactory = super.newBaseNettyPipelineFactory();
		pipelineFactory.setUseSslClientMode(false); // here we act as server setup
		pipelineFactory.setComponentApplicationChannelHandler(newFrameworkInteractionHandler());
		return pipelineFactory;
	}

	ChannelHandler newFrameworkInteractionHandler() {
		validate();
		final ServerFrameworkChannelHandler channelHandler = new ServerFrameworkChannelHandler();
		channelHandler.setServerTransportComponentFactory(this);
		channelHandler.setTransportManager(getTransportManager());
		return channelHandler;
	}

	protected DesktopServerConnector newDesktopServerConnector() {
		return new DesktopServerConnectorBase();
	}

	protected AgentServerConnector newAgentServerConnector() {
		return new AgentServerConnectorBase();
	}

	protected ServerTransportManager newServerTransportManager() {
		return new ServerTransportManagerBase();
	}

	protected TransportServer newTransportServer() {
		return new TransportServerBase();
	}

	protected TransportServerStateMachine newTransportServerStateMachine() {
		return new TransportServerStateMachine();
	}

	public void setServerSessionComponentFactory(final ServerSessionComponentFactory serverSessionComponentFactory) {
		this.serverSessionComponentFactory = serverSessionComponentFactory;
	}
}
