/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.DesktopResources;
import com.valloc.DesktopResourcesParticpator;
import com.valloc.framework.ReferenceKit;

/**
 *
 *
 *
 * @author wstevens
 */
public class DesktopGenericServiceFactoryAdapter extends GenericServiceFactoryAdapter implements DesktopResourcesParticpator
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

	@Override
	public <S extends Service> S buildService(final ReferenceKit referenceKit) {
		final S service = super.buildService(referenceKit);
		final DesktopService desktopService = (DesktopService) super.buildService(referenceKit);
		desktopService.setDesktopResources(desktopResources);
		return service;
	}


}
