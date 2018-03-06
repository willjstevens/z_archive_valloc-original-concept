/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.net.URL;


/**
 *
 *
 * @author wstevens
 */
class ObjectBundleItem<T> extends AbstractBundleItem
{
	private T subject;

	ObjectBundleItem(final String id, final URL itemUrl, final ProvidedLocale parentProvidedLocale) {
		super(id, itemUrl, parentProvidedLocale);
	}

	T getSubject() {
		return subject;
	}
	
	void setSubject(final T subject) {
		this.subject = subject;
	}

	@Override
	boolean isLoaded() {
		return subject != null;
	}
}
