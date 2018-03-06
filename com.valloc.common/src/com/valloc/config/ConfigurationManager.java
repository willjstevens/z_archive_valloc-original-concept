/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.valloc.CategoryType;

/**
 *
 *
 * @author wstevens
 */
public final class ConfigurationManager
{
//	private static final Logger logger = LogManager.manager().getLogger(ConfigurationManager.class, CategoryType.CONFIG);


	private static final Map<CategoryType, ConfigurationProvider<Configuration>> providers =
		new HashMap<CategoryType, ConfigurationProvider<Configuration>>();

	private static final Map<CategoryType, List<ConfigurationChangeListener<Configuration>>> listeners =
		new HashMap<CategoryType, List<ConfigurationChangeListener<Configuration>>>();

	public void addChangeListener(final ConfigurationChangeListener<Configuration> listener) {
		final CategoryType categoryType = listener.getConfigurationCategoryType();
		List<ConfigurationChangeListener<Configuration>> categoryTypeListeners = listeners.get(categoryType);
		if (categoryTypeListeners == null) {
			categoryTypeListeners = new ArrayList<ConfigurationChangeListener<Configuration>>();
			listeners.put(categoryType, categoryTypeListeners);
		}
		categoryTypeListeners.add(listener);
	}

	public void removeChangeListener(final ConfigurationChangeListener<Configuration> listener) {
		final CategoryType categoryType = listener.getConfigurationCategoryType();
		final List<ConfigurationChangeListener<Configuration>> categoryTypeListeners = listeners.get(categoryType);
		if (categoryTypeListeners != null) {
			categoryTypeListeners.remove(listener);
		}
	}

	public void propagateChangeEvent(final ChangeEvent<Configuration> changeEvent) {
		final CategoryType categoryType = changeEvent.getCategoryType();
		final List<ConfigurationChangeListener<Configuration>> categoryTypeListeners = listeners.get(categoryType);
		for (final ConfigurationChangeListener<Configuration> listener : categoryTypeListeners) {
			listener.configurationChanged(changeEvent);
		}
	}

	public void addConfigurationProvider(final ConfigurationProvider<Configuration> provider) {
		final CategoryType categoryType = provider.getCategoryType();
		providers.put(categoryType, provider);
	}

	public ConfigurationProvider<Configuration> getConfigurationProvider(final CategoryType categoryType) {
		return providers.get(categoryType);
	}
}
