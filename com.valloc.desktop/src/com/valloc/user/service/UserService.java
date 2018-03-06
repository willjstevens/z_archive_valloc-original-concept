/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import com.valloc.service.DesktopService;
import com.valloc.util.Result;


/**
 *
 *
 * @author wstevens
 */
public interface UserService extends DesktopService
{
	public Result syncLocalMeth(String someArg);
	public Result login(String username, char[] password);
	public Result asyncRemoteMeth(String someArg);
	public Result asyncLocalMeth(String someArg);
	public Result interruptMe(boolean isSync);
}
