/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.auth;


import java.security.Policy;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.valloc.AbstractTest;


/**
 * 
 *
 * @author wstevens
 */
public final class AuthServiceTest extends AbstractTest
{		
	private Policy originalPolicy;
	
	@Before
	public void setUp() throws Exception
	{
		originalPolicy = Policy.getPolicy();
		Policy.setPolicy(new VallocPolicy());
	}
	
	@After 
	public void tearDown() throws Exception
	{
		Policy.setPolicy(originalPolicy); // restore
	}
	
	@Test
	public void testDeploymentPermission()
	{		
		DeploymentPermission dev1Perm = new DeploymentPermission("Dev", true);
		DeploymentPermission dev2Perm = new DeploymentPermission("Dev", true);
		DeploymentPermission dev3Perm = new DeploymentPermission("Dev", false);
		DeploymentPermission qaPerm = new DeploymentPermission("QA", true);
		DeploymentPermission prodPerm = new DeploymentPermission("Prod", true);
		DeploymentPermission prodSpectatorPerm = new DeploymentPermission("Prod Spectator", false);
		DeploymentPermission prodDeployerPerm = new DeploymentPermission("Prod Deployer", true);
				
		// test two samely-configured but physically different permission objects equal  
		Assert.assertTrue(dev1Perm.equals(dev2Perm));
		Assert.assertTrue(dev1Perm.hashCode() == dev2Perm.hashCode());
		// test two differently configured objects do not equal
		Assert.assertFalse(qaPerm.equals(prodPerm));
		Assert.assertTrue(dev1Perm.hashCode() == dev2Perm.hashCode()); 
		// testing if different configured object test to false
		Assert.assertFalse(prodPerm.equals(prodSpectatorPerm));
		Assert.assertFalse(prodPerm.hashCode() == prodSpectatorPerm.hashCode());
		Assert.assertFalse(prodSpectatorPerm.equals(prodDeployerPerm));
		Assert.assertFalse(prodSpectatorPerm.hashCode() == prodDeployerPerm.hashCode());
		Assert.assertFalse(dev1Perm.equals(dev3Perm));
		Assert.assertFalse(dev1Perm.hashCode() == dev3Perm.hashCode());
	}		
	
	public static void main(String[] args)
	{
		AuthServiceTest test = new AuthServiceTest();
		try {
			test.setUp();
			test.testAuthorization();
			test.tearDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthorization()
	{	
		AdministratorPermission adminPerm = new AdministratorPermission();		
		DeploymentPermission developmentDeployerPerm = new DeploymentPermission("Development", true);
		DeploymentPermission qaDeployerPerm = new DeploymentPermission("QA", true);
		DeploymentPermission prodDeployerPerm = new DeploymentPermission("Prod", true);
		DeploymentPermission qaSpectatorPerm = new DeploymentPermission("QA", false);
		DeploymentPermission prodSpectatorPerm = new DeploymentPermission("Prod", false);
							
		RolePrincipal devDeployerRole = new RolePrincipal("Development Deployer", "A developer who is also permitted spectator roles in other environments.");
		devDeployerRole.addPermission(developmentDeployerPerm);
		devDeployerRole.addPermission(qaSpectatorPerm);
		devDeployerRole.addPermission(prodSpectatorPerm);
		
		RolePrincipal qaDeployerRole = new RolePrincipal("QA Deployer", "An administrator allowed to deploy to a QA environment.");		
		qaDeployerRole.addPermission(qaDeployerPerm); // this implies the spectator role, right?
		qaDeployerRole.addPermission(qaSpectatorPerm);  
				
		RolePrincipal prodDeployerRole = new RolePrincipal("Production Deployer", "An administrator allowed to deploy to the production environment.");		
		prodDeployerRole.addPermission(prodDeployerPerm); // this implies the spectator role, right?
		prodDeployerRole.addPermission(prodSpectatorPerm);
		prodDeployerRole.addPermission(adminPerm);
				
		AuthService authService = new AuthServiceImpl();
		authService.addCorePrincipal(devDeployerRole);
		authService.addCorePrincipal(qaDeployerRole);
		authService.addCorePrincipal(prodDeployerRole);

		Set<Principal> willTheDeveloper = new HashSet<Principal>();
		willTheDeveloper.add(devDeployerRole);		
		Subject willsSubject = new Subject(false, willTheDeveloper, Collections.EMPTY_SET, Collections.EMPTY_SET); 
		
		// test three different granted permissions
		Assert.assertTrue(authService.authorize(willsSubject, developmentDeployerPerm));
		Assert.assertTrue(authService.authorize(willsSubject, qaSpectatorPerm));
		Assert.assertTrue(authService.authorize(willsSubject, prodSpectatorPerm));
		// test two different non-granted permissions of same Permission class type
		Assert.assertFalse(authService.authorize(willsSubject, qaDeployerPerm));
		Assert.assertFalse(authService.authorize(willsSubject, prodDeployerPerm));
		// test a different non-granted permission of different type
		Assert.assertFalse(authService.authorize(willsSubject, adminPerm));	
		
		// test the DeploymentPermission implies method		
		DeploymentPermission developmentSpectatorPerm = new DeploymentPermission("Development", false);
		Assert.assertTrue(authService.authorize(willsSubject, developmentSpectatorPerm));
		developmentSpectatorPerm = new DeploymentPermission("DevelXXXXopment", false);
		Assert.assertFalse(authService.authorize(willsSubject, developmentSpectatorPerm));
		developmentSpectatorPerm = new DeploymentPermission("DevelXXXXopment", true);
		Assert.assertFalse(authService.authorize(willsSubject, developmentSpectatorPerm));
	}
	
}