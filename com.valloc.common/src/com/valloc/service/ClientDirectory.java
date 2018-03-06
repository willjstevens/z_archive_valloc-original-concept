/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import static com.valloc.Constants.DOT;

/**
 *
 *
 * @author wstevens
 */
public class ClientDirectory
{
	private ClientDirectory() {}

	private static final String CLIENT					= "client" + DOT;

	public static final String FRAMEWORK_CONTROL	= CLIENT + ServiceDirectory.FRAMEWORK_CONTROL;


}
