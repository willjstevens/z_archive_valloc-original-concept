/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import java.util.Date;

import com.valloc.Command;
import com.valloc.MessageSummaryStatus;
import com.valloc.MessageType;
import com.valloc.interrupt.InterruptHandler;
import com.valloc.interrupt.InterruptType;
import com.valloc.service.AbstractServerService;
import com.valloc.service.CommandDirectory;
import com.valloc.service.ServiceDirectory;
import com.valloc.util.Result;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
//public class ServerUserServiceBase extends AbstractService implements ServerUserService, InterruptHandler
public class ServerUserServiceBase extends AbstractServerService implements ServerUserService, InterruptHandler
{
	@Override
	public String name() {
		return ServiceDirectory.USER;
	}

	@Override
	@Command(name=CommandDirectory.LOGIN_DESKTOP)
	public String login(final String username, final char[] password) {
		String msg = "TOUCHDOWN! From the server-side: method login()";
		msg += " Args are " + username + " " + new String(password);
		System.out.println(msg);

		final String retval = "Message was received in command " + CommandDirectory.LOGIN_DESKTOP + " on " + new Date();

		final Result result = new Result(MessageSummaryStatus.SUCCESS, retval);
		result.addResultMessage("msg", MessageType.USER_INFORMATION, "Report this message to user.");

		return retval;
	}

	@Override
	@Command(name=CommandDirectory.ASYNC_REMOTE_METH)
	public String asyncRemoteMeth(final String someArg) {
		Util.quietSleep(1000);
		String msg = "TOUCHDOWN! From the server-side: method asyncRemoteMeth()";
		msg += " Arg is " + someArg;
		System.out.println(msg);

		final String retval = "Message was received in command " + CommandDirectory.ASYNC_REMOTE_METH + " on " + new Date();
		final Result result = new Result(MessageSummaryStatus.SUCCESS, retval);
		result.addResultMessage("msg", MessageType.USER_INFORMATION, "Report this message to user.");

		return retval;
	}

	@Override
	@Command(name=CommandDirectory.LONG_RUNNING_METH)
	public String interruptMe(final boolean isSync) {

		getInterruptTracker().subscribeInterrputHandler(this);

		for (int i = 0; i < 100; i++) {
			Util.quietSecondsSleep(1);
			System.out.println("Interrupt test heartbeat.");
			getInterruptTracker().checkInterrupt();
		}

		{
			throw new IllegalStateException("BOMB !!!    this code should never be executed !!!!");
		}
	}

	@Override
	public void handleInterrupt(final InterruptType interruptType, final Result result) {
		result.addResultMessage("interrupt-type", MessageType.USER_INFORMATION, "The ServerUserServiceBase service was interrupted from outside by InterruptType: " + interruptType);
		getInterruptTracker().unsubscribeInterrputHandler(this);
	}

	@Override
	@Command(name=CommandDirectory.MULTI_CLIENT_SYNC_CALL)
	public void multiClientSyncCall() {
//		getControllerComponentFactory().new
	}

	
	
}
