/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import com.valloc.Constants;

/**
 *
 *
 * @author wstevens
 */
public class CommandIds
{

	private CommandIds() {}

	public static final String LOGIN			= "login";
	public static final String LOGIN_DESKTOP	= LOGIN + Constants.DOT + "desktop";
	public static final String LOGIN_AGENT		= LOGIN + Constants.DOT + "agent";

}
