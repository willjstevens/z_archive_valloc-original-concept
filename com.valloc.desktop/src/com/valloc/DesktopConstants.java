package com.valloc;

import com.valloc.framework.NodeType;


/*
 * Property of Will Stevens
 * All rights reserved.
 */

/**
 *
 *
 * @author wstevens
 */
public final class DesktopConstants extends Constants
{
	private DesktopConstants() {}

	public static final String BUNDLE_ID_DESKTOP				= BUNDLE_ID_BASE + "desktop";
	public static final String BUNDLE_ID_DESKTOP_NL_ENGLISH 	= BUNDLE_ID_DESKTOP + FRAG_NL_SUFFIX_ENGLISH;
	public static final String BUNDLE_ID_DESKTOP_NL_GERMAN 		= BUNDLE_ID_DESKTOP + FRAG_NL_SUFFIX_GERMAN;
	public static final String BUNDLE_ID_DESKTOP_NL_FRENCH 		= BUNDLE_ID_DESKTOP + FRAG_NL_SUFFIX_FRENCH;

	public static final NodeType DESKTOP_PARTICIPANT_TYPE = NodeType.DESKTOP;
}
