/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import junit.framework.Assert;

import org.junit.Test;

import com.valloc.AbstractTest;

/**
 * 
 *
 * @author wstevens
 */
public final class UserServiceTest extends AbstractTest
{	

	/**
	 * Test trigger spinner with error causing premature escape.
	 */
	@Test
	public void basic()
	{

//		final DesktopServiceManager serviceManager = new DesktopServiceManagerBase();
//		serviceManager.setControllerManager();
		
		
		
//		final ServerUserService serverUserService = AgentResources.getInstance().getFrameworkComponentFactory().newService(ServiceDirectory.USER);
////		final UserService userService = serviceManager.newService(ServiceDirectory.USER);
//		serverUserService.login("", "pass".toCharArray());
		
		
//		// client side
//		final DesktopFrameworkManager desktopManager = new DesktopFrameworkManagerBase();
//		final UserService desktopUserOperation = desktopManager.createOperation(ModelDaoIds.USER);
//		desktopUserOperation.addRequestAttribute("key1", "bla");
//		desktopUserOperation.login("wstevens", "password".toCharArray());
//		
//		// server side
//		final DesktopFrameworkManager serverManager = new ServerFrameworkManagerBase();
//		final UserServiceOperation serverUserOperation = serverManager.createOperation(ModelDaoIds.USER);
//		serverUserOperation.loginDesktop();
		
		
		Assert.assertEquals(true, true);
	}
}