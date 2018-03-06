/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.auth;

import java.security.Permission;

import javax.security.auth.Subject;

/**
 * 
 *
 * @author wstevens
 */
public interface AuthService
{
	
//	public Subject authenticate(String username, char[] password, CallbackHandler callbackHandler);
		
	// primary authorize method, given subject object and target permission
	public boolean authorize(Subject subject, Permission permission);
		
	// adds a principal with corresponding object
	public void addCorePrincipal(RolePrincipal principal);
	
	// removes the principal and corresponding object
	public void removeCorePrincipal(RolePrincipal principal);
}
