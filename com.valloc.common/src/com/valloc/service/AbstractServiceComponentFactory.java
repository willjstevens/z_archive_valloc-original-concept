/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import java.util.HashMap;
import java.util.Map;

import com.valloc.AbstractComponentFactory;
import com.valloc.Constants;
import com.valloc.concurrent.request.RequestContainer;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.ReferenceKit;
import com.valloc.framework.service.FrameworkControlClientBase;
import com.valloc.framework.service.FrameworkControlServiceFactory;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractServiceComponentFactory extends AbstractComponentFactory implements ServiceComponentFactory
{
	private final static Class<?>[] commonServiceFactories = {
		FrameworkControlServiceFactory.class
	};

	private static final GenericServiceFactoryKit[] genericBasedServiceKits = {
		newGenericServiceFactoryKit(ClientDirectory.FRAMEWORK_CONTROL, FrameworkControlClientBase.class)
	};

	private final Map<String, ServiceFactory> factoryMappings = new HashMap<String, ServiceFactory>();
	private RequestContainer requestContainer;

	@Override
	public void initialize() {
		loadServiceFactories(commonServiceFactories);
		loadGenericBasedImpls(genericBasedServiceKits);
	}

	void loadServiceFactories(final Class<?>[] factoryTypes) {
		for (final Class<?> factoryType : factoryTypes) {
			final ServiceFactory factory = (ServiceFactory) Util.wrappedClassNewInstance(factoryType);
			final String factoryName = factory.name();
			if (factoryMappings.containsKey(factoryName)) {
				throw new IllegalArgumentException(String.format("Service factory \"%s\" is already loaded; it cannot be reloaded."));
			}
			factoryMappings.put(factoryName, factory);
		}
	}

	void loadGenericBasedImpls(final GenericServiceFactoryKit[] genericBasedServiceKits) {
		for (final GenericServiceFactoryKit kit : genericBasedServiceKits) {
			final GenericServiceFactoryAdapter factory = newGenericServiceFactoryAdapter();
			factory.setId(kit.getId());
			factory.setServiceType(kit.getServiceImplType());
			factoryMappings.put(factory.name(), factory);
		}
	}

	UniqueId newServiceId() {
		return new UniqueId();
	}

	static GenericServiceFactoryKit newGenericServiceFactoryKit(final String factoryId, final Class<? extends Service>serviceImplType) {
		return new GenericServiceFactoryKit(ClientDirectory.FRAMEWORK_CONTROL, FrameworkControlClientBase.class);
	}

	static class GenericServiceFactoryKit {
		private final String id;
		private final Class<? extends Service> serviceImplType;
		GenericServiceFactoryKit(final String id, final Class<? extends Service> serviceImplType) {
			this.id = id;
			this.serviceImplType = serviceImplType;
		}
		String getId() {
			return id;
		}
		Class<? extends Service> getServiceImplType() {
			return serviceImplType;
		}
	}

	abstract FrameworkManager getFrameworkManager();
	abstract ReferenceKit newReferenceKit();
	abstract GenericServiceFactoryAdapter newGenericServiceFactoryAdapter();

	@Override
	//	public Service newClientService(final String serviceName) {
	public <S extends Service> S newClientService(final String serviceName) {

		final UniqueId serviceId = newServiceId();
		final ReferenceKit refKit = newReferenceKit(serviceId, serviceName);
		refKit.setInterruptTracker(getFrameworkManager().createAndRegisterInterruptTracker(serviceId));

		return buildService(refKit);
	}

	@Override
	//	public Service newControllerService(final String serviceName, final UniqueId serviceId, final InterruptTracker interruptTracker) {
	public <S extends Service> S newControllerService(final String serviceName, final UniqueId serviceId, final InterruptTracker interruptTracker) {

		final ReferenceKit refKit = newReferenceKit(serviceId, serviceName);
		refKit.setInterruptTracker(interruptTracker);

		return buildService(refKit);
	}

	private ReferenceKit newReferenceKit(final UniqueId serviceId, final String serviceName) {
		final ReferenceKit refKit = newReferenceKit();
		refKit.setServiceId(serviceId);
		refKit.setServiceName(serviceName);
		refKit.setRequestContainer(requestContainer);
		return refKit;
	}

	<S extends Service> S buildService(final ReferenceKit referenceKit) {
		final String serviceName = referenceKit.getServiceName();
		final ServiceFactory factory = factoryMappings.get(serviceName);
		@SuppressWarnings(Constants.UNCHECKED)
		final S retval = (S) factory.buildService(referenceKit);
		return retval;
	}

	public void setRequestContainer(final RequestContainer requestContainer) {
		this.requestContainer = requestContainer;
	}

}
