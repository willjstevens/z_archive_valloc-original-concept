/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import java.util.LinkedList;
import java.util.List;

import com.valloc.AbstractComponentFactory;
import com.valloc.CommonResources;
import com.valloc.controller.pipeline.CommonCommandHandlerFlagsRepository;
import com.valloc.controller.pipeline.ControllerPipelineHandler;
import com.valloc.controller.pipeline.ControllerPipelineHandlerFactory;
import com.valloc.controller.pipeline.ControllerPipelineProcessor;
import com.valloc.controller.pipeline.FrameworkControllerPipelineHandler;
import com.valloc.controller.pipeline.GenericControllerPipelineHandlerFactoryAdapter;
import com.valloc.controller.pipeline.LocalReceiverControllerPipelineHandler;
import com.valloc.controller.pipeline.LocalSenderControllerPipelineHandler;
import com.valloc.controller.pipeline.RemoteReceiverControllerPipelineHandler;
import com.valloc.controller.pipeline.RemoteSenderControllerPipelineHandler;
import com.valloc.controller.pipeline.ServiceInvokerControllerPipelineHandlerFactory;
import com.valloc.framework.AbstractFrameworkComponentFactory;
import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.framework.NodeType;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.service.ServiceComponentFactory;
import com.valloc.session.SessionComponentFactory;
import com.valloc.util.DependencyGroup;
import com.valloc.util.DependencyItem;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractControllerComponentFactory extends AbstractComponentFactory implements ControllerComponentFactory
{
	//	private static final Logger logger = LogManager.manager().getLogger(ControllerComponentFactory.class, CategoryType.CONTROLLER);
	final DependencyGroup<String, ControllerPipelineHandlerFactory> handlerDependencyGroupTemplate = new DependencyGroup<String, ControllerPipelineHandlerFactory>();

	//	ServiceComponentFactory<FM, ? extends ControllerComponentFactory<FM>> serviceComponentFactory;
	ServiceComponentFactory serviceComponentFactory;
	AbstractFrameworkComponentFactory abstractFrameworkComponentFactory;
	SessionComponentFactory sessionComponentFactory;
	CommonCommandHandlerFlagsRepository commandHandlerFlagsRepository;

	ControllerPipelineHandlerFactory frameworkControllerPipelineHandlerFactory;
	ControllerPipelineHandlerFactory remoteSenderControllerPipelineHandlerFactory;
	ControllerPipelineHandlerFactory remoteReceiverControllerPipelineHandlerFactory;
	ControllerPipelineHandlerFactory localSenderControllerPipelineHandlerFactory;
	ControllerPipelineHandlerFactory localReceiverControllerPipelineHandlerFactory;
	ServiceInvokerControllerPipelineHandlerFactory serviceInvokerControllerPipelineHandlerFactory;

	@Override
	public void initialize() {
		// first initialize and contribute factories to mappings
		initializeHandlerFactoryMappings();

		// now add dependencies
		initializeDependencies();

		setInitialized(true);
	}

	void initializeHandlerFactoryMappings() {
		// first create factory instances
		frameworkControllerPipelineHandlerFactory = newFrameworkControllerPipelineHandlerFactory();
		remoteSenderControllerPipelineHandlerFactory = newRemoteSenderControllerPipelineHandlerFactory();
		remoteReceiverControllerPipelineHandlerFactory = newRemoteReceiverControllerPipelineHandlerFactory();
		localSenderControllerPipelineHandlerFactory = newLocalSenderControllerPipelineHandlerFactory();
		localReceiverControllerPipelineHandlerFactory = newLocalReceiverControllerPipelineHandlerFactory();
		serviceInvokerControllerPipelineHandlerFactory = newServiceInvokerControllerPipelineHandlerFactory();

		// now add to handler dependency template
		addPipelineHandlerFactoryMapping(frameworkControllerPipelineHandlerFactory);
		addPipelineHandlerFactoryMapping(localSenderControllerPipelineHandlerFactory);
		addPipelineHandlerFactoryMapping(localReceiverControllerPipelineHandlerFactory);
		addPipelineHandlerFactoryMapping(remoteSenderControllerPipelineHandlerFactory);
		addPipelineHandlerFactoryMapping(remoteReceiverControllerPipelineHandlerFactory);
		addPipelineHandlerFactoryMapping(serviceInvokerControllerPipelineHandlerFactory);
	}

	void initializeDependencies() {
		// setup item refs
		final DependencyItem<String, ControllerPipelineHandlerFactory> frameworkControllerFactoryItem =
			handlerDependencyGroupTemplate.getDependencyItem(frameworkControllerPipelineHandlerFactory.name());
		final DependencyItem<String, ControllerPipelineHandlerFactory> localSenderControllerFactoryItem =
			handlerDependencyGroupTemplate.getDependencyItem(localSenderControllerPipelineHandlerFactory.name());
		final DependencyItem<String, ControllerPipelineHandlerFactory> localReceiverControllerFactoryItem =
			handlerDependencyGroupTemplate.getDependencyItem(localReceiverControllerPipelineHandlerFactory.name());
		final DependencyItem<String, ControllerPipelineHandlerFactory> remoteSenderControllerFactoryItem =
			handlerDependencyGroupTemplate.getDependencyItem(remoteSenderControllerPipelineHandlerFactory.name());
		final DependencyItem<String, ControllerPipelineHandlerFactory> remoteReceiverControllerFactoryItem =
			handlerDependencyGroupTemplate.getDependencyItem(remoteReceiverControllerPipelineHandlerFactory.name());
		final DependencyItem<String, ControllerPipelineHandlerFactory> serviceInvokerControllerPipelineHandlerFactoryItem =
			handlerDependencyGroupTemplate.getDependencyItem(serviceInvokerControllerPipelineHandlerFactory.name());

		// now set dependencies
		remoteSenderControllerFactoryItem.addDependency(frameworkControllerFactoryItem);
		localSenderControllerFactoryItem.addDependency(frameworkControllerFactoryItem);
		serviceInvokerControllerPipelineHandlerFactoryItem.addDependency(localReceiverControllerFactoryItem);
		serviceInvokerControllerPipelineHandlerFactoryItem.addDependency(remoteReceiverControllerFactoryItem);
	}

	public void addPipelineHandlerFactoryMapping(final ControllerPipelineHandlerFactory factory) {
		final String factoryId = factory.name();
		final DependencyItem<String, ControllerPipelineHandlerFactory> factoryMapping = new DependencyItem<String, ControllerPipelineHandlerFactory>(factoryId, factory);
		handlerDependencyGroupTemplate.addItemToTemplate(factoryMapping);
	}


	@Override
	public abstract ServiceController newServiceController(FrameworkRequest request, FrameworkResponse response);
	abstract FrameworkRequest newFrameworkRequest(UniqueId uniqueId, CallType callType, String serviceName, String commandName, InterruptTracker interruptTracker);
	abstract void addOptionalHandlers(NodeType nodeType, String serviceName, String commandName, LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList);
	abstract CommonResources getCommonResources();

	FrameworkResponse newFrameworkResponse(final UniqueId responseId) {
		return abstractFrameworkComponentFactory.newFrameworkResponse(responseId);
	}

	LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> newHandlerList() {
		return new LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>>();
	}

	void insertFactoryToHandlerList(final String factoryId, final List<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList) {
		final DependencyItem<String, ControllerPipelineHandlerFactory> factoryItem = handlerDependencyGroupTemplate.getDependencyItem(factoryId);
		handlerDependencyGroupTemplate.insertAccordingToTemplate(factoryItem, handlerList);
	}

	ControllerPipelineProcessor newControllerPipelineProcessor(final List<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList, final InterruptTracker interruptTracker) {
		final ControllerPipelineProcessor pipelineProcessor = new ControllerPipelineProcessor();
		pipelineProcessor.setInterruptTracker(interruptTracker);
		final List<ControllerPipelineHandlerFactory> factories = handlerDependencyGroupTemplate.toSubjectList(handlerList);
		for (final ControllerPipelineHandlerFactory factory : factories) {
			final ControllerPipelineHandler handler = factory.newControllerPipelineHandler();
			handler.setInterruptTracker(interruptTracker);
			pipelineProcessor.addHandlerLast(handler);
		}
		return pipelineProcessor;
	}

	ServiceInvokerControllerPipelineHandlerFactory newServiceInvokerControllerPipelineHandlerFactory() {
		final ServiceInvokerControllerPipelineHandlerFactory serviceInvokerControllerPipelineHandlerFactory = new ServiceInvokerControllerPipelineHandlerFactory();
		serviceInvokerControllerPipelineHandlerFactory.setServiceComponentFactory(serviceComponentFactory);
		return serviceInvokerControllerPipelineHandlerFactory;
	}

	private GenericControllerPipelineHandlerFactoryAdapter newGenericControllerPipelineHandlerFactoryAdapter() {
		final GenericControllerPipelineHandlerFactoryAdapter factory = new GenericControllerPipelineHandlerFactoryAdapter();
		factory.setCommonResources(getCommonResources());
		return factory;
	}

	ControllerPipelineHandlerFactory newFrameworkControllerPipelineHandlerFactory() {
		final GenericControllerPipelineHandlerFactoryAdapter factory = newGenericControllerPipelineHandlerFactoryAdapter();
		factory.setHandlerType(FrameworkControllerPipelineHandler.class);
		return factory;
	}

	ControllerPipelineHandlerFactory newRemoteSenderControllerPipelineHandlerFactory() {
		final GenericControllerPipelineHandlerFactoryAdapter factory = newGenericControllerPipelineHandlerFactoryAdapter();
		factory.setHandlerType(RemoteSenderControllerPipelineHandler.class);
		return factory;
	}

	ControllerPipelineHandlerFactory newRemoteReceiverControllerPipelineHandlerFactory() {
		final GenericControllerPipelineHandlerFactoryAdapter factory = newGenericControllerPipelineHandlerFactoryAdapter();
		factory.setHandlerType(RemoteReceiverControllerPipelineHandler.class);
		return factory;
	}

	ControllerPipelineHandlerFactory newLocalSenderControllerPipelineHandlerFactory() {
		final GenericControllerPipelineHandlerFactoryAdapter factory = newGenericControllerPipelineHandlerFactoryAdapter();
		factory.setHandlerType(LocalSenderControllerPipelineHandler.class);
		return factory;
	}

	ControllerPipelineHandlerFactory newLocalReceiverControllerPipelineHandlerFactory() {
		final GenericControllerPipelineHandlerFactoryAdapter factory = newGenericControllerPipelineHandlerFactoryAdapter();
		factory.setHandlerType(LocalReceiverControllerPipelineHandler.class);
		return factory;
	}


}
