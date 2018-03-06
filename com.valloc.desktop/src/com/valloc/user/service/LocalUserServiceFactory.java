/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import com.valloc.Constants;
import com.valloc.framework.ReferenceKit;
import com.valloc.service.AbstractDesktopServiceFactory;
import com.valloc.service.Service;
import com.valloc.service.ServiceDirectory;

/**
 *
 *
 * @author wstevens
 */
public class LocalUserServiceFactory extends AbstractDesktopServiceFactory
{

	@Override
	public String name() {
		return ServiceDirectory.USER_LOCAL;
	}

	@Override
	public <S extends Service> S buildService(final ReferenceKit referenceKit) {
		final LocalUserService localUserService = new LocalUserServiceBase();
		initialize(localUserService, referenceKit);
		@SuppressWarnings(Constants.UNCHECKED)
		final S service = (S) localUserService;
		return service;
	}
}
