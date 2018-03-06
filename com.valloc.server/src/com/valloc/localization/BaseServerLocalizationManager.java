/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.util.Locale;

import com.valloc.ServerConstants;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
class BaseServerLocalizationManager extends AbstractLocalizationManager implements ServerLocalizationManager
{
	private static final PropertyKeys[] REGISTERED_KEYS_CLASSES = {
		Util.wrappedClassNewInstance(ServiceKeys.class)
	};

	/*
	 * (non-Javadoc)
	 * @see com.valloc.localization.AbstractLocalizationManager#initialize()
	 */
	@Override
	public void initialize() {
		super.initialize();
		loadBundlePackage(ServerConstants.BUNDLE_ID_SERVER);
		loadKeyPropertyClasses(REGISTERED_KEYS_CLASSES);
	}

	/* (non-Javadoc)
	 * @see com.valloc.localization.ServerLocalizationManager#newMessenger(java.util.Locale)
	 */
	@Override
	public Messenger newMessenger(final Locale locale) {
		final Messenger messenger = new BaseMessenger();
		messenger.setLocale(locale);
		messenger.setLocalizationManager(this);
		messenger.initialize();
		return messenger;
	}
}
