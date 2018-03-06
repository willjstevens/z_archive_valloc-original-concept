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
public interface AgentSessionManager extends SessionManager
{
	public AgentClientSession getServerSession();

	public void removeServerSession();

	void setDesktopClientSession(AgentClientSession session);
}


/*
public final class AgentSessionManager
{
	private ServerClientSession candidateSession;
	private ServerClientSession serverClientSession;

	private AgentSessionManager()
	{
	}

	public ServerClientSession getServerSession()
	{
		return serverClientSession;
	}

	// TODO: should this be passing in something like ServerHostConfig ??
	public void setCandidateServerSession(final ServerClientSession candidateSession)
	{
		this.candidateSession = candidateSession;
	}

	public void officializeCandidateSession()
	{
		serverClientSession = candidateSession;
		candidateSession = null;
		serverClientSession.setIsValid(true);
	}

	public FrameworkResponse establishServerSession(final ServerHostConfig serverHostConfig, final FrameworkRequest request)
	{


		return null;
	}
}
*/