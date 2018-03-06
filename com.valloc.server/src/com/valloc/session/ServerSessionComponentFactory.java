/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.framework.NodeType;
import com.valloc.framework.ServerFrameworkComponentFactory;
import com.valloc.transport.ServerConnector;

/**
 *
 *
 * @author wstevens
 */
public class ServerSessionComponentFactory extends SessionComponentFactory
{
	private ServerSessionManager sessionManager;
	private ServerFrameworkComponentFactory serverFrameworkComponentFactory;

	@Override
	public void initialize() {
		sessionManager = newServerSessionManager();

		setInitialized(true);
	}

	public ServerSessionManager getSessionManager() {
		return sessionManager;
	}

	public ServerSession newServerSession(final SessionId sessionId, final NodeType nodeType, final ServerConnector connector) {
		validate();
		ServerSession session = null;
		if (nodeType == NodeType.DESKTOP) {
			session = newDesktopServerSession();
		} else if (nodeType == NodeType.AGENT) {
			session = newAgentServerSession();
		}
		session.setId(sessionId);
		session.setUsername(sessionId.getUsername());
		session.setServerConnector(connector);
		session.setFrameworkManager(serverFrameworkComponentFactory.getFrameworkManager());
		sessionManager.addSession(session);
		return session;
	}

	protected ServerSessionManager newServerSessionManager() {
		return new ServerSessionManagerBase();
	}

	protected DesktopServerSession newDesktopServerSession() {
		return new DesktopServerSessionBase();
	}

	protected AgentServerSession newAgentServerSession() {
		return new AgentServerSessionBase();
	}

	public void setServerFrameworkComponentFactory(final ServerFrameworkComponentFactory serverFrameworkComponentFactory) {
		this.serverFrameworkComponentFactory = serverFrameworkComponentFactory;
	}
	
	
}
