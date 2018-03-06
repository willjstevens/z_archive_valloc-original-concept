/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.config;

import com.valloc.CategoryType;

/**
 *
 *
 * @author wstevens
 */
public interface ConfigurationProvider<T extends Configuration>
{

	// returns first a cached config if available; if needbe then it loads a configuration
	public T getConfiguration();
	// forces a load and return
	public T loadConfiguration();
	// persists configuration to underlying source
	public void persistConfiguration(T configuration);

	public CategoryType getCategoryType();

	void setConfigurationManager(ConfigurationManager configurationManager);
}
