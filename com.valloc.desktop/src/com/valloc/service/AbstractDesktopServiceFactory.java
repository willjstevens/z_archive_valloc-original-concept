/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.DesktopResources;
import com.valloc.framework.ReferenceKit;


/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractDesktopServiceFactory extends AbstractServiceFactory implements DesktopServiceFactory
{
	private DesktopResources desktopResources;

	@Override
	protected void initialize(final Service service, final ReferenceKit referenceKit) {
		super.initialize(service, referenceKit);
		final DesktopService desktopService = (DesktopService) service;
		desktopService.setDesktopResources(desktopResources);
	}

	@Override
	public DesktopResources getDesktopResources() {
		return desktopResources;
	}

	@Override
	public void setDesktopResources(final DesktopResources desktopResources) {
		this.desktopResources = desktopResources;
	}
}
