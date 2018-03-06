/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework.service;

import com.valloc.Constants;
import com.valloc.framework.ReferenceKit;
import com.valloc.service.AbstractCommonServiceFactory;
import com.valloc.service.Service;
import com.valloc.service.ServiceDirectory;

/**
 *
 *
 * @author wstevens
 */
public class FrameworkControlServiceFactory extends AbstractCommonServiceFactory
{

	@Override
	public String name() {
		return ServiceDirectory.FRAMEWORK_CONTROL;
	}

	@Override
	//	public FrameworkControlService buildService(final ReferenceKit referenceKit) {
	public <S extends Service> S buildService(final ReferenceKit referenceKit) {
		final FrameworkControlService newService = new FrameworkControlServiceBase();
		newService.setRequestContainer(referenceKit.getRequestContainer());
		initialize(newService, referenceKit);
		@SuppressWarnings(Constants.UNCHECKED)
		final S service = (S) newService;
		return service;
	}
}
