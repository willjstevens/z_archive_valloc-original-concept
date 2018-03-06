/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.framework.ReferenceKit;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractServiceFactory implements ServiceFactory
{
	protected void initialize(final Service service, final ReferenceKit referenceKit) {
		service.setId(referenceKit.getServiceId());
		service.setInterruptTracker(referenceKit.getInterruptTracker());
		if (!referenceKit.isPrimaryServiceSet()) {
			referenceKit.setService(service);
		} else {
			referenceKit.addReferencedService(service);
		}
	}
}
