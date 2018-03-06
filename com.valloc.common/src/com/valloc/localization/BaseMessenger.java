/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 *
 * @author wstevens
 */
class BaseMessenger implements Messenger
{
//	private static final String DEFAULT_TIMESTAMP_FORMAT	= "yyyy-MM-dd HH:mm:ss z";


	private Locale locale;
	private LocalizationManager localizationManager;
//	private MessageFormat messageFormatter;
//	private ChoiceFormat choiceFormatter;
//	private NumberFormat numberFormatter;
//	private DateFormat dateFormatter;

	// This is kind of clunky but we need one MessageFormat object per property string, which is
	//		reusable. This is because of how the API works from client code perspective and for
	//		efficiency's sake internally; additionally since they are not synchronized or sharable
	//		across multiple Messenger instances. So we might as well cache them for later reuse on
	//		single user.
	private final Map<String, MessageFormat> messageFormats = new HashMap<String, MessageFormat>();

	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Initializable#initialize()
	 */
	@Override
	public void initialize() {
		if (locale == null) {
			throw new IllegalStateException("Locale is not set, which is necessary for setup.");
		}

		// later initialization of the format objects
	}

	/* (non-Javadoc)
	 * @see com.valloc.localization.Messenger#string(java.lang.String)
	 */
	@Override
	public String string(final String key) {
		final String propertyValue = localizationManager.getBundleProperty(locale, key);
		return propertyValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.valloc.localization.Messenger#stringFormatted(java.lang.String, java.lang.Object[])
	 */
	@Override
	public String stringFormatted(final String key, final Object... args) {
		String formattedString = null;
		final String propertyValuePattern = localizationManager.getBundleProperty(locale, key);
		MessageFormat messageFormat = messageFormats.get(key);
		if (messageFormat == null) {
			messageFormat = new MessageFormat(propertyValuePattern, locale);
			messageFormats.put(key, messageFormat);
		}
		formattedString = messageFormat.format(args);

		return formattedString;
	}

	/* (non-Javadoc)
	 * @see com.valloc.localization.Messenger#getLocale()
	 */
	@Override
	public Locale getLocale() {
		return locale;
	}

	/* (non-Javadoc)
	 * @see com.valloc.localization.Messenger#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(final Locale locale) {
		this.locale = locale;

		initialize();
	}

	@Override
	public void setLocalizationManager(final LocalizationManager localizationManager) {
		this.localizationManager = localizationManager;
	}
}
