/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.DesktopResources;
import com.valloc.framework.DesktopReferenceKit;
import com.valloc.framework.FrameworkManager;
import com.valloc.user.service.LocalUserServiceFactory;
import com.valloc.user.service.UserServiceBase;

/**
 *
 *
 * @author wstevens
 */
public class DesktopServiceComponentFactoryBase extends AbstractServiceComponentFactory implements DesktopServiceComponentFactory
{
	private final static Class<?>[] serviceFactories = {
		LocalUserServiceFactory.class
	};

	private static final GenericServiceFactoryKit[] genericBasedServiceKits = {
		newGenericServiceFactoryKit(ServiceDirectory.USER, UserServiceBase.class)
	};

	private DesktopResources desktopResources;

	@Override
	public void initialize() {
		super.initialize();

		loadServiceFactories(serviceFactories);
		loadGenericBasedImpls(genericBasedServiceKits);

		setInitialized(true);
	}

	@Override
	DesktopReferenceKit newReferenceKit() {
		final DesktopReferenceKit refKit = new DesktopReferenceKit();
		refKit.desktopResources = desktopResources;
		return refKit;
	}

	@Override
	public DesktopResources getDesktopResources() {
		return desktopResources;
	}

	@Override
	public void setDesktopResources(final DesktopResources desktopResources) {
		this.desktopResources = desktopResources;
	}

	@Override
	GenericServiceFactoryAdapter newGenericServiceFactoryAdapter() {
		final DesktopGenericServiceFactoryAdapter adapter = new DesktopGenericServiceFactoryAdapter();
		adapter.setDesktopResources(desktopResources);
		return adapter;
	}

	@Override
	FrameworkManager getFrameworkManager() {
		return desktopResources.getFrameworkComponentFactory().getFrameworkManager();
	}
}
