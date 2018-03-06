/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.valloc.CategoryType;
import com.valloc.Constants;
import com.valloc.file.FileComponentFactory;
import com.valloc.file.FileConstants;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
abstract class AbstractLocalizationManager implements LocalizationManager
{
	private static final Logger logger = LogManager.manager().getLogger(AbstractLocalizationManager.class, CategoryType.LOCALIZATION);
	private static final String PROPERTY_NOT_FOUND = "PROPERTY_NOT_FOUND";
	private FileComponentFactory fileComponentFactory;
	private LocalizationFileInterpreter fileInterpreter;
	private final Map<String, ResourceLookupObject> lookupObjects = new HashMap<String, ResourceLookupObject>();
	private final Map<String, BundlePackage> bundlePackages = new HashMap<String, BundlePackage>();
	private final Map<String, PropertyKeys> propertyKeys = new HashMap<String, PropertyKeys>();

	private static final PropertyKeys[] SHARED_REGISTERED_KEYS_CLASSES = {
		Util.wrappedClassNewInstance(CommonKeys.class)
	};

	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Initializable#initialize()
	 */
	@Override
	public void initialize() {
		fileInterpreter = fileComponentFactory.newFileInterpreter(FileConstants.FILE_INTERPRETER_RESOURCE_BUNDLE);
		loadBundlePackage(Constants.BUNDLE_ID_COMMON);
		loadKeyPropertyClasses(SHARED_REGISTERED_KEYS_CLASSES);
	}

	@Override
	public void loadKeyPropertyClasses(final PropertyKeys[] registeredKeyClasses) {
		for (final PropertyKeys propertyKeysClass : registeredKeyClasses) {
			final String name = propertyKeysClass.name();
			propertyKeys.put(name, propertyKeysClass);
		}
	}

	@Override
	public void loadBundlePackage(final String bundleSymbolicName) {
		final BundlePackage bundlePackage = fileInterpreter.loadBundlePackage(bundleSymbolicName);
		bundlePackages.put(bundleSymbolicName, bundlePackage);
	}

	@Override
	public void addResourceLookupObject(final ResourceLookupObject resourceLookupObject) {
		lookupObjects.put(resourceLookupObject.getKey(), resourceLookupObject);
	}

	@Override
	public String getBundleProperty(final Locale locale, final String key) {
		String rawPropertyValue = PROPERTY_NOT_FOUND; // initialize to not found
		final BundleItemInfo bundleItemInfo = lookupObjects.get(key).getBundleItemInfo();
		final String bundleSymbolicName = bundleItemInfo.getBundleSymbolicName();
		final String bundleItemId = bundleItemInfo.getBundleItemId();

		final BundlePackage bundlePackage = bundlePackages.get(bundleSymbolicName);
		final ProvidedLocale providedLocale = bundlePackage.getProvidedLocale(locale);
		final PropertyFileBundleItem propertyFileBundleItem = providedLocale.getBundleItem(bundleItemId);
		if (!propertyFileBundleItem.isLoaded()) {
			final ResourceBundle resourceBundle = fileInterpreter.loadResourceBundle(propertyFileBundleItem.getItemUrl());
			propertyFileBundleItem.setResourceBundle(resourceBundle);
		}

		// now load parameterized value sting
		rawPropertyValue = propertyFileBundleItem.getResourceBundle().getString(key);
		if (logger.isFiner()) {
			logger.finer("Loaded property value \"%s\" for requested property key \"%s\".", rawPropertyValue, key);
		}

		return rawPropertyValue;
	}

	@Override
	public void setFileComponentFactory(final FileComponentFactory fileComponentFactory) {
		this.fileComponentFactory = fileComponentFactory;
	}
}
