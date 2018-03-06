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
public interface ServerSessionManager extends SessionManager
{
	public void officializeSession(final ServerSession session);

	public void addSession(final ServerSession serverSession);

	public void removeSession(final ServerSession serverSession);

	public ServerSession getSessionById(final SessionId sessionId);
}
