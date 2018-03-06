/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system.support;

import com.valloc.domain.DomainObjectSupport;
import com.valloc.domain.StringSerializer;
import com.valloc.transport.WireMessage;

/**
 * 
 * 
 * @author wstevens
 */
public final class WireMessageSupport implements DomainObjectSupport<WireMessage>
{

	@Override
	public Class<WireMessage> getType() {
		return WireMessage.class;
	}

	@Override
	public void registerStringAliases(final StringSerializer serializer) {
		serializer.setClassAlias("wire-message", getType());

		// serializer.setFieldAlias("mac-address", getType(), "macAddress");
	}

	@Override
	public WireMessage toDto() {
		return null;
	}

}
