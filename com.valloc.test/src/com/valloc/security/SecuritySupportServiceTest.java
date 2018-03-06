/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security;

import org.junit.Before;
import org.junit.Test;

import com.valloc.AbstractTest;

/**
 * 
 *
 * @author wstevens
 */
public final class SecuritySupportServiceTest extends AbstractTest
{		
	private SecuritySupportServiceImpl securitySupportService;
	
	@Before
	public void setUp() throws Exception
	{
		securitySupportService = new SecuritySupportServiceImpl();
	}

	@Test
	public void testSetNewSecureRandom()
	{ 
		securitySupportService.setNewSecureRandom();
	}	
}