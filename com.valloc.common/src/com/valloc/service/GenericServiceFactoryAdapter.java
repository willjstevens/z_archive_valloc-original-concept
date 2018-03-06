/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.Constants;
import com.valloc.framework.ReferenceKit;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public class GenericServiceFactoryAdapter extends AbstractServiceFactory
{
	private String id;
	private Class<? extends Service> serviceType;

	@Override
	public String name() {
		return id;
	}

	@Override
	public <S extends Service> S buildService(final ReferenceKit referenceKit) {
		@SuppressWarnings(Constants.UNCHECKED)
		final S service =  (S) Util.wrappedClassNewInstance(serviceType);
		initialize(service, referenceKit);
		return service;
	}

	public <S extends Service> void setServiceType(final Class<S> serviceType) {
		this.serviceType = serviceType;
	}

	public void setId(final String id) {
		this.id = id;
	}
}
