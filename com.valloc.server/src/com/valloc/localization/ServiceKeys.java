/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import static com.valloc.Constants.DOT;
import static com.valloc.util.Util.buildString;

import com.valloc.ServerConstants;
import com.valloc.file.FileConstants;

/**
 *
 *
 * @author wstevens
 */
public class ServiceKeys extends AbstractKeys
{
	private static final String HOST_BUNDLE_SYMB_NAME		= ServerConstants.BUNDLE_ID_SERVER;
	private static final String BUNDLE_ITEM_ID				= ServerLocalizationConstants.BUNDLE_ID_SERVICE;
	private static final String SERVICE_PROPS_FILE_NAME		= BUNDLE_ITEM_ID + FileConstants.EXTENSION_PROPERTIES;

	// Target constants:
	public static final String SERVICE_COMPLETED_SUCCESSFULLY		= buildString(BUNDLE_ITEM_ID, DOT, "completed-successfully");
	public static final String SERVICE_COMPLETED_UNSUCCESSFULLY		= buildString(BUNDLE_ITEM_ID, DOT, "completed-unsuccessfully");

	static final String[] REGISTERED_KEYS = {
		SERVICE_COMPLETED_SUCCESSFULLY,
		SERVICE_COMPLETED_UNSUCCESSFULLY
	};

	ServiceKeys () {
		super(new BundleItemInfo(BUNDLE_ITEM_ID, HOST_BUNDLE_SYMB_NAME, SERVICE_PROPS_FILE_NAME));
	}

	@Override
	public String name() {
		return ServerLocalizationConstants.BUNDLE_ID_SERVICE;
	}

	@Override
	public void initialize() {
		loadKeys(REGISTERED_KEYS);
	}
}
