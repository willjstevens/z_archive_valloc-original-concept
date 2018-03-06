/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.auth;

import static com.valloc.Constants.*;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * 
 *
 * @author wstevens
 */
public class DeploymentPermission extends Permission
{	private static final long serialVersionUID = -6052704088814797205L;

	private boolean canExecute;
	
	
	/**
	 * @param name
	 */
	public DeploymentPermission(String name, boolean canExecute)
	{
		super(name);
		
		this.canExecute = canExecute;
	}
	
	/* 
	 * @see java.security.Permission#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{		
		if (!(obj instanceof DeploymentPermission)) {
			return false;
		}
	
		boolean retval = false;
		DeploymentPermission subject = (DeploymentPermission) obj;		
		String subjectStringRepresentation = subject.toString();
		String thisStringRepresentation = this.toString();
		if (thisStringRepresentation.equals(subjectStringRepresentation)) {
			retval = true;
		}
		return retval;
	}

	/* 
	 * @see java.security.Permission#getActions()
	 */
	@Override
	public String getActions()
	{
		String retval = null;
		
		if (canExecute) {
			retval = "execute";
		}
		
		return retval;
	}
	
	/* 
	 * @see java.security.Permission#hashCode()
	 */
	@Override
	public int hashCode()
	{ 
		return toString().hashCode();
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
		builder.append(QUOTE).append(getName()).append(QUOTE).append(COMMA);
		builder.append(QUOTE).append(getActions()).append(QUOTE);
		builder.append(')');
		return builder.toString();
	}

	/* 
	 * @see java.security.Permission#implies(java.security.Permission)
	 */
	@Override
	public boolean implies(Permission permission)
	{
		if (!(permission instanceof DeploymentPermission)) {
			return false;
		}
		
		if (this.equals(permission)) {
			return true; // no problems here
		}
		
		// test if this permission is an execute, and the subject is only a view, then  
		//		this implies the subject for doing both execute and view   
		DeploymentPermission deploymentPermission = DeploymentPermission.class.cast(permission);				
		String thisDeploymentName = this.getName();
		String subjectDeploymentName = deploymentPermission.getName(); 
		if (thisDeploymentName.equals(subjectDeploymentName)) { // if true acknowledges deployments (by name) are same			
			if (canExecute == true) {
				// Returning true here acknowledges that if name is same and this's canExecute is true, then it 
				//		implies the subject can execute too.
				return true;	
			}			
		}
		
		return false; 
	}

	
	/* 
	 * @see java.security.Permission#newPermissionCollection()
	 */
	@Override
	public PermissionCollection newPermissionCollection()
	{
		return new PermissionCollection() {
			private static final long serialVersionUID = -2655033030857059746L;
			private List<DeploymentPermission> permissions = new ArrayList<DeploymentPermission>();
			
			@Override
			public void add(Permission permission)
			{
				permissions.add((DeploymentPermission)permission);
			}

			@Override
			public Enumeration<Permission> elements()
			{
				Vector<Permission> retval = new Vector<Permission>();				
				for (Permission permission : permissions) {
					retval.add(permission);
				}
				return retval.elements();
			}

			@Override
			public boolean implies(Permission permission)
			{
				boolean retval = false; 
				for (DeploymentPermission deploymentPermission : permissions) {
					if (deploymentPermission.implies(permission)) {
						retval = true;
						break;
					}
				}				
				return retval;
			}
		};
	}

	/**
	 * @return boolean Returns the canExecute.
	 */
	public boolean canExecute()
	{
		return canExecute;
	}	
}