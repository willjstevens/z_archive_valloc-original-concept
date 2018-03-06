/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system.support;

import com.valloc.domain.DomainObjectSupport;
import com.valloc.domain.StringSerializer;
import com.valloc.domain.system.Agent;

/**
 *
 *
 * @author wstevens
 */
public class AgentSupport implements DomainObjectSupport<Agent>
{

	@Override
	public Class<Agent> getType() {
		return Agent.class;
	}

	@Override
	public void registerStringAliases(final StringSerializer serializer) {
		serializer.setClassAlias("agent", getType());
		
		serializer.setFieldAlias("host-config", getType(), "hostConfig");
		serializer.setFieldAlias("is-licensed", getType(), "isLicensed");
	}

	@Override
	public Agent toDto() {
		return null;
	}

}
