/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.Command;
import com.valloc.Constants;

/**
 * Contains a list of command names that can be referenced inside a service. These are
 * associated with the {@link Command} <code>name</code> annotation attribute.
 *
 * @author wstevens
 */
public class CommandDirectory
{
	private CommandDirectory() {}

	public static final String ASYNC_LOCAL_METH = "async-local-meth";
	public static final String ASYNC_REMOTE_METH = "async-remote-meth";
	public static final String SYNC_LOCAL_METH = "sync-local-meth";
	public static final String LONG_RUNNING_METH = "long-running-meth";
	public static final String MULTI_CLIENT_SYNC_CALL = "multi-client-sync-call";
	

	private static final String LOGIN			= "login";
	public static final String LOGIN_DESKTOP	= LOGIN + Constants.DOT + "desktop";
	public static final String LOGIN_AGENT		= LOGIN + Constants.DOT + "agent";
	private static final String USER_CREATE		= "user-create";
	public static final String USER_CREATE_STG_1	= USER_CREATE + Constants.DOT + "stg-1";

	public static final String SERVER_START		= "server.start";
	public static final String SERVER_STOP		= "server.stop";

	public static final String INTERRUPT_REQUEST = "interrupt-request";
}
