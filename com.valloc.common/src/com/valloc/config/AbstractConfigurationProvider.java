/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.config;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractConfigurationProvider<T extends Configuration> implements ConfigurationProvider<T>
{
	private ConfigurationManager configurationManager;


	protected ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	@Override
	public void setConfigurationManager(final ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
}
