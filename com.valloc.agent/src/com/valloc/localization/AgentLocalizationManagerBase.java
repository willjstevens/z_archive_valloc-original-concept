/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.valloc.AgentConstants;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
class AgentLocalizationManagerBase extends AbstractLocalizationManager implements AgentLocalizationManager
{
	private static final Map<Locale, Messenger> sharedLocaleMessengers = new HashMap<Locale, Messenger>();
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
		loadBundlePackage(AgentConstants.BUNDLE_ID_AGENT);
		loadKeyPropertyClasses(REGISTERED_KEYS_CLASSES);
	}

	/* (non-Javadoc)
	 * @see com.valloc.localization.AgentLocalizationManager#getMessenger(java.util.Locale)
	 */
	@Override
	public Messenger getMessenger(final Locale locale) {
		Messenger messenger = sharedLocaleMessengers.get(locale);
		if (messenger == null) { // load on-demand
			messenger = new BaseMessenger();
			messenger.setLocale(locale);
			messenger.setLocalizationManager(this);
			messenger.initialize();
			sharedLocaleMessengers.put(locale, messenger);
		}
		return messenger;
	}

}
