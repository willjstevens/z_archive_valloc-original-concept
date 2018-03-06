/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.framework.DesktopFrameworkComponentFactory;
import com.valloc.transport.DesktopClientConnector;
import com.valloc.transport.DesktopTransportComponentFactory;

/**
 * 
 * 
 * @author wstevens
 */
public class DesktopSessionComponentFactory extends AbstractClientSessionComponentFactory
{
	private DesktopTransportComponentFactory desktopTransportComponentFactory;
	private DesktopFrameworkComponentFactory desktopFrameworkComponentFactory;
	private DesktopSessionManager sessionManager;
	
	@Override
	public void initialize() {
		sessionManager = newDesktopSessionManager();

		setInitialized(true);
	}

	public DesktopSessionManager getSessionManager() {
		return sessionManager;
	}

	public DesktopClientSession createAndRegisterDesktopSession(final String username) {
		validate();
		final DesktopClientSession session = newDesktopClientSession();
		final SessionId sessionId = newSessionId(username);
		session.setId(sessionId);
		session.setUsername(username);
		
		// build and associate transport connector 
		final DesktopClientConnector connector = desktopTransportComponentFactory.createAndRegisterDesktopClientConnector(session);
		session.setClientConnector(connector);
		
		// set manager references
		sessionManager.setDesktopClientSession(session);
		session.setFrameworkManager(desktopFrameworkComponentFactory.getFrameworkManager());
		
		return session;
	}

	protected DesktopClientSession newDesktopClientSession() {
		return new DesktopClientSessionBase();
	}
	
	protected DesktopSessionManager newDesktopSessionManager() {
		return new DesktopSessionManagerBase();
	}
	
	public void setDesktopTransportComponentFactory(final DesktopTransportComponentFactory desktopTransportComponentFactory) {
		this.desktopTransportComponentFactory = desktopTransportComponentFactory;
	}

	public void setDesktopFrameworkComponentFactory(final DesktopFrameworkComponentFactory desktopFrameworkComponentFactory) {
		this.desktopFrameworkComponentFactory = desktopFrameworkComponentFactory;
	}
}
