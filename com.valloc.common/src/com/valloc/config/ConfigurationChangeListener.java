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
public interface ConfigurationChangeListener<T extends Configuration>
{
	public void configurationChanged(ChangeEvent<T> changeEvent);
	public CategoryType getConfigurationCategoryType();
}
