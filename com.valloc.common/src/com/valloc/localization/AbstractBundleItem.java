/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.net.URL;

import com.valloc.Nameable;

/**
 *
 *
 * @author wstevens
 */
//abstract class AbstractBundleItem implements Identifiable<String>
abstract class AbstractBundleItem implements Nameable
{
	private final String name;
	private final URL itemUrl;
	private final ProvidedLocale parentProvidedLocale;

	AbstractBundleItem(final String name, final URL itemUrl, final ProvidedLocale parentProvidedLocale) {
		this.name = name;
		this.itemUrl = itemUrl;
		this.parentProvidedLocale = parentProvidedLocale;
	}

	abstract boolean isLoaded();

	@Override
	public String name() {
		return name;
	}

	URL getItemUrl() {
		return itemUrl;
	}

	ProvidedLocale getParentProvidedLocale() {
		return parentProvidedLocale;
	}
}
