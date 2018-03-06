/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system.support;

import com.valloc.domain.DomainObjectSupport;
import com.valloc.domain.StringSerializer;
import com.valloc.domain.system.ProxyHostConfig;

/**
 *
 *
 * @author wstevens
 */
public final class ProxyHostConfigSupport implements DomainObjectSupport<ProxyHostConfig>
{

	@Override
	public Class<ProxyHostConfig> getType() {
		return ProxyHostConfig.class;
	}

	@Override
	public void registerStringAliases(final StringSerializer serializer) {
		serializer.setClassAlias("proxy-host-config", getType());
		
		serializer.setFieldAlias("username", getType(), "username");
		serializer.setFieldAlias("password", getType(), "password");
	}

	@Override
	public ProxyHostConfig toDto() {
		return null;
	}

}
