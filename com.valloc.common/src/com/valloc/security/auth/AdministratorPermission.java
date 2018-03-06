/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.auth;

import static com.valloc.Constants.*;

import java.security.BasicPermission;

/**
 * 
 *
 * @author wstevens
 */
public class AdministratorPermission extends BasicPermission
{	private static final long serialVersionUID = -6052704088814797205L;
	
	/**
	 * @param name
	 */
	public AdministratorPermission()
	{
		super("Administrator");
	}
	
	/* 
	 * @see java.security.Permission#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append('(');		
		builder.append(QUOTE).append(getClass().getName()).append(QUOTE).append(COMMA);
		builder.append(QUOTE).append(getName()).append(QUOTE);
		builder.append(')');
		return builder.toString();
	}
}