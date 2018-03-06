/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.Date;

import com.valloc.AbstractComponentFactory;
import com.valloc.Priority;
import com.valloc.concurrent.request.RequestContainer;
import com.valloc.controller.AbstractControllerComponentFactory;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractFrameworkComponentFactory extends AbstractComponentFactory implements FrameworkComponentFactory
{
	private RequestContainer requestContainer;
	private AbstractControllerComponentFactory controllerComponentFactory;

	@Override
	public FrameworkRequest newFrameworkRequest(final UniqueId uniqueId, final NodeType nodeType, final String serviceName, final String commandName, final CallType callType) {
		final Date startTimestamp = uniqueId.getInceptionTimestamp();
		final Priority priority = getNodeDefaultPriority();
		return new FrameworkRequest(uniqueId, nodeType, serviceName, commandName, callType, priority, startTimestamp);
	}

	@Override
	public FrameworkResponse newFrameworkResponse(final UniqueId responseId) {
		return new FrameworkResponse(responseId);
	}

	@Override
	public void setRequestContainer(final RequestContainer requestContainer) {
		this.requestContainer = requestContainer;
	}

	@Override
	public void setControllerComponentFactory(final AbstractControllerComponentFactory controllerComponentFactory) {
		this.controllerComponentFactory = controllerComponentFactory;
	}

	@Override
	public RequestContainer getRequestContainer() {
		return requestContainer;
	}

	@Override
	public AbstractControllerComponentFactory getControllerComponentFactory() {
		return controllerComponentFactory;
	}


}
