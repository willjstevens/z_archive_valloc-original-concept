/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.DesktopResources;
import com.valloc.controller.DesktopControllerComponentFactory;
import com.valloc.framework.DesktopFrameworkManager;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractDesktopService extends AbstractService implements DesktopService
{
	private DesktopResources desktopResources;

	@Override
	public DesktopResources getDesktopResources() {
		return desktopResources;
	}

	@Override
	public void setDesktopResources(final DesktopResources desktopResources) {
		this.desktopResources = desktopResources;
	}

	protected DesktopControllerComponentFactory controllerFactory() {
		return desktopResources.getControllerComponentFactory();
	}

	protected DesktopFrameworkManager frameworkManager() {
		return desktopResources.getFrameworkComponentFactory().getDesktopFrameworkManager();
	}
}
