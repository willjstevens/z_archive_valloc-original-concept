/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.Constants;
import com.valloc.controller.ServerControllerComponentFactoryBase;
import com.valloc.framework.ServerFrameworkComponentFactory;
import com.valloc.framework.ServerFrameworkManager;
import com.valloc.framework.ServerReferenceKit;
import com.valloc.session.ServerSessionComponentFactory;
import com.valloc.user.service.ServerUserServiceBase;

/**
 *
 *
 * @author wstevens
 */
public class ServerServiceComponentFactory
//extends AbstractServiceComponentFactory<ServerFrameworkComponentFactory, ServerControllerComponentFactory, ServerSessionComponentFactory>
//extends AbstractServiceComponentFactory<ServerFrameworkManager, ServerFrameworkComponentFactory, ServerControllerComponentFactory, ServerSessionComponentFactory, ServerReferenceKit>
//extends AbstractServiceComponentFactory<ServerFrameworkManager, ServerFrameworkComponentFactory, ServerControllerComponentFactory, ServerSessionComponentFactory, ServerService, ServerReferenceKit>
//<SS extends Service<ServerFrameworkManager, ServerControllerComponentFactory>>
extends AbstractServiceComponentFactory
	<ServerFrameworkManager, 
	ServerFrameworkComponentFactory, 
	ServerControllerComponentFactoryBase, 
	ServerSessionComponentFactory,
	ServerReferenceKit>
//	SS, 
//	ServerReferenceKit<SS>>
{
	private final static Class<?>[] serviceFactories = {
//		ServerUserServiceFactory.class
	};


	@SuppressWarnings({ Constants.RAWTYPES, Constants.UNCHECKED	})
	private final static GenericServiceFactoryKit[] genericBasedServiceKits = {
		new GenericServiceFactoryKit(ServiceDirectory.USER, ServerUserServiceBase.class)
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
//	ServerReferenceKit<SS> newReferenceKit() {
//		return new ServerReferenceKit<SS>();
//	}
//
//	@Override
//	void enhanceReferenceKit(final ServerReferenceKit<SS> referenceKit) {
//		// no references to set for now
//	}
	
	@Override
	ServerReferenceKit newReferenceKit() {
		return new ServerReferenceKit();
	}

	@Override
	void enhanceReferenceKit(final ServerReferenceKit referenceKit) {
		// no references to set for now
	}

}
