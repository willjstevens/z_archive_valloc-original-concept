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
public interface ServerLocalizationManager extends LocalizationManager
{
	public Messenger newMessenger(Locale locale);
}
