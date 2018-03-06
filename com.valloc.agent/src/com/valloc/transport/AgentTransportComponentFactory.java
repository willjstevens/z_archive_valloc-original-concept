/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.session.AgentClientSession;

/**
 *
 *
 * @author wstevens
 */
public class AgentTransportComponentFactory extends AbstractTransportComponentFactory
{
	private AgentTransportManager agentTransportManager;

	@Override
	public void initialize() {
		agentTransportManager = newDesktopTransportManager();

		setInitialized(true);
	}

	public AgentTransportManager getTransportManager() {
		return agentTransportManager;
	}

	public AgentClientConnector createAndRegisterDesktopClientConnector(final AgentClientSession session) {
		validate();

		final AgentClientConnector connector = newDesktopClientConnector();
		final AgentFrameworkChannelHandler channelHandler = newDesktopFrameworkChannelHandler();
		channelHandler.setTransportConnector(connector);

		final NettyPipelineFactory pipelineFactory = super.newBaseNettyPipelineFactory();
		pipelineFactory.setUseSslClientMode(true);
		pipelineFactory.setComponentApplicationChannelHandler(channelHandler);

		connector.setSession(session);
		connector.setPipelineFactory(pipelineFactory);
		connector.setFrameworkManager(getFrameworkComponentFactory().getFrameworkManager());
		agentTransportManager.setDesktopClientConnector(connector);

		return connector;
	}

	AgentClientConnector newDesktopClientConnector() {
		return new AgentClientConnectorBase();
	}

	AgentFrameworkChannelHandler newDesktopFrameworkChannelHandler() {
		return new AgentFrameworkChannelHandler();
	}

	public AgentTransportManager getDesktopTransportManager() {
		return agentTransportManager;
	}

	protected AgentTransportManager newDesktopTransportManager() {
		return new AgentTransportManagerBase();
	}
}
