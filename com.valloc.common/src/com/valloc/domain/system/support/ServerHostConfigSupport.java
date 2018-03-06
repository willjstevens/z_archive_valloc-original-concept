/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system.support;

import com.valloc.domain.DomainObjectSupport;
import com.valloc.domain.StringSerializer;
import com.valloc.domain.system.ServerHostConfig;

/**
 *
 *
 * @author wstevens
 */
public final class ServerHostConfigSupport implements DomainObjectSupport<ServerHostConfig>
{

	@Override
	public Class<ServerHostConfig> getType() {
		return ServerHostConfig.class;
	}

	@Override
	public void registerStringAliases(final StringSerializer serializer) {
		serializer.setClassAlias("server-host-config", getType());
		
		serializer.setFieldAlias("port", getType(), "port");
	}

	@Override
	public ServerHostConfig toDto() {
		return null;
	}

}
