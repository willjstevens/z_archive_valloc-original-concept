/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.Priority;
import com.valloc.concurrent.request.RequestContainer;
import com.valloc.controller.AbstractControllerComponentFactory;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public interface FrameworkComponentFactory
{

	public FrameworkRequest newFrameworkRequest(UniqueId uniqueId, NodeType nodeType, String serviceName, String commandName, CallType callType);
	public FrameworkResponse newFrameworkResponse(UniqueId responseId);

//	public FrameworkManager getFrameworkManager();
	public <FM extends FrameworkManager> FM getFrameworkManager();

	public void setRequestContainer(RequestContainer requestContainer);
	public void setControllerComponentFactory(AbstractControllerComponentFactory controllerComponentFactory);

	abstract Priority getNodeDefaultPriority();
	RequestContainer getRequestContainer();
	AbstractControllerComponentFactory getControllerComponentFactory();
}
