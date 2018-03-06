/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system.support;

import com.valloc.domain.DomainObjectSupport;
import com.valloc.domain.StringSerializer;
import com.valloc.framework.Request;

/**
 * 
 * 
 * @author wstevens
 */
public final class RequestSupport implements DomainObjectSupport<Request>
{

	@Override
	public Class<Request> getType() {
		return Request.class;
	}

	@Override
	public void registerStringAliases(final StringSerializer serializer) {
		serializer.setClassAlias("request", getType());

		// serializer.setFieldAlias("mac-address", getType(), "macAddress");
	}

	@Override
	public Request toDto() {
		return null;
	}

}
