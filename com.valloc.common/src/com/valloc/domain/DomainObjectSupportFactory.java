/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain;

import java.util.HashMap;
import java.util.Map;

import com.valloc.Constants;
import com.valloc.domain.system.support.AgentSupport;
import com.valloc.domain.system.support.DesktopSupport;
import com.valloc.domain.system.support.HostConfigSupport;
import com.valloc.domain.system.support.ParticipantSupport;
import com.valloc.domain.system.support.ProxyHostConfigSupport;
import com.valloc.domain.system.support.ServerHostConfigSupport;
import com.valloc.domain.system.support.ServerSupport;
import com.valloc.domain.system.support.WireMessageSupport;

/**
 *
 *
 * @author wstevens
 */
public final class DomainObjectSupportFactory
{
	private static final DomainObjectSupportFactory factory = new DomainObjectSupportFactory();
	private static final Map<Class<?>, DomainObjectSupport<?>> domainSupportLookup = new HashMap<Class<?>, DomainObjectSupport<?>>();
	
	private static final DomainObjectSupport<?>[] DOMAIN_SUPPORT_IMPL = {
		new AgentSupport(),
		new DesktopSupport(),
		new HostConfigSupport(),
		new ParticipantSupport(),
		new ProxyHostConfigSupport(),
		new ServerHostConfigSupport(),
		new ServerSupport(),
		new WireMessageSupport()
	}; 
	
	private DomainObjectSupportFactory() {}
	
	public static DomainObjectSupportFactory getDomainObjectSupportFactory() {
		return factory;
	}
	
	public void initializeDomainSupport(final StringSerializer serializer) {
		for (final DomainObjectSupport<?> domainObjectSupport : DOMAIN_SUPPORT_IMPL) {
			final Class<?> type = domainObjectSupport.getType();
			domainSupportLookup.put(type, domainObjectSupport);
			domainObjectSupport.registerStringAliases(serializer);
		}
	}
	
	public <T> DomainObjectSupport<T> getDomainObjectSupport(final Class<T> domainType) {
		if (domainSupportLookup.isEmpty()) {
			throw new IllegalStateException("Domain object support classes not initialized and loaded.");
		}
		@SuppressWarnings(Constants.UNCHECKED)
		final DomainObjectSupport<T> domainSupport = (DomainObjectSupport<T>) domainSupportLookup.get(domainType); 
		return domainSupport;
	}
}
