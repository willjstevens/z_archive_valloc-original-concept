/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.session.DesktopClientSession;

/**
 *
 *
 * @author wstevens
 */
public class DesktopTransportComponentFactory extends AbstractTransportComponentFactory
{
	private DesktopTransportManager desktopTransportManager;

	@Override
	public void initialize() {
		desktopTransportManager = newDesktopTransportManager();

		setInitialized(true);
	}

	public DesktopTransportManager getTransportManager() {
		return desktopTransportManager;
	}

	public DesktopClientConnector createAndRegisterDesktopClientConnector(final DesktopClientSession session) {
		validate();

		final DesktopClientConnector connector = newDesktopClientConnector();
		final DesktopFrameworkChannelHandler channelHandler = newDesktopFrameworkChannelHandler();
		channelHandler.setTransportConnector(connector);

		final NettyPipelineFactory pipelineFactory = super.newBaseNettyPipelineFactory();
		pipelineFactory.setUseSslClientMode(true);
		pipelineFactory.setComponentApplicationChannelHandler(channelHandler);

		connector.setSession(session);
		connector.setPipelineFactory(pipelineFactory);
		connector.setFrameworkManager(getFrameworkComponentFactory().getFrameworkManager());
		desktopTransportManager.setDesktopClientConnector(connector);

		return connector;
	}

	DesktopClientConnector newDesktopClientConnector() {
		return new DesktopClientConnectorBase();
	}

	DesktopFrameworkChannelHandler newDesktopFrameworkChannelHandler() {
		return new DesktopFrameworkChannelHandler();
	}

	public DesktopTransportManager getDesktopTransportManager() {
		return desktopTransportManager;
	}

	protected DesktopTransportManager newDesktopTransportManager() {
		return new DesktopTransportManagerBase();
	}
}
