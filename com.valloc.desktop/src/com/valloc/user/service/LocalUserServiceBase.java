/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import com.valloc.Command;
import com.valloc.MessageSummaryStatus;
import com.valloc.MessageType;
import com.valloc.service.AbstractDesktopService;
import com.valloc.service.CommandDirectory;
import com.valloc.service.ServiceDirectory;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public class LocalUserServiceBase extends AbstractDesktopService implements LocalUserService
{
	@Override
	public String name() {
		return ServiceDirectory.USER_LOCAL;
	}

	@Override
	@Command(name=CommandDirectory.ASYNC_LOCAL_METH)
	public String asyncLocalMeth(final String someArg) {
		String msg = "TOUCHDOWN! From the desktop-side: method asyncLocalMeth()";
		msg += " Arg is " + someArg;
		initializeMessageSummary(MessageSummaryStatus.SUCCESS);
		addSummaryMessage("msg", MessageType.USER_INFORMATION, msg);
		System.out.println(msg);
		Util.quietSleep(1000);
		return msg;
	}

	@Override
	@Command(name=CommandDirectory.SYNC_LOCAL_METH)
	public String syncLocalMeth(final String someArg) {
		String msg = "TOUCHDOWN! From the desktop-side: method syncLocalMeth()";
		msg += " Arg is " + someArg;
		initializeMessageSummary(MessageSummaryStatus.SUCCESS);
		addSummaryMessage("msg", MessageType.USER_INFORMATION, msg);
		System.out.println(msg);
		Util.quietSleep(1000);
		return msg;
	}

	//	@Override
	//	public void handleInterrupt() {
	//	}
}
