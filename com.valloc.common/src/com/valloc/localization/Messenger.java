/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.util.Locale;

import com.valloc.lifecycle.Initializable;

/**
 *
 *
 * @author wstevens
 */
public interface Messenger extends Initializable
{
	public Locale getLocale();
	public String string(String key);
	public String stringFormatted(String key, Object... args);

	// these desirable ??
//	public int integer(String key);
//	public Date timestamp(String key);
//	public Date convertLocalDateToServerDate(Date localDate);
//	public Date convertServerDateToLocalDate(Date serverDate);


	void setLocale(Locale locale);
	void setLocalizationManager(LocalizationManager localizationManager);
}
