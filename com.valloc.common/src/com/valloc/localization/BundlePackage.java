/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 *
 * @author wstevens
 */
final class BundlePackage
{
	private final String bundleSymbolicName;
	private final Map<Locale, ProvidedLocale> providedLocales = new HashMap<Locale, ProvidedLocale>();
	
	BundlePackage(final String bundleSymbolicName) {
		this.bundleSymbolicName = bundleSymbolicName;
	}
	
	String getBundleSymbolicName() {
		return bundleSymbolicName;
	}
	
	ProvidedLocale getProvidedLocale(final Locale locale) {
		return providedLocales.get(locale);
	}
	
	void addProvidedLocale(final ProvidedLocale providedLocale) {
		final Locale locale = providedLocale.getLocale();
		providedLocales.put(locale, providedLocale);
	}
	
	public boolean hasLocale(final Locale locale) {
		return providedLocales.containsKey(locale);
	}
}
