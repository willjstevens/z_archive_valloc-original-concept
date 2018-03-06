/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.auth;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.Permissions;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

/**
 * 
 *
 * @author wstevens
 */
final class AuthServiceImpl implements AuthService
{	
	private final Map<? super Principal, AccessControlContext> roleContexts = new HashMap<Principal, AccessControlContext>();
	
	AuthServiceImpl() {}
	
	/* 
	 * @see com.valloc.security.auth.AuthService#authorize(javax.security.auth.Subject)
	 */
	@Override
	public boolean authorize(Subject subject, final Permission permission)
	{	
		boolean retval = false;
		
		PrivilegedAction<Boolean> action = new PrivilegedAction<Boolean>() {			
			@Override
			public Boolean run() {
				boolean retval = true;
				try {
					AccessController.checkPermission(permission);
				} catch (AccessControlException e) {
					retval = false;
				}
				return retval;
			}			
		};
		
		// IMPORTANT: The reason why this block manually loops through each of the subject's pricipals
		//		and retrieves the associated AccessControlContext object is to determine if only ONE 
		//		AccessControlContext object satisfies the permission - as opposed to Java's security 
		//		model that mandates the permission object (class type, name and actions uniformly) must
		//		be alotted in EACH ProtectionDomain object (or here, ProtectionDomain is encapsulated within
		//		an AccessControlContext object) so the INTERSECTION of the permission in all ProtectionDomain
		//		objects must occur in order for the permission check to succeed. This is not the case, as 
		//		what is desired is to find just ONE AccessControlObject to satisfy the permission, which again
		//		an AccessControlContext object is associated with each RolePrincipal object.	
		Set<RolePrincipal> subjectPrincipals = subject.getPrincipals(RolePrincipal.class);
		for (RolePrincipal rolePrincipal : subjectPrincipals) {
			AccessControlContext context = roleContexts.get(rolePrincipal);
			boolean isAuthorized = AccessController.doPrivileged(action, context);
			if (isAuthorized) { // all it takes is ONE RolePrincipal check to succeed
				retval = true; 
				break;
			}
		}
				
		return retval;
	}
	
	/* 
	 * @see com.valloc.security.auth.AuthService#addPrincipal(java.security.Principal)
	 */
	@Override
	public void addCorePrincipal(RolePrincipal principal)
	{	
		if (!roleContexts.containsKey(principal)) {
			
			CodeSource nullCodeSource = null;
			Permissions permissions = principal.getPermissions();
			ClassLoader nullClassLoader = null;
			RolePrincipal[] principalArray = { principal };		
			VallocProtectionDomain domain = new VallocProtectionDomain(nullCodeSource, permissions, nullClassLoader, principalArray);
	
			VallocProtectionDomain[] domains = { domain };		
			AccessControlContext roleContext = new AccessControlContext(domains);
			
			roleContexts.put(principal, roleContext);
		}
	}

	/* 
	 * @see com.valloc.security.auth.AuthService#removePrincipal(java.security.Principal)
	 */
	@Override
	public void removeCorePrincipal(RolePrincipal principal)
	{
		roleContexts.remove(principal);
	}
}