/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.auth;

import java.security.Permission;
import java.security.Permissions;
import java.security.Principal;

/**
 * 
 *
 * @author wstevens
 */
public class RolePrincipal implements Principal
{
	private String name;
	private String description;
	private Permissions permissions = new Permissions();
		
	/**
	 * 
	 */
	public RolePrincipal(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	/* 
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @return String Returns the description.
	 */
	String getDescription()
	{
		return description;
	}

	/* 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof RolePrincipal)) { 
			return false;
		}
		
		boolean retval = false;
		String subjectName = ((RolePrincipal) obj).getName();
		retval = name.equals(subjectName);
		return retval;
	}

	/* 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		// used as key in HashMap so this is implemented using hashCode of the lowercase version of the name:
		return name.toLowerCase().hashCode(); 
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return name;
	}
		
	public <T extends Permission> void addPermission(T permission)
	{
		permissions.add(permission);
	}

	/**
	 * @return Permissions Returns the permissions.
	 */
	Permissions getPermissions()
	{
		return permissions;
	}
}
