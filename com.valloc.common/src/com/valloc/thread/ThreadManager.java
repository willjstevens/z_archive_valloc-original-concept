/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.thread;

import java.lang.Thread.UncaughtExceptionHandler;

import com.valloc.thread.BaseThreadManager.ThreadBuilder;

/**
 *
 *
 * @author wstevens
 */
public interface ThreadManager extends UncaughtExceptionHandler
{
	public ManagedThread newThread(Runnable implementation, ThreadCategory threadCategory, String name);
	public ThreadBuilder newThreadBuilder(Runnable implementation, ThreadCategory threadCategory, String name);
		
	void removeThread(ManagedThread managedThread);
	void reportThrowable(ManagedThread thread, Throwable t);
	void reportInterrupted(ManagedThread thread);
	void purge(); // used mostly by test cases
	
	public ThreadManagerSnapshot takeThreadManagerSnapshot();
}
