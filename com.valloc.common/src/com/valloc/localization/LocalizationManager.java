/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.util.Locale;

import com.valloc.file.FileComponentFactory;
import com.valloc.lifecycle.Initializable;

/**
 *
 *
 * @author wstevens
 */
abstract interface LocalizationManager extends Initializable
{
	void loadBundlePackage(String bundleSymbolicName);
	void loadKeyPropertyClasses(PropertyKeys[] registeredKeyClasses);
	void addResourceLookupObject(ResourceLookupObject resourceLookupObject);
	String getBundleProperty(Locale locale, String key);

	public void setFileComponentFactory(FileComponentFactory fileComponentFactory);

	// what is this intended to be?  A byte[] for some image file?
//	<T> T getBundleItem(Locale locale, String key);
}
