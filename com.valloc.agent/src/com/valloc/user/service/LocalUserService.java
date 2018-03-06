/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import com.valloc.service.AgentService;


/**
 *
 *
 * @author wstevens
 */
//public interface LocalUserService extends Service
public interface LocalUserService extends AgentService
{
	public String syncLocalMeth(String someArg);
	public String asyncLocalMeth(String someArg);
}
