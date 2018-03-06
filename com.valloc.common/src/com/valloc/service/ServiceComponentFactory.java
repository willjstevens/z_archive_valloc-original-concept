/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.interrupt.InterruptTracker;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public abstract interface ServiceComponentFactory
{
	/* Used for brand new service which will create a new service ID. */
	//	public Service newClientService(String serviceName);
	//	/* Creates a brand new service with a known unique ID. */
	//	public Service newControllerService(String serviceName, UniqueId serviceId, InterruptTracker interruptTracker);

	public <S extends Service> S newClientService(String serviceName);
	/* Creates a brand new service with a known unique ID. */
	public <S extends Service> S newControllerService(String serviceName, UniqueId serviceId, InterruptTracker interruptTracker);


	//	abstract FrameworkComponentFactory getFrameworkComponentFactory();
	//	abstract ControllerComponentFactory getControllerComponentFactory();
	//	abstract SessionComponentFactory getSessionComponentFactory();
	//
	//	public abstract void setFrameworkComponentFactory(FrameworkComponentFactory frameworkComponentFactory);
	//	public abstract void setControllerComponentFactory(ControllerComponentFactory controllerComponentFactory);
	//	public abstract void setSessionComponentFactory(SessionComponentFactory sessionComponentFactory);
}
