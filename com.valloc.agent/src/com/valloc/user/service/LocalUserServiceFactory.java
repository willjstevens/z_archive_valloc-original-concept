/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.user.service;

import com.valloc.framework.AgentReferenceKit;
import com.valloc.service.AbstractAgentServiceFactory;
import com.valloc.service.ServiceDirectory;

/**
 *
 *
 * @author wstevens
 */
//public class LocalUserServiceFactory extends AbstractServiceFactory<LocalUserService, AgentReferenceKit>
public class LocalUserServiceFactory extends AbstractAgentServiceFactory<LocalUserService>
{

	@Override
	public String name() {
		return ServiceDirectory.USER_LOCAL;
	}

	@Override
	public LocalUserService buildService(final AgentReferenceKit referenceKit) {
		final LocalUserService newUserService = new LocalUserServiceBase();
		addServiceToKit(newUserService, referenceKit);
		enhanceService(newUserService, referenceKit);
		return newUserService;
	}
}
