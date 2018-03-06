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
public final class ServerConstants extends Constants
{
	private ServerConstants() {}

	public static final String BUNDLE_ID_SERVER				= BUNDLE_ID_BASE + "server";
	public static final String BUNDLE_ID_SERVER_NL_ENGLISH 	= BUNDLE_ID_SERVER + FRAG_NL_SUFFIX_ENGLISH; 
	public static final String BUNDLE_ID_SERVER_NL_GERMAN 	= BUNDLE_ID_SERVER + FRAG_NL_SUFFIX_GERMAN;
	public static final String BUNDLE_ID_SERVER_NL_FRENCH 	= BUNDLE_ID_SERVER + FRAG_NL_SUFFIX_FRENCH;
	
	public static final NodeType SERVER_NODE_TYPE = NodeType.SERVER;
}
