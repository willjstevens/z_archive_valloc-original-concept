/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.auth;

import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Principal;
import java.security.ProtectionDomain;

/**
 * 
 *
 * @author wstevens
 */
public class VallocProtectionDomain extends ProtectionDomain
{
	/**
	 * @param codesource
	 * @param permissions
	 * @param classloader
	 * @param principals
	 */
	public VallocProtectionDomain(CodeSource codesource, PermissionCollection permissions, ClassLoader classloader, Principal[] principals)
	{
		super(codesource, permissions, classloader, principals);
	}	
}
