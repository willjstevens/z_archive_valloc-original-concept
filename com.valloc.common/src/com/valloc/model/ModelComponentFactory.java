/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import java.util.HashMap;
import java.util.Map;

import com.valloc.AbstractComponentFactory;
import com.valloc.Constants;
import com.valloc.transport.TransportModelDao;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public class ModelComponentFactory extends AbstractComponentFactory
{
	private static final Object[][] commonDaoMappings = {
		{ModelDaoIds.TRANSPORT, TransportModelDao.class}
	};
	private final Map<String, Class<? extends ModelDao>> visitorMappings = new HashMap<String, Class<? extends ModelDao>>();

	private ModelManager modelManager;
	
	@Override
	public void initialize() {
		addMappings(commonDaoMappings);
		
		modelManager = newModelManager();
	}

	void addMappings(final Object[][] mappings) {
		for (final Object[] mapping : mappings) {
			final String daoId = (String) mapping[0];
			@SuppressWarnings(Constants.UNCHECKED)
			final Class<? extends ModelDao> daoType = (Class<? extends ModelDao>) mapping[1];
			visitorMappings.put(daoId, daoType);
		}
	}
	
	<T extends ModelDao> T newModelDao(final String modelDaoId) {
		final Class<?> modelDaoType = visitorMappings.get(modelDaoId);
		@SuppressWarnings(Constants.UNCHECKED)
		final T daoInstance = (T) Util.wrappedClassNewInstance(modelDaoType);		
		daoInstance.setModelManager(modelManager);		
		return daoInstance;
	}
	
	public ModelManager getModelManager() {
		return modelManager;
	}

	protected ModelManager newModelManager() {
		return new BaseModelManager();
	}
}
