/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import org.junit.Before;
import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.domain.MockDomainObjectFactory;
import com.valloc.model.object.AgentModelObject;
import com.valloc.transport.TransportModelDao;

/**
 *
 *
 * @author wstevens
 */
public final class ModelTest extends AbstractTest
{
	private ModelComponentFactory modelComponentFactory;
	private MockDomainObjectFactory mockDomainObjectFactory;	
	
	@Before
	public void setUp() throws Exception {
		modelComponentFactory = new ModelComponentFactory();
		mockDomainObjectFactory = new MockDomainObjectFactory();	
	}

	@Test
	public void basicUsage() {
		
		final TransportModelDao transportModelDao = modelComponentFactory.newModelDao(ModelDaoIds.TRANSPORT);
		final AgentModelObject agentModelObject = transportModelDao.loadAgentModelObject(mockDomainObjectFactory.newAgent());
		
	}
}