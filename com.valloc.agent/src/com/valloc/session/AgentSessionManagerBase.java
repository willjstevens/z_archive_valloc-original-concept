/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

/**
 *
 *
 * @author wstevens
 */
public final class AgentSessionManagerBase extends AbstractClientSessionManager implements AgentSessionManager
{
	private AgentClientSession session;

	@Override
	public void setDesktopClientSession(final AgentClientSession session) {
		this.session = session;
	}

	@Override
	public AgentClientSession getServerSession() {
		return session;
	}

	@Override
	public void removeServerSession() {
		session.setIsValid(false);
		session = null;
	}
}
