/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.valloc.CategoryType;
import com.valloc.Command;
import com.valloc.MessageSummary;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.interrupt.InterruptEscapeException;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.service.Service;
import com.valloc.service.ServiceComponentFactory;
import com.valloc.util.UniqueId;


/**
 *
 *
 * @author wstevens
 */
class ServiceInvokerControllerPipelineHandler extends AbstractControllerPipelineHandler
{
	private static final Logger logger = LogManager.manager().getLogger(ServiceInvokerControllerPipelineHandler.class, CategoryType.CONTROLLER);
	private ServiceComponentFactory serviceComponentFactory;

	@Override
	public String name() {
		return ServiceInvokerControllerPipelineHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute(final PipelineState pipelineState) {
		final FrameworkRequest request = getFrameworkRequest();
		final UniqueId requestId = request.id();
		final String serviceName = request.getServiceName();
		final String commandName = request.getCommandName();
		final Object[] args = request.getCommandArgs().toArray();
		final InterruptTracker interruptTracker = getInterruptTracker();

		final Service service = serviceComponentFactory.newControllerService(serviceName, requestId, interruptTracker);

		Method targetServiceMethod = null;
		search: for (final Method serviceMethod : service.getClass().getMethods()) {
			for (final Annotation methodAnnotation : serviceMethod.getAnnotations()) {
				final Class<? extends Annotation> annoClazz = methodAnnotation.annotationType();
				if (annoClazz.equals(Command.class)) {
					final Command candidateAnnotation = (Command) methodAnnotation;
					final String candidateAnnoName = candidateAnnotation.name();
					if (commandName.equals(candidateAnnoName)) {
						targetServiceMethod = serviceMethod;
						break search;
					}
				}
			}
		}

		MessageSummary messageSummary = null; 
		Object serviceRetval = null; // could be null
		try {
			serviceRetval = targetServiceMethod.invoke(service, args);
			
			// completed if here
			messageSummary = service.isMessageSummaryInitialized() ? service.getMessageSummary() : MessageSummary.SUCCESS;
		} catch (final InterruptEscapeException swallow) {
			messageSummary = MessageSummary.INTERRUPT;
			// anticipated, so log as FINE and move on
			logger.fine("Received interrupt request for ... ");
		} catch (final IllegalArgumentException e) {
			logger.error("FIXME", e);
		} catch (final IllegalAccessException e) {
			logger.error("FIXME", e);
		} catch (final InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof InterruptEscapeException) {
				messageSummary = interruptTracker.getFuture().getResult().getMessageSummary();
			} else {
				logger.error("FIXME", e);
			}
		} catch (final Exception e) {
			logger.error("FIXME", e);
		}

		// transfer data from service result into framework response
		final FrameworkResponse response = getFrameworkResponse();
		response.setMessageSummary(messageSummary);
		// could be null if command does not intrinsically return a value
		response.setReturnValue(serviceRetval);

		// TODO: This necessary ??
		pipelineState.setComplete(true);
	}

	public void setServiceComponentFactory(final ServiceComponentFactory serviceComponentFactory) {
		this.serviceComponentFactory = serviceComponentFactory;
	}
}
