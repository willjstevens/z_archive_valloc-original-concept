/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import com.valloc.service.ServerService;


/**
 *
 *
 * @author wstevens
 */
//public interface ServerUserService extends Service
public interface ServerUserService extends ServerService
{
	public String login(String username, char[] password);
	public String asyncRemoteMeth(String someArg);
	public String interruptMe(boolean isSync);
	
	public void multiClientSyncCall();
}
