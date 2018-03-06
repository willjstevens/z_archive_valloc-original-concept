/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.util.Locale;

/**
 *
 *
 * @author wstevens
 */
public interface DesktopLocalizationManager extends LocalizationManager
{
	public Messenger getMessenger();
	public void loadUserLocale(Locale locale);
}
