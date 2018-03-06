/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;


/**
 *
 *
 * @author wstevens
 */
abstract class AbstractKeys implements PropertyKeys
{
	private LocalizationManager localizationManager;
	private final BundleItemInfo bundleItemInfo;

	AbstractKeys(final BundleItemInfo bundleItemInfo) {
		this.bundleItemInfo = bundleItemInfo;
	}

	@Override
	public void loadKeys(final String[] keys) {
		for (final String key : keys) {
			final ResourceLookupObject resourceLookupObject = new ResourceLookupObject(bundleItemInfo, key);
			localizationManager.addResourceLookupObject(resourceLookupObject);
		}
	}

	@Override
	public void setLocalizationManager(final LocalizationManager localizationManager) {
		this.localizationManager = localizationManager;
	}
}
