/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.service.ServiceComponentFactory;

/**
 *
 *
 * @author wstevens
 */
public class ServiceInvokerControllerPipelineHandlerFactory implements ControllerPipelineHandlerFactory
{
	private ServiceComponentFactory serviceComponentFactory;

	/* (non-Javadoc)
	 * @see com.valloc.Identifiable#id()
	 */
	@Override
	public String name() {
		return ServiceInvokerControllerPipelineHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see com.valloc.controller.ControllerPipelineHandlerFactory#newControllerPipelineHandler()
	 */
	@Override
	public ControllerPipelineHandler newControllerPipelineHandler() {
		final ServiceInvokerControllerPipelineHandler receiverHandler = new ServiceInvokerControllerPipelineHandler();
		receiverHandler.setServiceComponentFactory(serviceComponentFactory);
		return receiverHandler;
	}

	public void setServiceComponentFactory(final ServiceComponentFactory serviceComponentFactory) {
		this.serviceComponentFactory = serviceComponentFactory;
	}
}
