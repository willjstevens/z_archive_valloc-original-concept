/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import com.valloc.Nameable;
import com.valloc.lifecycle.Initializable;

/**
 *
 *
 * @author wstevens
 */
//public interface PropertyKeys extends Identifiable<String>, Initializable
public interface PropertyKeys extends Nameable, Initializable
{
	void loadKeys(final String[] keys);
	void setLocalizationManager(final LocalizationManager localizationManager);
}
