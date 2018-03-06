/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system.support;

import com.valloc.domain.DomainObjectSupport;
import com.valloc.domain.StringSerializer;
import com.valloc.domain.system.HostConfig;

/**
 * 
 * 
 * @author wstevens
 */
public final class HostConfigSupport implements DomainObjectSupport<HostConfig>
{

	@Override
	public Class<HostConfig> getType() {
		return HostConfig.class;
	}

	@Override
	public void registerStringAliases(final StringSerializer serializer) {
		serializer.setClassAlias("host-config", getType());

		serializer.setFieldAlias("inet-socket-address", getType(), "inetSocketAddress");
		serializer.setFieldAlias("mac-address", getType(), "macAddress");
		serializer.setFieldAlias("proxy-host-config", getType(), "proxyHostConfig");
	}

	@Override
	public HostConfig toDto() {
		return null;
	}

}
