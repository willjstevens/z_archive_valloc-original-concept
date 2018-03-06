/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.Constants;
import com.valloc.controller.AgentControllerComponentFactory;
import com.valloc.framework.AgentFrameworkComponentFactory;
import com.valloc.framework.AgentFrameworkManager;
import com.valloc.framework.AgentReferenceKit;
import com.valloc.session.AgentSessionComponentFactory;
import com.valloc.user.service.LocalUserServiceFactory;
import com.valloc.user.service.UserServiceBase;

/**
 *
 *
 * @author wstevens
 */
public class AgentServiceComponentFactory
//extends AbstractServiceComponentFactory<AgentFrameworkComponentFactory, DesktopControllerComponentFactory, DesktopSessionComponentFactory>
//extends AbstractServiceComponentFactory<AgentFrameworkManager, AgentFrameworkComponentFactory, AgentControllerComponentFactory, AgentSessionComponentFactory, AgentReferenceKit>
//extends AbstractServiceComponentFactory<AgentFrameworkManager, AgentFrameworkComponentFactory, AgentControllerComponentFactory, AgentSessionComponentFactory, AgentService, AgentReferenceKit>
//<AS extends Service<AgentFrameworkManager, AgentControllerComponentFactory>>
extends AbstractServiceComponentFactory
	<AgentFrameworkManager, 
	AgentFrameworkComponentFactory, 
	AgentControllerComponentFactory, 
	AgentSessionComponentFactory, 
//	AS, 
	AgentReferenceKit>
//	AgentReferenceKit<AS>>
//	AgentReferenceKit<? extends Service<AgentFrameworkManager, AgentControllerComponentFactory>>>
{
	private final static Class<?>[] serviceFactories = {
		LocalUserServiceFactory.class
	};


	@SuppressWarnings({ Constants.RAWTYPES, Constants.UNCHECKED	})
	private final static GenericServiceFactoryKit[] genericBasedServiceKits = {
		new GenericServiceFactoryKit(ServiceDirectory.USER, UserServiceBase.class)
	};

	@Override
	@SuppressWarnings(Constants.UNCHECKED)
	public void initialize() {
		super.initialize();

		loadServiceFactories(serviceFactories);
		loadGenericBasedImpls(genericBasedServiceKits);

		setInitialized(true);
	}

//	@Override
//	AgentReferenceKit<AS> newReferenceKit() {
//		return new AgentReferenceKit<AS>();
//	}
	@Override
	AgentReferenceKit newReferenceKit() {
		return new AgentReferenceKit();
	}

//	@Override
//	void enhanceReferenceKit(final AgentReferenceKit<AS> referenceKit) {
//		referenceKit.sessionManager = getSessionComponentFactory().getSessionManager();
//		referenceKit.frameworkManager = getFrameworkComponentFactory().getFrameworkManager();
//	}

	@Override
	void enhanceReferenceKit(final AgentReferenceKit referenceKit) {
		referenceKit.sessionManager = getSessionComponentFactory().getSessionManager();
		referenceKit.frameworkManager = getFrameworkComponentFactory().getFrameworkManager();
	}

}
