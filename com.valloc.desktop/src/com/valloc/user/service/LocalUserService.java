/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import com.valloc.service.DesktopService;


/**
 *
 *
 * @author wstevens
 */
public interface LocalUserService extends DesktopService
{
	public String syncLocalMeth(String someArg);
	public String asyncLocalMeth(String someArg);
}
