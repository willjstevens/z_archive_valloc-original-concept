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
public interface DesktopSessionManager extends SessionManager
{
	public DesktopClientSession getServerSession();

	public void removeServerSession();

	void setDesktopClientSession(DesktopClientSession session);
}
