/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system;

import com.valloc.framework.NodeType;

/**
 *
 *
 * @author wstevens
 */
public abstract class Participant
{
	private final String name;
	private final NodeType nodeType;

	Participant(final String name, final NodeType nodeType) {
		this.name = name;
		this.nodeType = nodeType;
	}

	public final String getName() {
		return name;
	}

	public final NodeType getNodeType() {
		return nodeType;
	}
}
