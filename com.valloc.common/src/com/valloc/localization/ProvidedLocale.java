/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.valloc.Constants;

/**
 *
 *
 * @author wstevens
 */
final class ProvidedLocale
{
	private final Locale locale;
	private final URL localeDirectory;
	private final BundlePackage parentBundlePackage;
	private final Map<String, AbstractBundleItem> bundleItems = new HashMap<String, AbstractBundleItem>();

	ProvidedLocale(final Locale locale, final URL localeDirectory, final BundlePackage parentBundlePackage) {
		this.locale = locale;
		this.localeDirectory = localeDirectory;
		this.parentBundlePackage = parentBundlePackage;
	}

	Locale getLocale() {
		return locale;
	}

	URL getLocaleDirectory() {
		return localeDirectory;
	}

	BundlePackage getParentBundlePackage() {
		return parentBundlePackage;
	}

	<T extends AbstractBundleItem> T getBundleItem(final String bundleId) {
		@SuppressWarnings(Constants.UNCHECKED)
		final T retval = (T) bundleItems.get(bundleId);
		return retval;
	}

	void addBundleItem(final AbstractBundleItem bundleItem) {
		final String bundleId = bundleItem.name();
		bundleItems.put(bundleId, bundleItem);
	}
}
