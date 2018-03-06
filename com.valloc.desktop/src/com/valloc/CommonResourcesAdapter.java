/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import com.valloc.controller.ControllerComponentFactory;
import com.valloc.framework.FrameworkComponentFactory;
import com.valloc.service.ServiceComponentFactory;
import com.valloc.session.SessionComponentFactory;

/**
 *
 *
 *
 * @author wstevens
 */
public class CommonResourcesAdapter implements CommonResources
{
	private final DesktopResources desktopResources;

	public CommonResourcesAdapter(final DesktopResources desktopResources) {
		this.desktopResources = desktopResources;
	}

	@Override
	public FrameworkComponentFactory getFrameworkComponentFactory() {
		return desktopResources.getFrameworkComponentFactory();
	}

	@Override
	public SessionComponentFactory getSessionComponentFactory() {
		return desktopResources.getSessionComponentFactory();
	}

	@Override
	public ControllerComponentFactory getControllerComponentFactory() {
		return desktopResources.getControllerComponentFactory();
	}

	@Override
	public ServiceComponentFactory getServiceComponentFactory() {
		return desktopResources.getServiceComponentFactory();
	}


}
