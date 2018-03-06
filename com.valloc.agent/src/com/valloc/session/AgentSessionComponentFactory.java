/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.framework.AgentFrameworkComponentFactory;
import com.valloc.transport.AgentClientConnector;
import com.valloc.transport.AgentTransportComponentFactory;

/**
 *
 *
 * @author wstevens
 */
public class AgentSessionComponentFactory extends AbstractClientSessionComponentFactory
{
	private AgentTransportComponentFactory agentTransportComponentFactory;
	private AgentFrameworkComponentFactory desktopFrameworkComponentFactory;
	private AgentSessionManager sessionManager;

	@Override
	public void initialize() {
		sessionManager = newAgentSessionManager();

		setInitialized(true);
	}

	public AgentSessionManager getSessionManager() {
		return sessionManager;
	}

	public AgentClientSession createAndRegisterDesktopSession(final String username) {
		validate();
		final AgentClientSession session = newAgentClientSession();
		final SessionId sessionId = newSessionId(username);
		session.setId(sessionId);
		session.setUsername(username);

		// build and associate transport connector
		final AgentClientConnector connector = agentTransportComponentFactory.createAndRegisterDesktopClientConnector(session);
		session.setClientConnector(connector);

		// set manager references
		sessionManager.setDesktopClientSession(session);
		session.setFrameworkManager(desktopFrameworkComponentFactory.getFrameworkManager());

		return session;
	}

	protected AgentClientSession newAgentClientSession() {
		return new AgentClientSessionBase();
	}

	protected AgentSessionManager newAgentSessionManager() {
		return new AgentSessionManagerBase();
	}

	public void setDesktopTransportComponentFactory(final AgentTransportComponentFactory agentTransportComponentFactory) {
		this.agentTransportComponentFactory = agentTransportComponentFactory;
	}

	public void setDesktopFrameworkComponentFactory(final AgentFrameworkComponentFactory desktopFrameworkComponentFactory) {
		this.desktopFrameworkComponentFactory = desktopFrameworkComponentFactory;
	}
}
