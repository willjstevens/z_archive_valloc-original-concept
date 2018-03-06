/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author wstevens
 */
public class ServerSessionManagerBase implements ServerSessionManager
{
	// private static final Logger logger = LogManager.manager().getLogger(ServerSessionManagerBase.class, CategoryType.SESSION);

	private final Map<SessionId, ServerSession> serverSessions = new HashMap<SessionId, ServerSession>();

	@Override
	public void officializeSession(final ServerSession session) {
		session.setIsValid(true);
	}

	@Override
	public void addSession(final ServerSession serverSession) {
		serverSessions.put(serverSession.id(), serverSession);
	}

	@Override
	public void removeSession(final ServerSession serverSession) {
		serverSessions.remove(serverSession.id());
	}

	@Override
	public ServerSession getSessionById(final SessionId sessionId) {
		return serverSessions.get(sessionId);
	}
}
