/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.Constants;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public class GenericControllerPipelineHandlerFactoryAdapter extends AbstractCommonControllerPipelineHandlerFactory
{
	private Class<? extends ControllerPipelineHandler> handlerType;

	/* (non-Javadoc)
	 * @see com.valloc.Identifiable#id()
	 */
	@Override
	public String name() {
		return handlerType.getSimpleName();
	}

	@Override
	public ControllerPipelineHandler newControllerPipelineHandler() {
		final ControllerPipelineHandler handler = Util.wrappedClassNewInstance(handlerType);

		// these are bought for free on every controller pipeline handler
		//		handler.setFrameworkComponentFactory(getFrameworkComponentFactory());

		//		Class<AbstractControllerPipelineHandler> handlerClazz = handler.getClass().cast(handler);
		@SuppressWarnings(Constants.UNCHECKED)
		final AbstractControllerPipelineHandler<FM> handlerAsAbstract = AbstractControllerPipelineHandler.class.cast(handler);
		handlerAsAbstract.setFrameworkComponentFactory(getFrameworkComponentFactory());

		handler.setSessionComponentFactory(getSessionComponentFactory());
		return handler;
	}

	public void setHandlerType(final Class<? extends ControllerPipelineHandler> handlerType) {
		this.handlerType = handlerType;
	}
}
