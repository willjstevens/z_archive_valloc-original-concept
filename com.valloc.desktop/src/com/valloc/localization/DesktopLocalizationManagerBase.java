/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.util.Locale;

import com.valloc.DesktopConstants;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
class DesktopLocalizationManagerBase extends AbstractLocalizationManager implements DesktopLocalizationManager
{
	private Messenger messenger;

	private static final PropertyKeys[] REGISTERED_KEYS_CLASSES = {
		Util.wrappedClassNewInstance(UiWindowKeys.class)
	};

	/*
	 * (non-Javadoc)
	 * @see com.valloc.localization.AbstractLocalizationManager#initialize()
	 */
	@Override
	public void initialize() {
		super.initialize();
		loadBundlePackage(DesktopConstants.BUNDLE_ID_DESKTOP);
		loadKeyPropertyClasses(REGISTERED_KEYS_CLASSES);
	}

	/* (non-Javadoc)
	 * @see com.valloc.localization.DesktopLocalizationManager#getMessenger()
	 */
	@Override
	public Messenger getMessenger() {
		if (messenger == null) {
			throw new IllegalStateException("Messenger has not been initilized yet.");
		}
		return messenger;
	}

	@Override
	public void loadUserLocale(final Locale locale) {
		messenger = new BaseMessenger();
		messenger.setLocale(locale);
		messenger.setLocalizationManager(this);
		messenger.initialize();
	}
}
