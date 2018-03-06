/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.HashMap;
import java.util.Map;

import com.valloc.concurrent.request.RequestContainer;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.service.Service;
import com.valloc.util.UniqueId;

/**
 * A top level reference to be passed from operation factory to factory containing all shareable resources between resources.
 *
 * @author wstevens
 */
public abstract class ReferenceKit
{
	public FrameworkRequest request;
	private Service service;
	private String serviceName;
	private UniqueId serviceId;
	private InterruptTracker interruptTracker;
	private final Map<String, ServiceInfo> referencedServices = new HashMap<String, ServiceInfo>();
	private RequestContainer requestContainer;

	public class ServiceInfo {
		public Service service;
		public boolean isLoaded;
	}

	public void addReferencedService(final Service service) {
		final ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.service = service;
		referencedServices.put(service.name(), serviceInfo);
	}

	public boolean isPrimaryServiceSet() {
		return service != null;
	}

	public UniqueId getServiceId() {
		return serviceId;
	}

	public void setServiceId(final UniqueId serviceId) {
		this.serviceId = serviceId;
	}

	public Service getService() {
		return service;
	}

	public void setService(final Service service) {
		this.service = service;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(final String serviceName) {
		this.serviceName = serviceName;
	}

	public InterruptTracker getInterruptTracker() {
		return interruptTracker;
	}

	public void setInterruptTracker(final InterruptTracker interruptTracker) {
		this.interruptTracker = interruptTracker;
	}

	public Service getReferencedService(final String serviceId) {
		return referencedServices.get(serviceId).service;
	}

	public boolean isServiceLoaded(final String serviceId) {
		boolean isLoaded = false;

		final ServiceInfo serviceInfo = referencedServices.get(serviceId);
		if (serviceInfo != null && serviceInfo.isLoaded) {
			isLoaded = true;
		}

		return isLoaded;
	}

	public RequestContainer getRequestContainer() {
		return requestContainer;
	}

	public void setRequestContainer(final RequestContainer requestContainer) {
		this.requestContainer = requestContainer;
	}


}
