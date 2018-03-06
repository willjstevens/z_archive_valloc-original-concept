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
public final class AgentConstants extends Constants
{
	private AgentConstants() {}

	public static final String BUNDLE_ID_AGENT				= BUNDLE_ID_BASE + "agent";
	public static final String BUNDLE_ID_AGENT_NL_ENGLISH 	= BUNDLE_ID_AGENT + FRAG_NL_SUFFIX_ENGLISH;
	public static final String BUNDLE_ID_AGENT_NL_GERMAN 	= BUNDLE_ID_AGENT + FRAG_NL_SUFFIX_GERMAN;
	public static final String BUNDLE_ID_AGENT_NL_FRENCH 	= BUNDLE_ID_AGENT + FRAG_NL_SUFFIX_FRENCH;

	public static final NodeType AGENT_PARTICIPANT_TYPE = NodeType.AGENT;
}
