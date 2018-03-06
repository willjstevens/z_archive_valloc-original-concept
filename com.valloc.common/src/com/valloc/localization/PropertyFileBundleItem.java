/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 *
 * @author wstevens
 */
class PropertyFileBundleItem extends AbstractBundleItem
{
	private ResourceBundle resourceBundle;

	PropertyFileBundleItem(final String id, final URL itemUrl, final ProvidedLocale parentProvidedLocale) {
		super(id, itemUrl, parentProvidedLocale);
	}

	@Override
	boolean isLoaded() {
		return resourceBundle != null;
	}

	ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	void setResourceBundle(final ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}
}
