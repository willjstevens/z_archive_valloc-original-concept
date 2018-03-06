/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import static com.valloc.Constants.DOT;

import com.valloc.Constants;
import com.valloc.file.FileConstants;

/**
 *
 *
 * @author wstevens
 */
public class CommonKeys extends AbstractKeys
{
	private static final String HOST_BUNDLE_SYMB_NAME	= Constants.BUNDLE_ID_COMMON;
	private static final String COMMON_PROPS_FILE_NAME	= LocalizationConstants.BUNDLE_ID_COMMON + FileConstants.EXTENSION_PROPERTIES;

	private static final String COMMON_BUNDLE_PREFIX	= LocalizationConstants.BUNDLE_ID_COMMON + DOT;
	public static final String OKAY						= COMMON_BUNDLE_PREFIX + "okay";
	public static final String CANCEL					= COMMON_BUNDLE_PREFIX + "cancel";

	static final String[] REGISTERED_KEYS = {
		OKAY,
		CANCEL
	};

	CommonKeys () {
		super(new BundleItemInfo(LocalizationConstants.BUNDLE_ID_COMMON, HOST_BUNDLE_SYMB_NAME, COMMON_PROPS_FILE_NAME));
	}

	@Override
	public String name() {
		return LocalizationConstants.BUNDLE_ID_COMMON;
	}

	@Override
	public void initialize() {
		loadKeys(REGISTERED_KEYS);
	}
}
