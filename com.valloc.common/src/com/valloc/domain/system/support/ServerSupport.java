/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system.support;

import com.valloc.domain.DomainObjectSupport;
import com.valloc.domain.StringSerializer;
import com.valloc.domain.system.Server;

/**
 *
 *
 * @author wstevens
 */
public class ServerSupport implements DomainObjectSupport<Server>
{

	@Override
	public Class<Server> getType() {
		return Server.class;
	}

	@Override
	public void registerStringAliases(final StringSerializer serializer) {
		serializer.setClassAlias("server", getType());
		
		serializer.setFieldAlias("server-host-config", getType(), "serverHostConfig");
	}

	@Override
	public Server toDto() {
		return null;
	}

}
