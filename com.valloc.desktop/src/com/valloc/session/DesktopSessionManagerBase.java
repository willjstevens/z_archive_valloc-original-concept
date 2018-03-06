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
public final class DesktopSessionManagerBase extends AbstractClientSessionManager implements DesktopSessionManager
{
	private DesktopClientSession session;

	@Override
	public void setDesktopClientSession(final DesktopClientSession session) {
		this.session = session;
	}

	@Override
	public DesktopClientSession getServerSession() {
		return session;
	}

	@Override
	public void removeServerSession() {
		session.setIsValid(false);
		session = null;
	}
}
