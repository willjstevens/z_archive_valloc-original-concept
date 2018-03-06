/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system.support;

import com.valloc.domain.DomainObjectSupport;
import com.valloc.domain.StringSerializer;
import com.valloc.domain.system.Desktop;

/**
 *
 *
 * @author wstevens
 */
public class DesktopSupport implements DomainObjectSupport<Desktop>
{

	@Override
	public Class<Desktop> getType() {
		return Desktop.class;
	}

	@Override
	public void registerStringAliases(final StringSerializer serializer) {
		serializer.setClassAlias("desktop", getType());
		
		serializer.setFieldAlias("host-config", getType(), "hostConfig");
	}

	@Override
	public Desktop toDto() {
		return null;
	}

}
