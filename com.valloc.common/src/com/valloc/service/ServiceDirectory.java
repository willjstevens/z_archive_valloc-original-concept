/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import static com.valloc.Constants.DOT;

/**
 * Contains service names available to pass to disconnected nodes as an identifier
 * for what service to execute.
 *
 * @author wstevens
 */
public class ServiceDirectory
{
	private ServiceDirectory() {}

//	private static final String CLIENT					= "client";
	public static final String FRAMEWORK_CONTROL		= "framework-control";
//	public static final String FRAMEWORK_CONTROL_CLIENT	= FRAMEWORK_CONTROL + DOT + CLIENT;
	public static final String ADMINISTRATION 			= "administration";
	public static final String DEPLOYMENT 				= "deployment";
	public static final String USER 					= "user";
	public static final String USER_LOCAL				= USER + DOT + "local";
	public static final String MODEL					= "model";

}
