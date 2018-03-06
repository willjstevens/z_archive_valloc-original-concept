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
public interface AgentLocalizationManager extends LocalizationManager
{
	public Messenger getMessenger(Locale locale);
}
