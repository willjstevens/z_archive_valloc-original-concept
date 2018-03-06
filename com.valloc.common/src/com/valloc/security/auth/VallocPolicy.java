/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.auth;

import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;

/**
 * 
 *
 * @author wstevens
 */
public final class VallocPolicy extends Policy
{
	/* 
	 * @see java.security.Policy#implies(java.security.ProtectionDomain, java.security.Permission)
	 */
	@Override
	public boolean implies(ProtectionDomain domain, Permission permission)
	{
		// here any non-Valloc, or system ProtectionDomains are automatically allowed to pass
		if (!(domain instanceof VallocProtectionDomain)) {
			return true; 
		}
		
		return super.implies(domain, permission);
	}
}
