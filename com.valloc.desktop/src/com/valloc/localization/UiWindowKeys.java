/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import static com.valloc.Constants.DOT;
import static com.valloc.localization.DesktopLocalizationConstants.BUNDLE_ID_UI_WINDOW;
import static com.valloc.util.Util.buildString;

import com.valloc.DesktopConstants;
import com.valloc.file.FileConstants;

/**
 *
 *
 * @author wstevens
 */
public class UiWindowKeys extends AbstractKeys
{
	private static final String HOST_BUNDLE_SYMB_NAME		= DesktopConstants.BUNDLE_ID_DESKTOP;
	private static final String BUNDLE_ITEM_ID				= DesktopLocalizationConstants.BUNDLE_ID_UI_WINDOW;
	private static final String UI_WINDOW_PROPS_FILE_NAME	= BUNDLE_ITEM_ID + FileConstants.EXTENSION_PROPERTIES;

	// Building block constants:
//	private static final String UI_WINDOW_BUNDLE_PREFIX			= BUNDLE_ITEM_ID + DOT;
	private static final String UI_WINDOW_PERSPECTIVE_PREFIX 	= buildString(BUNDLE_ID_UI_WINDOW, DOT, "perspective", DOT);
	private static final String UI_WINDOW_PREFERENCE_PREFIX 	= buildString(BUNDLE_ID_UI_WINDOW, DOT, "preference", DOT);

	// Target constants:
	public static final String UI_WINDOW_TITLE						= buildString(BUNDLE_ID_UI_WINDOW, DOT, "title");
	public static final String UI_WINDOW_MAIN_PERSPECTIVE_TITLE		= buildString(UI_WINDOW_PERSPECTIVE_PREFIX, "main", DOT, "title");
	public static final String UI_WINDOW_ADMIN_PERSPECTIVE_TITLE	= buildString(UI_WINDOW_PERSPECTIVE_PREFIX, "admin", DOT, "title");
	public static final String UI_WINDOW_PREFERENCE_GENERAL_TITLE	= buildString(UI_WINDOW_PREFERENCE_PREFIX, "general", DOT, "title");
	public static final String UI_WINDOW_PREFERENCE_SERVER_TITLE	= buildString(UI_WINDOW_PREFERENCE_PREFIX, "server", DOT, "title");

	static final String[] REGISTERED_KEYS = {
		UI_WINDOW_TITLE,
		UI_WINDOW_MAIN_PERSPECTIVE_TITLE,
		UI_WINDOW_ADMIN_PERSPECTIVE_TITLE,
		UI_WINDOW_PREFERENCE_GENERAL_TITLE,
		UI_WINDOW_PREFERENCE_SERVER_TITLE
	};

	UiWindowKeys () {
		super(new BundleItemInfo(BUNDLE_ITEM_ID, HOST_BUNDLE_SYMB_NAME, UI_WINDOW_PROPS_FILE_NAME));
	}

	@Override
	public String name() {
		return DesktopLocalizationConstants.BUNDLE_ID_UI_WINDOW;
	}

	@Override
	public void initialize() {
		loadKeys(REGISTERED_KEYS);
	}
}
