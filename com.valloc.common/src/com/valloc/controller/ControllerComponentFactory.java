/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.ComponentFactory;
import com.valloc.framework.FrameworkComponentFactory;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.service.Service;
import com.valloc.session.SessionComponentFactory;

/**
 *
 *
 *
 * @author wstevens
 */
public interface ControllerComponentFactory extends ComponentFactory
{

	public abstract SynchronousClientController newClientLocalSyncController(String serviceName, String commandName, Service service);
	public abstract AsynchronousClientController newClientLocalAsyncController(String serviceName, String commandName, Service service);
	public abstract SynchronousClientController newClientRemoteSyncController(String serviceName, String commandName, Service service);
	public abstract AsynchronousClientController newClientRemoteAsyncController(String serviceName, String commandName, Service service);
	public abstract ServiceController newServiceController(FrameworkRequest request, FrameworkResponse response);


	FrameworkComponentFactory getFrameworkComponentFactory();
	SessionComponentFactory getSessionComponentFactory();

	public void setFrameworkComponentFactory(FrameworkComponentFactory frameworkComponentFactory);
	public void setSessionComponentFactory(SessionComponentFactory sessionComponentFactory);
}
