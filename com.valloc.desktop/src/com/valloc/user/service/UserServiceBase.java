/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import java.util.Date;

import com.valloc.MessageSummary;
import com.valloc.MessageType;
import com.valloc.controller.AsynchronousClientController;
import com.valloc.controller.SynchronousClientController;
import com.valloc.interrupt.InterruptHandler;
import com.valloc.interrupt.InterruptHandlerAdapter;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.interrupt.InterruptType;
import com.valloc.service.AbstractDesktopService;
import com.valloc.service.CommandDirectory;
import com.valloc.service.ServiceDirectory;
import com.valloc.util.Result;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public class UserServiceBase extends AbstractDesktopService implements UserService
{
	@Override
	public String name() {
		return ServiceDirectory.USER;
	}

	@Override
	public Result syncLocalMeth(final String someArg) {

		//		final SynchronousClientController controller = getControllerComponentFactory().newClientLocalSyncController(ServiceDirectory.USER_LOCAL, CommandDirectory.SYNC_LOCAL_METH, this);
		final SynchronousClientController controller = controllerFactory().newClientLocalSyncController(ServiceDirectory.USER_LOCAL, CommandDirectory.SYNC_LOCAL_METH, this);
		// code here to get new request object, populate and set into controller
		controller.addArgLast("Hi");
		controller.submitSync();

		final String retval = (String) controller.getReturnValue();

		System.out.println("RETVAL RECEIVED BACK: " + retval);

		final MessageSummary messageSummary = controller.getMessageSummary();
		System.out.println("Is successful login? " + messageSummary.isSuccess());

		return controller.summarizeToResult();
	}

	@Override
	public Result login(final String username, final char[] password) {
		// bla bla processing...

		//		final SynchronousClientController controller = getControllerComponentFactory().newClientRemoteSyncController(ServiceDirectory.USER, CommandDirectory.LOGIN_DESKTOP, this);
		final SynchronousClientController controller = controllerFactory().newClientRemoteSyncController("", "", this);

		// code here to get new request object, populate and set into controller
		controller.addArgLast(username);
		controller.addArgLast(password);
		controller.submitSync();

		final String retval = (String) controller.getReturnValue();

		System.out.println("RETVAL RECEIVED BACK: " + retval);

		final MessageSummary messageSummary = controller.getMessageSummary();
		System.out.println("Is successful login? " + messageSummary.isSuccess());

		return controller.summarizeToResult();
	}

	@Override
	public Result asyncRemoteMeth(final String someArg) {
		final AsynchronousClientController controller = controllerFactory().newClientRemoteAsyncController(ServiceDirectory.USER, CommandDirectory.ASYNC_REMOTE_METH, this);

		controller.addArgLast(someArg);
		controller.submitAsync();

		// do work here...

		System.out.println(new Date() + " BEFORE block");

		controller.blockForCompletion();

		System.out.println(new Date() + " AFTER block");

		// resume and analyize results

		final MessageSummary messageSummary = controller.getMessageSummary();
		System.out.println(messageSummary);

		System.out.println("\n\nRETURNED STRING: " + controller.getFrameworkResponse().getReturnValue() + "\n\n");

		return controller.summarizeToResult();
	}

	@Override
	public Result asyncLocalMeth(final String someArg) {
		final AsynchronousClientController controller = controllerFactory().newClientLocalAsyncController(ServiceDirectory.USER_LOCAL, CommandDirectory.ASYNC_LOCAL_METH, this);
		controller.addArgLast(someArg);
		controller.submitAsync();

		// do work here...

		controller.blockForCompletion();

		// resume and analyize results

		final MessageSummary messageSummary = controller.getMessageSummary();
		System.out.println(messageSummary);
		System.out.println("\n\nRETURNED STRING: " + controller.getFrameworkResponse().getReturnValue() + "\n\n");

		return controller.summarizeToResult();
	}

	@Override
	public Result interruptMe(final boolean isSync) {
		final Result result = null;
		final InterruptTracker interruptTracker = getInterruptTracker();

		interruptTracker.markIsJoinableInterrupt();

		// bla bla processing...

		if (isSync) {
			final InterruptHandler syncInterruptHandler = new InterruptHandlerAdapter(this) {
				@Override public void handleInterrupt(final InterruptType interruptType, final Result result) {
					final String msg = "Interrupted SYNC local user service base!";
					result.addResultMessage("local-interrupt-msg", MessageType.USER_WARNING, msg);
					System.out.println(msg);
					interruptTracker.unsubscribeInterrputHandler(this);
				}
			};
			interruptTracker.subscribeInterrputHandler(syncInterruptHandler);
			final SynchronousClientController controller = controllerFactory().newClientRemoteSyncController(ServiceDirectory.USER, CommandDirectory.LONG_RUNNING_METH, this);
			controller.addArgLast(isSync);
			controller.submitSync();
			// immediately check interrupt status after returning from blocking submit sync
			interruptTracker.checkInterrupt();
			final String retval = (String) controller.getReturnValue();
			System.out.println("retval received back: " + retval);
			final MessageSummary messageSummary = controller.getMessageSummary();
			System.out.println("Summary status " + messageSummary.toString());
			interruptTracker.unsubscribeInterrputHandler(syncInterruptHandler);

		} else { // async block
			final InterruptHandler asyncInterruptHandler = new InterruptHandlerAdapter(this) {
				@Override public void handleInterrupt(final InterruptType interruptType, final Result result) {
					final String msg = "Interrupted ASYNC local user service base!";
					result.addResultMessage("local-interrupt-msg", MessageType.USER_WARNING, msg);
					System.out.println(msg);
					interruptTracker.unsubscribeInterrputHandler(this);
				}
			};
			interruptTracker.subscribeInterrputHandler(asyncInterruptHandler);
			final AsynchronousClientController controller = controllerFactory().newClientRemoteAsyncController(ServiceDirectory.USER, CommandDirectory.LONG_RUNNING_METH, this);
			controller.addArgLast(isSync);
			controller.submitAsync();
			for (int i = 0; i < 3; i++) {
				Util.quietSecondsSleep(1);
				System.out.println("Async client heartbeat.");
				interruptTracker.checkInterrupt();
			}
			controller.blockForCompletion();
			// immediately check interrupt status after returning from blocking submit sync
			interruptTracker.checkInterrupt();
			final String retval = (String) controller.getReturnValue();
			System.out.println("retval received back: " + retval);
			final MessageSummary messageSummary = controller.getMessageSummary();
			System.out.println("Summary status " + messageSummary.toString());
			interruptTracker.unsubscribeInterrputHandler(asyncInterruptHandler);

		}

		return result;
	}
}
